package sfc.consolidation.simulator.types;

import lombok.Data;

@Data
public class ResetArg {
  private int maxRackNum;
  private int minRackNum;
  private int maxSrvNumInSingleRack;
  private int minSrvNumInSingleRack;
  private int maxVnfNum;
  private int minVnfNum;
  private int maxSfcNum;
  private int minSfcNum;
  private int maxSrvVcpuNum;
  private int minSrvVcpuNum;
  private int maxSrvVmemMb;
  private int minSrvVmemMb;
  private int maxVnfVcpuNum;
  private int minVnfVcpuNum;
  private int maxVnfVmemMb;
  private int minVnfVmemMb;
}
