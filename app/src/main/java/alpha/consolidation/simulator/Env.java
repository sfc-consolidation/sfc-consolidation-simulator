package alpha.consolidation.simulator;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import lombok.Getter;
import org.cloudsimplus.core.CloudSimPlus;
import org.cloudsimplus.resources.PeSimple;
import org.cloudsimplus.hosts.network.NetworkHost;
import org.cloudsimplus.brokers.DatacenterBroker;
import org.cloudsimplus.brokers.DatacenterBrokerSimple;
import org.cloudsimplus.cloudlets.network.NetworkCloudlet;
import org.cloudsimplus.datacenters.network.NetworkDatacenter;
import org.cloudsimplus.power.models.PowerModelHost;
import org.cloudsimplus.power.models.PowerModelHostSimple;
import org.cloudsimplus.schedulers.vm.VmSchedulerTimeShared;
import org.cloudsimplus.schedulers.cloudlet.CloudletSchedulerTimeShared;
import org.cloudsimplus.utilizationmodels.UtilizationModelDynamic;
import org.cloudsimplus.vms.HostResourceStats;
import org.cloudsimplus.vms.network.NetworkVm;

import alpha.consolidation.simulator.types.SFC;
import alpha.consolidation.simulator.types.SRV;
import alpha.consolidation.simulator.types.VNF;
import alpha.consolidation.simulator.types.State;
import alpha.consolidation.simulator.types.Action;
import alpha.consolidation.simulator.utils.Constants;
import alpha.consolidation.simulator.utils.RandomSingleton;

@Getter
public class Env {
  private final State initState;
  private final State curState;

  private final CloudSimPlus simulator;
  private final NetworkDatacenter dc;
  private final DatacenterBroker dcBroker;
  private int step = 1;

  private static final int SCHEDULING_INTERVAL = 2;

  public Env(int srvNum, int sfcNum, int vnfNum, List<SRV> srvList, List<SFC> sfcList, List<VNF> vnfList) {
    // If list is null, generate randomly.
    if (srvList == null) {
      srvList = generate_random_srvs(srvNum);
    }
    if (sfcList == null) {
      sfcList = generate_random_sfcs(sfcNum);
    }
    if (vnfList == null) {
      vnfList = generate_random_vnfs(srvNum, sfcNum, vnfNum, srvList, sfcList);
    }
    // CloudSimPlus Setup
    for (SRV srv : srvList) {
      if (srv.getHost() == null) {
        NetworkHost host = new NetworkHost(
            srv.getTotVmemMb(),
            Constants.HOST_BW,
            Constants.HOST_STORAGE,
            Collections.nCopies(srv.getTotVcpuNum(), new PeSimple(Constants.HOST_MIPS)));
        // 👇 TODO: shutdown도 고려할지 생각해야할 듯.
        PowerModelHost pm = new PowerModelHostSimple(Constants.HOST_MAX_POWER, Constants.HOST_IDLE_POWER);
        host.setPowerModel(pm);
        host.setId(srv.getId());
        host.setVmScheduler(new VmSchedulerTimeShared());
        host.enableUtilizationStats();
        srv.setHost(host);
      }
    }
    for (VNF vnf : vnfList) {
      if (vnf.getVm() == null) {
        // 👇 TODO: Length가 뭔지 확인해야함. (해당 Cloudlet의 수행 시간을 의미하는 것으로 추정 중..)
        final var cloudlet = new NetworkCloudlet(Constants.HOST_MIPS * 10, vnf.getReqVcpuNum());
        cloudlet.setUtilizationModel(new UtilizationModelDynamic(0.5));
        cloudlet.setSizes(Constants.HOST_STORAGE / 100);
        vnf.setCloudlet(cloudlet);
        final var vm = new NetworkVm(Constants.VM_MIPS, vnf.getReqVcpuNum());
        vm.setRam(vnf.getReqVmemMb());
        vm.setBw(Constants.HOST_BW / 10);
        vm.setSize(Constants.HOST_STORAGE / 100);
        vm.setCloudletScheduler(new CloudletSchedulerTimeShared());
        vm.enableUtilizationStats();
        vnf.setVm(vm); // TODO: VM MIPS를 HOST랑 맞추는 게 좋은지 확인
        // vnf.getCloudlet().setVm(vnf.getVm());
        // vnf.getVm().setHost(srvList.get(vnf.getSrvId()).getHost());
      }
    }
    simulator = new CloudSimPlus();
    dc = new NetworkDatacenter(simulator, srvList.stream().map(SRV::getHost).toList());
    dc.setSchedulingInterval(SCHEDULING_INTERVAL);
    dcBroker = new DatacenterBrokerSimple(simulator);

    // Link each Cloudlet to a spacific VM
    for (int i = 0; i < vnfNum; i++) {
      dcBroker.bindCloudletToVm(vnfList.get(i).getCloudlet(), vnfList.get(i).getVm());
    }

    // Set state
    curState = new State();
    curState.setSrvList(srvList);
    curState.setSfcList(sfcList);
    curState.setVnfList(vnfList);

    initState = curState.capture(); // deep copy
  }

  /*
   * generate_random_srvs
   * Generate random SRVs.
   * You must generate in order of SRV, SFC, VNF.
   * 
   * @param srvNum: number of SRVs to generate.
   * 
   * @return: a list of SRVs.
   */
  private List<SRV> generate_random_srvs(int srvNum) {
    List<SRV> srvList = new ArrayList<>();
    for (int i = 0; i < srvNum; ++i) {
      SRV srv = new SRV();
      srv.setId(i);
      srv.setTotVcpuNum(
          RandomSingleton.getInstance().nextInt(Constants.MIN_SRV_VCPU_NUM, Constants.MAX_SRV_VCPU_NUM + 1));
      srv.setTotVmemMb(
          RandomSingleton.getInstance().nextInt(Constants.MIN_SRV_VMEM_MB, Constants.MAX_SRV_VMEM_MB + 1));
      srv.setSleepable(RandomSingleton.getInstance().nextFloat() < Constants.SRV_SLEEPABLE_PROB);
      srvList.add(srv);
    }
    return srvList;
  }

  /*
   * generate_random_sfcs
   * Generate random SFCs.
   * You must generate in order of SRV, SFC, VNF.
   * 
   * @param sfcNum: number of SFCs to generate.
   * 
   * @return: a list of SFCs.
   */
  private List<SFC> generate_random_sfcs(int sfcNum) {
    List<SFC> sfcList = new ArrayList<>();
    for (int i = 0; i < sfcNum; ++i) {
      SFC sfc = new SFC();
      sfc.setId(i);
      sfcList.add(sfc);
    }
    return sfcList;
  }

  /*
   * generate_random_vnfs
   * Generate random VNFs.
   * You must generate in order of SRV, SFC, VNF.
   * 
   * @param srvNum: number of SRVs.
   * 
   * @param sfcNum: number of SFCs.
   * 
   * @param vnfNum: number of VNFs to generate.
   * 
   * @return: a list of VNFs.
   */
  private List<VNF> generate_random_vnfs(int srvNum, int sfcNum, int vnfNum, List<SRV> srvList, List<SFC> sfcList) {
    List<VNF> vnfList = new ArrayList<>();
    for (int i = 0; i < vnfNum; ++i) {
      VNF vnf = new VNF();
      vnf.setId(i);
      vnf.setSrvId(RandomSingleton.getInstance().nextInt(srvNum));
      vnf.setSfcId(RandomSingleton.getInstance().nextInt(sfcNum));
      vnf.setReqVcpuNum(
          RandomSingleton.getInstance().nextInt(Constants.MIN_VNF_VCPU_NUM, Constants.MAX_VNF_VCPU_NUM + 1));
      vnf.setReqVmemMb(
          RandomSingleton.getInstance().nextInt(Constants.MIN_VNF_VMEM_MB, Constants.MAX_VNF_VMEM_MB + 1));
      vnf.setMovable(RandomSingleton.getInstance().nextFloat() < Constants.VNF_MOVABLE_PROB);
      vnfList.add(vnf);
    }
    return vnfList;
  }

  public State step(State state, Action action) {
    // TODO: Implement this.
    return curState.capture();
  }

  public State reset() {
    dcBroker.submitVmList(curState.getVnfList().stream().map(VNF::getVm).toList());
    dcBroker.submitCloudletList(curState.getVnfList().stream().map(VNF::getCloudlet).toList());
    simulator.start();
    // TODO: Implement this.
    return curState.capture();
  }

  public void getPowerConsumption() {
    for (SRV srv : curState.getSrvList()) {
      final var host = srv.getHost();
      final HostResourceStats cpuStats = host.getCpuUtilizationStats();

      final double utilizationPercentMean = cpuStats.getMean();
      final double watts = host.getPowerModel().getPower(utilizationPercentMean);

      System.out.printf(
          "[Host %d] CPU Usage: %6.1f%% | Power Consumption: %8.0f W%n",
          host.getId(), utilizationPercentMean * 100, watts);
    }
  }
}
