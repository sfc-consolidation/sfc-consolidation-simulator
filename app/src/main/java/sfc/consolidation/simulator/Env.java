package sfc.consolidation.simulator;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.cloudsimplus.core.CloudSimPlus;
import org.cloudsimplus.resources.Pe;
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
import sfc.consolidation.simulator.types.ResetArg;
import sfc.consolidation.simulator.utils.Constants;
import sfc.consolidation.simulator.utils.GeneratorSingleton;

@Getter
public class Env {
  private State initState;
  private State curState;

  private static final int SCHEDULING_INTERVAL = 5;

  private static final int CLOUDLET_LENGTH = 2000; // Task의 length가 Cloudlet보다 커야함.
  private static final int TASK_LENGTH = 4000;
  private static final int TASK_MEM_MB = 1000;
  private static final int NUMBER_OF_PACKETS_TO_SEND = 1;
  private static final long PACKET_DATA_LENGTH_IN_BYTES = 1000;

  private boolean lastIsSuccess;

  public Env(String mode, String file_or_raw) {
    if (mode == "file") {
      curState = readFile(file_or_raw);
    } else if (mode == "raw") {
      curState = readRaw(file_or_raw);
    } else {
      ResetArg arg = new ResetArg();
      {
        // # racks = 2
        arg.setMinRackNum(2);
        arg.setMaxRackNum(2);
        // # SRVs = 3
        arg.setMinSrvNumInSingleRack(3);
        arg.setMaxSrvNumInSingleRack(3);
        // # VNFs = 5
        arg.setMinVnfNum(5);
        arg.setMaxVnfNum(10);
        // # SFCs = 3
        arg.setMinSfcNum(3);
        arg.setMaxSfcNum(3);
        // # vCPUs = 100
        arg.setMinSrvVcpuNum(100);
        arg.setMaxSrvVcpuNum(100);
        // # vMem = 32GB
        arg.setMinSrvVmemMb(32 * 1024);
        arg.setMaxSrvVmemMb(32 * 1024);
        // # vCPUs per VNF = 1~10
        arg.setMinVnfVcpuNum(1);
        arg.setMaxVnfVcpuNum(10);
        // # vMem per VNF = 512MB ~ 4GB
        arg.setMinVnfVmemMb(1024 / 2);
        arg.setMaxVnfVmemMb(1024 * 4);
      }
      curState = GeneratorSingleton.getRandomState(arg);
    }
    // If list is null, generate below default setup.
    initState = curState.capture(); // deep copy
  }

  private void setupCloudsimWithState(State state) {
    state = state.capture(); // deep copy
    final List<Rack> rackList = state.getRackList();
    final List<SRV> srvList = state.getSrvList();
    final List<SFC> sfcList = state.getSfcList();
    final List<VNF> vnfList = state.getVnfList();

    CloudSimPlus simulator = new CloudSimPlus();

    // 0. Setup ClientDc
    // This client is used for generating SFC traffic.
    List<Pe> clientPeList = Collections.nCopies(Constants.MAX_SRV_VCPU_NUM, new PeSimple(Constants.VCPU_MIPS));
    NetworkHost clientHost = new NetworkHost(
        Constants.MAX_SRV_VMEM_MB,
        Constants.SRV_BW,
        GeneratorSingleton.getStorageMb(Constants.MAX_SRV_VMEM_MB),
        clientPeList);
    clientHost.setId(-1);
    NetworkDatacenter ClientDc = new NetworkDatacenter(simulator, List.of(clientHost));
    ClientDc.setSchedulingInterval(SCHEDULING_INTERVAL);
    DatacenterBroker clientBroker = new DatacenterBrokerSimple(simulator);
    EdgeSwitch clientSwitch = new EdgeSwitch(simulator, ClientDc);
    ClientDc.addSwitch(clientSwitch);
    clientSwitch.connectHost(clientHost);
    NetworkVm clientVm = new NetworkVm(Constants.VCPU_MIPS, Constants.MAX_SRV_VCPU_NUM);
    clientVm.setId(-1);
    clientHost.createVm(clientVm);
    NetworkCloudlet clientCloudlet = new NetworkCloudlet(TASK_LENGTH, Constants.VCPU_MIPS);
    clientCloudlet.setId(-1);
    clientCloudlet
        .setUtilizationModel(new UtilizationModelFull())
        .setVm(clientVm)
        .setBroker(clientVm.getBroker());

    // 1. Setup SrvList.
    for (SRV srv : srvList) {
      NetworkHost host = new NetworkHost(
          srv.getTotVmemMb(),
          Constants.SRV_BW,
          GeneratorSingleton.getStorageMb(srv.getTotVmemMb()),
          Collections.nCopies(srv.getTotVcpuNum(), new PeSimple(Constants.VCPU_MIPS)));
      host.setId(srv.getId());
      host.setRamProvisioner(new ResourceProvisionerSimple())
          .setBwProvisioner(new ResourceProvisionerSimple())
          .setVmScheduler(new VmSchedulerTimeShared());
      PowerModelHost pm = new PowerModelHostSimple(
          GeneratorSingleton.getMaxPower(srv.getTotVcpuNum(), srv.getTotVmemMb()),
          GeneratorSingleton.getIdlePower(srv.getTotVcpuNum(), srv.getTotVmemMb()));
      host.setPowerModel(pm);
      host.enableUtilizationStats();
      srv.setHost(host);
    }

    // 2. Setup CloudSimPlus simulator and NetworkDatacenter.
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
        SFC sfc = sfcList.stream().filter(s -> s.getId() == vnf.getSfcId()).findFirst().get();
        vm.setBroker(sfc.getBroker());
        vnf.setVm(vm);
        srvList.stream().filter(srv -> srv.getId() == vnf.getSrvId()).findFirst().get().getHost().createVm(vm);

        final var cloudlet = new NetworkCloudlet(CLOUDLET_LENGTH, vnf.getReqVcpuNum());
        cloudlet.setUtilizationModel(new UtilizationModelFull());
        cloudlet.setId(vnf.getId());
        cloudlet.setVm(vm);
        cloudlet.setBroker(sfc.getBroker());
        vnf.setCloudlet(cloudlet);
      }
    }

    addExecutionTask(clientCloudlet);
    // 6. Run packet transmission simulation to each SFC.
    for (SFC sfc : sfcList) {
      // get sfc's vnf list and sort by orderInSfc
      List<VNF> vl = sfc.getVNFList(vnfList).stream().sorted(Comparator.comparingInt(VNF::getOrderInSfc)).toList();
      List<NetworkVm> vml = vl.stream().map(VNF::getVm).toList();
      List<NetworkCloudlet> cl = vl.stream().map(VNF::getCloudlet).toList();

      if (cl.size() > 0) {
        addSendTask(clientCloudlet, cl.get(0));
        addReceiveTask(cl.get(0), clientCloudlet);

        for (int i = 0; i < cl.size() - 1; i++) {
          addExecutionTask(cl.get(i));
          addSendTask(cl.get(i), cl.get(i + 1));
          addReceiveTask(cl.get(i + 1), cl.get(i));
        }

        addExecutionTask(cl.get(cl.size() - 1));
        addSendTask(cl.get(cl.size() - 1), clientCloudlet);
        addReceiveTask(clientCloudlet, cl.get(cl.size() - 1));

      }

      sfc.getBroker().submitVmList(vml);
      sfc.getBroker().submitCloudletList(cl);
    }

    clientBroker.submitVm(clientVm);
    clientBroker.submitCloudlet(clientCloudlet);

    // 7. Run simulation.
    try {
      simulator.start();

      // 8. Print results.
      lastIsSuccess = true;
      curState = state;
      state.print();
      showSimulationResults(dc, sfcList.stream().map(SFC::getBroker).toList());
      getResult().print();
    } catch (Exception e) {
      lastIsSuccess = false;
      curState = state;
      state.print();
    }
  }

  public State step(Action action) {
    int tgtSrvId = action.getSrvId();
    int srcVnfId = action.getVnfId();
    int srvNum = curState.getSrvList().size();
    if (tgtSrvId < 1 || tgtSrvId > srvNum) {
      System.out.println("Invalid Action [SRV ID is not exist]");
      lastIsSuccess = false;
      return curState.capture();
    }
    boolean unmatch = true;
    for (VNF vnf : curState.getVnfList()) {
      if (vnf.getId() == srcVnfId) {
        if (!vnf.isMovable()) {
          System.out.println("Invalid Action [VNF is not movable]");
          lastIsSuccess = false;
          return curState.capture();
        }
        vnf.setSrvId(tgtSrvId);
        unmatch = false;
      }
    }
    if (unmatch) {
      System.out.println("Invalid Action [VNF ID is not exist]");
      lastIsSuccess = false;
      return curState.capture();
    }

    setupCloudsimWithState(curState);

    return curState.capture();
  }

  public State reset() {
    setupCloudsimWithState(curState);

    return curState.capture();
  }

  public State reset(ResetArg arg) {
    curState = GeneratorSingleton.getRandomState(arg);
    initState = curState.capture();
    setupCloudsimWithState(curState);
    return curState.capture();
  }

  public Info getResult() {
    Info info = new Info();
    List<Float> powerList = new ArrayList<>();
    List<Float> cpuUtilList = new ArrayList<>();
    List<Float> memUtilList = new ArrayList<>();
    List<Integer> bwUtilList = new ArrayList<>();
    List<Boolean> sleepList = new ArrayList<>();
    int sleepNum = 0;

    for (Rack rack : curState.getRackList()) {
      for (SRV srv : rack.getSrvList()) {
        var host = srv.getHost();
        HostResourceStats cpuStats = host.getCpuUtilizationStats();
        double memUtil = host.getRam().getPercentUtilization();
        double cpuUtil = cpuStats.getMean();
        double watts = host.getPowerModel().getPower(cpuUtil);
        double bwUtil = host.getBwUtilization();

        if (srv.getVNFList(curState.getVnfList()).isEmpty() && srv.isSleepable()) {
          sleepNum++;
          watts = watts / 10;
          cpuUtil = cpuUtil / 10;
          memUtil = memUtil / 10;
          sleepList.add(true);
        } else {
          sleepList.add(false);
        }
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
    info.setSleepList(sleepList);
    info.setSleepNum(sleepNum);
    info.setSuccess(lastIsSuccess);

    Map<Integer, Float> sfcFinishedTime = new HashMap<>();
    for (VNF vnf : curState.getVnfList()) {
      int sfcId = vnf.getSfcId();
      SFC sfc = curState.getSfcList().stream().filter(s -> s.getId() == sfcId).findFirst().get();
      if (sfc.getLength() == vnf.getOrderInSfc()) {
        sfcFinishedTime.put(vnf.getSfcId(), (float) vnf.getCloudlet().getFinishTime());
      }
    }
    List<Float> latencyList = new ArrayList<>();
    float accTime = 0.0f;
    for (int i = 1; i <= curState.getSfcList().size(); i++) {
      try {
        latencyList.add(sfcFinishedTime.get(i) - accTime);
        accTime = sfcFinishedTime.get(i);
      } catch (Exception e) {
        latencyList.add(0.0f);
      }
    }
    info.setLatencyList(latencyList);

    return info;
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

  private State readRaw(String raw_state) {
    try {
      ObjectMapper om = new ObjectMapper();
      raw_state = (raw_state.replaceAll(":\"?([^{|^]*?)\"?(?=[,|}|\\]])", ":\"$1\"").replaceAll("\"?(\\w+)\"?(?=:)",
          "\"$1\""));
      State s = om.readValue(raw_state, State.class);
      return s;
    } catch (IOException e) {
      e.printStackTrace();
      System.out.print(e);
      return null;
    }
  }
}
