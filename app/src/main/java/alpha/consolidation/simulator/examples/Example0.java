package alpha.consolidation.simulator.examples;

import java.util.ArrayList;
import java.util.List;

import org.cloudsimplus.brokers.DatacenterBroker;
import org.cloudsimplus.brokers.DatacenterBrokerSimple;
import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.core.CloudSimPlus;
import org.cloudsimplus.datacenters.Datacenter;
import org.cloudsimplus.datacenters.DatacenterSimple;
import org.cloudsimplus.hosts.Host;
import org.cloudsimplus.hosts.HostSimple;
import org.cloudsimplus.listeners.EventInfo;
import org.cloudsimplus.provisioners.ResourceProvisionerSimple;
import org.cloudsimplus.resources.Pe;
import org.cloudsimplus.resources.PeSimple;
import org.cloudsimplus.schedulers.cloudlet.CloudletSchedulerSpaceShared;
import org.cloudsimplus.schedulers.vm.VmSchedulerTimeShared;
import org.cloudsimplus.vms.Vm;
import org.cloudsimplus.vms.VmSimple;

// * Question
// * 1. VM의 MIPS랑 Host의 MIPS랑 같아야 하는가? 커지면 어떻게 되는가?

public class Example0 {
  private static final int SCHEDULING_INTERVAL = 1;
  private static final int HOSTS = 1;
  private static final int HOST_PES = 32;
  private static final int HOST_MIPS = 1000;
  private static final int HOST_RAM = 20000; // in Megabytes
  private static final int HOST_BW = 100000; // in Megabits/s
  private static final int HOST_STORAGE = 1000000; // in Megabytes
  private static final int VMS = 1;
  private static final int VM_MIPS = 1000;
  private static final int VM_PES = 14;
  private static final int VM_RAM = 10000; // in Megabytes
  private static final int VM_BW = 1000; // in Megabits/s
  private static final int VM_SIZE = 10000; // in Megabytes (# VM Size)
  private static final int CLOUDLETS = 10;

  private final CloudSimPlus simulation;

  private List<Host> hostList;
  private List<Vm> vmList;
  private List<Cloudlet> cloudletList;
  private final Datacenter dc;
  private final DatacenterBroker broker;
  private int vmId = 0;

  public static void main(String[] args) {
    new Example0();
  }

  public Example0() {
    hostList = new ArrayList<>(HOSTS);
    vmList = new ArrayList<>(VMS);
    cloudletList = new ArrayList<>(CLOUDLETS);

    simulation = new CloudSimPlus();
    simulation.addOnClockTickListener(this::onClockTickListener);

    for (int i = 0; i < HOSTS; ++i) {
      hostList.add(createHost());
    }

    dc = new DatacenterSimple(simulation, hostList);
    dc.setSchedulingInterval(SCHEDULING_INTERVAL);

    broker = new DatacenterBrokerSimple(simulation);
    vmList.add(createVm());

    broker.submitVmList(vmList);
    broker.submitCloudletList(cloudletList);

    simulation.start();
  }

  private Host createHost() {
    final var peList = new ArrayList<Pe>(HOST_PES);
    for (int i = 0; i < HOST_PES; ++i) {
      peList.add(new PeSimple(HOST_MIPS));
    }
    return new HostSimple(HOST_RAM, HOST_BW, HOST_STORAGE, peList)
        .setRamProvisioner(new ResourceProvisionerSimple())
        .setBwProvisioner(new ResourceProvisionerSimple())
        .setVmScheduler(new VmSchedulerTimeShared());
  }

  private Vm createVm() {
    final int id = vmId++;

    final Vm vm = new VmSimple(id, VM_MIPS, VM_PES)
        .setRam(VM_RAM)
        .setBw(VM_BW)
        .setSize(VM_SIZE)
        .setCloudletScheduler(new CloudletSchedulerSpaceShared());
    return vm;
  }

  private void onClockTickListener(EventInfo evt) {
    vmList.forEach(vm -> {
      System.out.printf(
          "\t\tTime %6.1f: Vm %d CPU Usage: %6.2f%% (%2d vCPUs. Running Cloudlets: #%02d) Upper Threshold: %.2f%n",
          evt.getTime(), vm.getId(), vm.getCpuPercentUtilization() * 100.0,
          vm.getPesNumber(),
          vm.getCloudletScheduler().getCloudletExecList().size(),
          vm.getPeVerticalScaling().getUpperThresholdFunction().apply(vm));
    });
  }

}
