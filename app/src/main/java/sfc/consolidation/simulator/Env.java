package sfc.consolidation.simulator;

import java.io.File;
import java.util.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.cloudsimplus.core.CloudSimPlus;
import org.cloudsimplus.resources.PeSimple;
import org.cloudsimplus.vms.network.NetworkVm;
import org.cloudsimplus.vms.HostResourceStats;
import org.cloudsimplus.hosts.network.NetworkHost;
import org.cloudsimplus.power.models.PowerModelHost;
import org.cloudsimplus.power.models.PowerModelHostSimple;
import org.cloudsimplus.brokers.DatacenterBroker;
import org.cloudsimplus.brokers.DatacenterBrokerSimple;
import org.cloudsimplus.network.switches.EdgeSwitch;
import org.cloudsimplus.builders.tables.CloudletsTableBuilder;
import org.cloudsimplus.cloudlets.network.NetworkCloudlet;
import org.cloudsimplus.cloudlets.network.CloudletExecutionTask;
import org.cloudsimplus.cloudlets.network.CloudletReceiveTask;
import org.cloudsimplus.cloudlets.network.CloudletSendTask;
import org.cloudsimplus.schedulers.vm.VmSchedulerTimeShared;
import org.cloudsimplus.schedulers.cloudlet.CloudletSchedulerTimeShared;
import org.cloudsimplus.datacenters.network.NetworkDatacenter;
import org.cloudsimplus.provisioners.ResourceProvisionerSimple;
import org.cloudsimplus.utilizationmodels.UtilizationModelFull;

import lombok.Getter;

import com.fasterxml.jackson.databind.ObjectMapper;

import sfc.consolidation.simulator.types.SFC;
import sfc.consolidation.simulator.types.SRV;
import sfc.consolidation.simulator.types.VNF;
import sfc.consolidation.simulator.types.State;
import sfc.consolidation.simulator.types.Action;
import sfc.consolidation.simulator.types.Info;
import sfc.consolidation.simulator.types.Rack;
import sfc.consolidation.simulator.utils.Constants;
import sfc.consolidation.simulator.utils.GeneratorSingleton;

@Getter
public class Env {
  private final State initState;
  private State curState;

  private static final int SCHEDULING_INTERVAL = 5;

  private static final int CLOUDLET_LENGTH = 2000; // Task의 length가 Cloudlet보다 커야함.
  private static final int TASK_LENGTH = 4000;
  private static final int TASK_MEM_MB = 1000;
  private static final int NUMBER_OF_PACKETS_TO_SEND = 1;
  private static final long PACKET_DATA_LENGTH_IN_BYTES = 1000;

  private State readFile(String topology_path) {
    try {
      ObjectMapper om = new ObjectMapper();
      State s = om.readValue(new File(topology_path), State.class);
      return s;
    } catch (IOException e) {
      e.printStackTrace();
      System.out.print(e);
      return null;
    }
  }

  public Env(String topology_path) {
    // If list is null, generate below default setup.
    if (topology_path == null) {
      curState = GeneratorSingleton.getRandomState(
          2, 2, // # racks = 2
          3, 3, // # SRVs = 3
          5, 5, // # SFCs = 5
          3, 3, // # VNFs = 3
          100, 100, // # vCPUs = 100
          32 * 1024, 32 * 1024, // # vMem = 32GB
          1, 10, // # vCPUs per VNF = 1~10
          1024 / 2, 1024 * 4); // # vMem per VNF = 512MB ~ 4GB
    } else {
      curState = readFile(topology_path);
    }
    initState = curState.capture(); // deep copy
  }

  private void setupCloudsimWithState(State state) {
    state = state.capture(); // deep copy
    final List<Rack> rackList = state.getRackList();
    final List<SRV> srvList = state.getSrvList();
    final List<SFC> sfcList = state.getSfcList();
    final List<VNF> vnfList = state.getVnfList();

    // 1. Setup SrvList.
    for (SRV srv : srvList) {
      NetworkHost host = new NetworkHost(
          srv.getTotVmemMb(),
          Constants.SRV_BW,
          GeneratorSingleton.getStorageMb(srv.getTotVmemMb()),
          Collections.nCopies(Constants.VCPU_MIPS, new PeSimple(srv.getTotVcpuNum())));

      host.setRamProvisioner(new ResourceProvisionerSimple())
          .setBwProvisioner(new ResourceProvisionerSimple())
          .setVmScheduler(new VmSchedulerTimeShared());
      // 👇 TODO: shutdown / sleep을 적용하는 것도 고려할지 생각해야할 듯.
      PowerModelHost pm = new PowerModelHostSimple(
          GeneratorSingleton.getMaxPower(srv.getTotVcpuNum(), srv.getTotVmemMb()),
          GeneratorSingleton.getIdlePower(srv.getTotVcpuNum(), srv.getTotVmemMb()));
      host.setPowerModel(pm);
      host.enableUtilizationStats();
      srv.setHost(host);
    }

    // 2. Setup CloudSimPlus simulator and NetworkDatacenter.
    CloudSimPlus simulator = new CloudSimPlus();
    NetworkDatacenter dc = new NetworkDatacenter(simulator, srvList.stream().map(SRV::getHost).toList());
    dc.setSchedulingInterval(SCHEDULING_INTERVAL);

    // 3. Setup EdgeList
    for (Rack rack : rackList) {
      EdgeSwitch swit = new EdgeSwitch(simulator, dc);
      rack.setTorSwitch(swit);
      dc.addSwitch(swit);
      for (SRV srv : rack.getSrvList()) {
        swit.connectHost(srv.getHost());
      }
    }

    // 4. Setup SFCList's broker
    for (SFC sfc : sfcList) {
      DatacenterBroker brok = new DatacenterBrokerSimple(simulator);
      sfc.setBroker(brok);
    }

    // 5. Setup VNFList's vm and cloudlet
    for (VNF vnf : vnfList) {
      if (vnf.getVm() == null) {
        final var vm = new NetworkVm(vnf.getId(), Constants.VCPU_MIPS, vnf.getReqVcpuNum());
        vm.setRam(vnf.getReqVmemMb());
        vm.setBw(Constants.SRV_BW / 10);
        vm.setSize(GeneratorSingleton.getStorageMb(vnf.getReqVmemMb()));
        vm.setCloudletScheduler(new CloudletSchedulerTimeShared());
        vm.enableUtilizationStats();
        vm.setId(vnf.getId());
        vm.setBroker(sfcList.get(vnf.getSfcId()).getBroker());
        vnf.setVm(vm);
        srvList.get(vnf.getSrvId()).getHost().createVm(vnf.getVm());

        final var cloudlet = new NetworkCloudlet(CLOUDLET_LENGTH, vnf.getReqVcpuNum());
        cloudlet.setUtilizationModel(new UtilizationModelFull());
        cloudlet.setId(vnf.getId());
        cloudlet.setVm(vm);
        cloudlet.setBroker(sfcList.get(vnf.getSfcId()).getBroker());
        vnf.setCloudlet(cloudlet);
      }
    }

    // 6. Run packet transmission simulation to each SFC.
    for (SFC sfc : sfcList) {
      // get sfc's vnf list and sort by orderInSfc
      List<VNF> vl = sfc.getVNFList(vnfList).stream().sorted(Comparator.comparingInt(VNF::getOrderInSfc)).toList();
      List<NetworkVm> vml = vl.stream().map(VNF::getVm).toList();
      List<NetworkCloudlet> cl = vl.stream().map(VNF::getCloudlet).toList();
      for (int i = 0; i < cl.size() - 1; i++) {
        addExecutionTask(cl.get(i));
        addSendTask(cl.get(i), cl.get(i + 1));
        addReceiveTask(cl.get(i + 1), cl.get(i));
      }
      if (cl.size() > 0)
        addExecutionTask(cl.get(cl.size() - 1));

      sfc.getBroker().submitVmList(vml);
      sfc.getBroker().submitCloudletList(cl);
    }

    // 7. Run simulation.
    simulator.start();

    // 8. Print results.
    curState = state;
    state.print();
    showSimulationResults(dc, sfcList.stream().map(SFC::getBroker).toList());
    getResult().print();
    // getPowerConsumption(state);
  }

  public State step(Action action) {
    int tgtSrvId = action.getSrvId();
    int srcVnfId = action.getVnfId();

    curState.getVnfList().get(srcVnfId).setSrvId(tgtSrvId);
    setupCloudsimWithState(curState);

    return curState.capture();
  }

  public State reset() {
    setupCloudsimWithState(curState);

    return curState.capture();
  }

  public Info getResult() {
    Info info = new Info();
    List<Float> powerList = new ArrayList<>();
    List<Float> cpuUtilList = new ArrayList<>();
    List<Float> memUtilList = new ArrayList<>();
    List<Integer> bwUtilList = new ArrayList<>();
    int sleepNum = 0; // TODO: change

    for (Rack rack : curState.getRackList()) {
      for (SRV srv : rack.getSrvList()) {
        final var host = srv.getHost();
        final HostResourceStats cpuStats = host.getCpuUtilizationStats();
        final double memUtil = host.getRam().getPercentUtilization();
        final double cpuUtil = cpuStats.getMean();
        final double watts = host.getPowerModel().getPower(cpuUtil);
        final double bwUtil = host.getBwUtilization();
        powerList.add((float) watts);
        cpuUtilList.add((float) cpuUtil);
        memUtilList.add((float) memUtil);
        bwUtilList.add((int) bwUtil);
      }
    }
    info.setCpuUtilList(cpuUtilList);
    info.setMemUtilList(memUtilList);
    info.setPowerList(powerList);
    info.setBwUtilList(bwUtilList);
    info.setSleepNum(sleepNum);
    return info;
  }

  private void getPowerConsumption(State state) {
    for (Rack rack : state.getRackList()) {
      for (SRV srv : rack.getSrvList()) {
        final var host = srv.getHost();
        final HostResourceStats cpuStats = host.getCpuUtilizationStats();

        final double utilizationPercentMean = cpuStats.getMean();
        final double watts = host.getPowerModel().getPower(utilizationPercentMean);
        final double bwUtil = host.getBwUtilization();

        System.out.printf(
            "[Host %d] CPU Usage: %6.1f%% | Power Consumption: %8.0f W | Bandwidth Util: %8.0f%n",
            host.getId(), utilizationPercentMean * 100, watts, bwUtil);
      }
    }
  }

  /**
   * Adds an execution-task to the list of tasks of the given
   * {@link NetworkCloudlet}.
   *
   * @param cloudlet the {@link NetworkCloudlet} the task will belong to
   */
  private static void addExecutionTask(NetworkCloudlet cloudlet) {
    final var task = new CloudletExecutionTask(cloudlet.getTasks().size(), TASK_LENGTH);
    task.setMemory(TASK_MEM_MB);
    cloudlet.addTask(task);
  }

  /**
   * Adds a send-task to the list of tasks of the given {@link NetworkCloudlet}.
   *
   * @param sourceCloudlet      the {@link NetworkCloudlet} from which packets
   *                            will be sent
   * @param destinationCloudlet the destination {@link NetworkCloudlet} to send
   *                            packets to
   */
  private void addSendTask(final NetworkCloudlet sourceCloudlet, final NetworkCloudlet destinationCloudlet) {
    final var task = new CloudletSendTask(sourceCloudlet.getTasks().size());
    task.setMemory(TASK_MEM_MB);
    sourceCloudlet.addTask(task);
    for (int i = 0; i < NUMBER_OF_PACKETS_TO_SEND; i++) {
      task.addPacket(destinationCloudlet, PACKET_DATA_LENGTH_IN_BYTES);
    }
  }

  /**
   * Adds a receive-task to the list of tasks of the given
   * {@link NetworkCloudlet}.
   *
   * @param cloudlet       the {@link NetworkCloudlet} the task will belong to
   * @param sourceCloudlet the {@link NetworkCloudlet} expected to receive packets
   *                       from
   */
  private void addReceiveTask(final NetworkCloudlet cloudlet, final NetworkCloudlet sourceCloudlet) {
    final var task = new CloudletReceiveTask(cloudlet.getTasks().size(), sourceCloudlet.getVm());
    task.setMemory(TASK_MEM_MB);
    task.setExpectedPacketsToReceive(NUMBER_OF_PACKETS_TO_SEND);
    cloudlet.addTask(task);
  }

  private void showSimulationResults(NetworkDatacenter dc, List<DatacenterBroker> brokerList) {
    final var cloudletFinishedList = brokerList.stream().flatMap(broker -> broker.getCloudletFinishedList().stream())
        .toList();
    new CloudletsTableBuilder(cloudletFinishedList).build();

    System.out.println();
    for (NetworkHost host : dc.getHostList()) {
      System.out.printf("Host %d data transferred: %d bytes%n",
          host.getId(), host.getTotalDataTransferBytes());
    }

    System.out.println("Simulation finished!");
  }

}