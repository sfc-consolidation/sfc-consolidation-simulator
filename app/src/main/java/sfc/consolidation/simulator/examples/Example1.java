package sfc.consolidation.simulator.examples;

import java.util.ArrayList;
import java.util.List;

import org.cloudsimplus.brokers.DatacenterBroker;
import org.cloudsimplus.brokers.DatacenterBrokerSimple;
import org.cloudsimplus.builders.tables.CloudletsTableBuilder;
import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.cloudlets.CloudletSimple;
import org.cloudsimplus.core.CloudSimPlus;
import org.cloudsimplus.datacenters.Datacenter;
import org.cloudsimplus.datacenters.DatacenterSimple;
import org.cloudsimplus.hosts.Host;
import org.cloudsimplus.hosts.HostSimple;
import org.cloudsimplus.network.topologies.BriteNetworkTopology;
import org.cloudsimplus.resources.Pe;
import org.cloudsimplus.resources.PeSimple;
import org.cloudsimplus.schedulers.cloudlet.CloudletSchedulerTimeShared;
import org.cloudsimplus.utilizationmodels.UtilizationModelFull;
import org.cloudsimplus.vms.Vm;
import org.cloudsimplus.vms.VmSimple;

public class Example1 {
  private static final String NETWORK_TOPOLOGY_FILE = "topology.brite";
  private static final int VM_PES = 1;
  private final Datacenter dc;
  private final DatacenterBroker broker;

  private final List<Cloudlet> cloudletList;
  private final List<Vm> vmList;
  private final CloudSimPlus simulation;

  public static void main(String[] args) {
    new Example1();
  }

  public Example1() {
    System.out.println("Starting" + getClass().getSimpleName() + "...");

    vmList = new ArrayList<>();
    cloudletList = new ArrayList<>();
    simulation = new CloudSimPlus();

    final var hostList = new ArrayList<Host>();
    final var peList = new ArrayList<Pe>();

    final long mips = 1000;
    peList.add(new PeSimple(mips));

    final long ram = 2048; // in Megabytes
    final long storage = 1000000; // in Megabytes
    final long bw = 10000; // in Megabits/s

    final var host = new HostSimple(ram, bw, storage, peList);
    hostList.add(host);

    dc = new DatacenterSimple(simulation, hostList);
    broker = new DatacenterBrokerSimple(simulation);

    // <-- Network Topology Configuration
    final var networkTopology = BriteNetworkTopology.getInstance(NETWORK_TOPOLOGY_FILE);
    simulation.setNetworkTopology(networkTopology);

    networkTopology.mapNode(dc, 0);
    networkTopology.mapNode(broker, 3);
    // Network Topology Configuration -->

    // <-- VM Configuration
    final Vm vm = new VmSimple(250, VM_PES)
        .setRam(512)
        .setBw(1000)
        .setSize(10000)
        .setCloudletScheduler(new CloudletSchedulerTimeShared());
    vmList.add(vm);
    broker.submitVmList(vmList);
    // VM Configuration -->

    // <-- Cloudlet Configuration
    final var cloudlet = new CloudletSimple(40000, VM_PES)
        .setFileSize(300)
        .setOutputSize(300)
        .setUtilizationModel(new UtilizationModelFull());
    cloudletList.add(cloudlet);
    broker.submitCloudletList(cloudletList);
    // Cloudlet Configuration -->

    simulation.start();

    new CloudletsTableBuilder(broker.getCloudletFinishedList()).build();
    System.out.println("Finished " + getClass().getSimpleName() + ".");
  }
}
