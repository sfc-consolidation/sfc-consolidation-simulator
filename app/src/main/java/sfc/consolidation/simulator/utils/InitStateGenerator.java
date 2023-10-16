package sfc.consolidation.simulator.utils;

import java.io.File;
import java.io.IOException;

import picocli.CommandLine;
import picocli.CommandLine.Option;
import picocli.CommandLine.Command;
import com.fasterxml.jackson.databind.ObjectMapper;

import sfc.consolidation.simulator.types.ResetArg;
import sfc.consolidation.simulator.types.State;

@Command(description = "Generate a topology file", mixinStandardHelpOptions = true, version = "0.0.1")
public class InitStateGenerator implements Runnable {
  @Option(names = {
      "--min-rack" }, description = "Minimum number of racks", required = true, defaultValue = Constants.MIN_RACK_NUM
          + "")
  static int minRack;
  @Option(names = {
      "--max-rack" }, description = "Maximum number of racks", required = true, defaultValue = Constants.MAX_RACK_NUM
          + "")
  static int maxRack;
  @Option(names = {
      "--min-srv-single-rack" }, description = "Minimum number of servers in a rack", required = true, defaultValue = Constants.MIN_SRV_NUM_IN_SINGLE_RACK
          + "")
  static int minSrv;
  @Option(names = {
      "--max-srv-single-rack" }, description = "Maximum number of servers in a rack", required = true, defaultValue = Constants.MAX_SRV_NUM_IN_SINGLE_RACK
          + "")
  static int maxSrv;
  @Option(names = {
      "--min-vnf" }, description = "Minimum number of VNFs", required = true, defaultValue = Constants.MIN_VNF_NUM + "")
  static int minVnf;
  @Option(names = {
      "--max-vnf" }, description = "Maximum number of VNFs", required = true, defaultValue = Constants.MAX_VNF_NUM + "")
  static int maxVnf;
  @Option(names = {
      "--min-sfc" }, description = "Minimum number of SFCs", required = true, defaultValue = Constants.MIN_SFC_NUM + "")
  static int minSfc;
  @Option(names = {
      "--max-sfc" }, description = "Maximum number of SFCs", required = true, defaultValue = Constants.MAX_SFC_NUM + "")
  static int maxSfc;
  @Option(names = {
      "--min-srv-vcpu" }, description = "Minimum number of vCPUs in a server", required = true, defaultValue = Constants.MIN_SRV_VCPU_NUM
          + "")
  static int minSrvVcpu;
  @Option(names = {
      "--max-srv-vcpu" }, description = "Maximum number of vCPUs in a server", required = true, defaultValue = Constants.MAX_SRV_VCPU_NUM
          + "")
  static int maxSrvVcpu;
  @Option(names = {
      "--min-srv-vmem" }, description = "Minimum amount of vMem(MB) in a server", required = true, defaultValue = Constants.MIN_SRV_VMEM_MB
          + "")
  static int minSrvVmem;
  @Option(names = {
      "--max-srv-vmem" }, description = "Maximum amount of vMem(MB) in a server", required = true, defaultValue = Constants.MAX_SRV_VMEM_MB
          + "")
  static int maxSrvVmem;
  @Option(names = {
      "--min-vnf-vcpu" }, description = "Minimum number of vCPUs in a VNF", required = true, defaultValue = Constants.MIN_VNF_VCPU_NUM
          + "")
  static int minVnfVcpu;
  @Option(names = {
      "--max-vnf-vcpu" }, description = "Maximum number of vCPUs in a VNF", required = true, defaultValue = Constants.MAX_VNF_VCPU_NUM
          + "")
  static int maxVnfVcpu;
  @Option(names = {
      "--min-vnf-vmem" }, description = "Minimum amount of vMem(MB) in a VNF", required = true, defaultValue = Constants.MIN_VNF_VMEM_MB
          + "")
  static int minVnfVmem;
  @Option(names = {
      "--max-vnf-vmem" }, description = "Maximum amount of vMem(MB) in a VNF", required = true, defaultValue = Constants.MAX_VNF_VMEM_MB
          + "")
  static int maxVnfVmem;

  public void run() {
    ResetArg arg = new ResetArg();
    {
      arg.setMinRackNum(minRack);
      arg.setMaxRackNum(maxRack);
      arg.setMinSrvNumInSingleRack(minSrv);
      arg.setMaxSrvNumInSingleRack(maxSrv);
      arg.setMinVnfNum(minVnf);
      arg.setMaxVnfNum(maxVnf);
      arg.setMinSfcNum(minSfc);
      arg.setMaxSfcNum(maxSfc);
      arg.setMinSrvVcpuNum(minSrvVcpu);
      arg.setMaxSrvVcpuNum(maxSrvVcpu);
      arg.setMinSrvVmemMb(minSrvVmem);
      arg.setMaxSrvVmemMb(maxSrvVmem);
      arg.setMinVnfVcpuNum(minVnfVcpu);
      arg.setMaxVnfVcpuNum(maxVnfVcpu);
      arg.setMinVnfVmemMb(minVnfVmem);
      arg.setMaxVnfVmemMb(maxVnfVmem);
    }

    State topo = GeneratorSingleton.getRandomState(arg);

    // save topo map object to resources directory using jackson library
    ObjectMapper om = new ObjectMapper();
    try {
      String jsonString = om.writeValueAsString(topo);
      System.out.println(jsonString);
      om.writeValue(new File("src/main/resources/topology.json"), topo);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    new CommandLine(new InitStateGenerator()).execute(args);
  }
}
