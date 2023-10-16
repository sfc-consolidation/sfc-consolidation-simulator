package sfc.consolidation.simulator.utils;

import java.util.ArrayList;
import java.util.List;

import sfc.consolidation.simulator.types.Rack;
import sfc.consolidation.simulator.types.ResetArg;
import sfc.consolidation.simulator.types.SFC;
import sfc.consolidation.simulator.types.SRV;
import sfc.consolidation.simulator.types.State;
import sfc.consolidation.simulator.types.VNF;

public class GeneratorSingleton {
  static public long getStorageMb(int vMemMb) {
    return vMemMb * (long) 100;
  }

  /*
   * getMaxPower
   * 
   * Maximum power consumption of a server is 500W + sfc (0 <= sfc <= 2500)
   * 
   * @param vCpuNum
   * 
   * @param vMemMb
   * 
   * @return max power in Watts
   */
  static public int getMaxPower(int vCpuNum, int vMemMb) {
    return (int) (500 + (2500
        * (vCpuNum / Constants.MAX_SRV_VCPU_NUM) * 0.9 + (vMemMb / Constants.MAX_SRV_VMEM_MB) * 0.1));
  }

  /*
   * getIdlePower
   * 
   * Idle power consumption of a server is 150W + sfc (0 <= sfc <= 350)
   * 
   * @param vCpuNum
   * 
   * @param vMemMb
   * 
   * @return idle power in Watts
   */
  static public int getIdlePower(int vCpuNum, int vMemMb) {
    return (int) (150 + (350
        * (vCpuNum / Constants.MAX_SRV_VCPU_NUM) * 0.9 + (vMemMb / Constants.MAX_SRV_VMEM_MB) * 0.1));
  }

  /*
   * getRandomState
   * 
   * Generate a random initial state.
   * 
   * @param minRack
   * 
   * @param maxRack
   * 
   * @param minSrv
   * 
   * @param maxSrv
   * 
   * @param minVnf
   * 
   * @param maxVnf
   * 
   * @param minSfc
   * 
   * @param maxSfc
   * 
   * @param minSrvVcpu
   * 
   * @param maxSrvVcpu
   * 
   * @param minSrvVmem
   * 
   * @param maxSrvVmem
   * 
   * @param minVnfVcpu
   * 
   * @param maxVnfVcpu
   * 
   * @param minVnfVmem
   * 
   * @param maxVnfVmem
   * 
   * @return a random state
   */
  static public State getRandomState(ResetArg resetArg) {
    int minRack = clamp(resetArg.getMinRackNum(), Constants.MIN_RACK_NUM, Constants.MAX_RACK_NUM);
    int maxRack = clamp(resetArg.getMaxRackNum(), minRack, Constants.MAX_RACK_NUM);

    int minSrv = clamp(resetArg.getMinSrvNumInSingleRack(), Constants.MIN_SRV_NUM_IN_SINGLE_RACK,
        Constants.MAX_SRV_NUM_IN_SINGLE_RACK);
    int maxSrv = clamp(resetArg.getMaxSrvNumInSingleRack(), minSrv, Constants.MAX_SRV_NUM_IN_SINGLE_RACK);

    int minVnf = clamp(resetArg.getMinVnfNum(), Constants.MIN_VNF_NUM, Constants.MAX_VNF_NUM);
    int maxVnf = clamp(resetArg.getMaxVnfNum(), minVnf, Constants.MAX_VNF_NUM);

    int minSfc = clamp(resetArg.getMinSfcNum(), Constants.MIN_SFC_NUM, Constants.MAX_SFC_NUM);
    int maxSfc = clamp(resetArg.getMaxSfcNum(), minSfc, Constants.MAX_SFC_NUM);

    int minSrvVcpu = clamp(resetArg.getMinSrvVcpuNum(), Constants.MIN_SRV_VCPU_NUM, Constants.MAX_SRV_VCPU_NUM);
    int maxSrvVcpu = clamp(resetArg.getMaxSrvVcpuNum(), minSrvVcpu, Constants.MAX_SRV_VCPU_NUM);

    int minSrvVmem = clamp(resetArg.getMinSrvVmemMb(), Constants.MIN_SRV_VMEM_MB, Constants.MAX_SRV_VMEM_MB);
    int maxSrvVmem = clamp(resetArg.getMaxSrvVmemMb(), minSrvVmem, Constants.MAX_SRV_VMEM_MB);

    int minVnfVcpu = clamp(resetArg.getMinVnfVcpuNum(), Constants.MIN_VNF_VCPU_NUM, Constants.MAX_VNF_VCPU_NUM);
    int maxVnfVcpu = clamp(resetArg.getMaxVnfVcpuNum(), minVnfVcpu, Constants.MAX_VNF_VCPU_NUM);

    int minVnfVmem = clamp(resetArg.getMinVnfVmemMb(), Constants.MIN_VNF_VMEM_MB, Constants.MAX_VNF_VMEM_MB);
    int maxVnfVmem = clamp(resetArg.getMaxVnfVmemMb(), minVnfVmem, Constants.MAX_VNF_VMEM_MB);

    int rackNum = RandomSingleton.getInstance().nextInt(minRack, maxRack + 1);
    int vnfNum = RandomSingleton.getInstance().nextInt(minVnf, maxVnf + 1);
    int sfcNum = RandomSingleton.getInstance().nextInt(minSfc, maxSfc + 1);
    int totSrvNum = 0;

    List<Rack> rackList = new ArrayList<>();
    for (int i = 1; i <= rackNum; ++i) {
      List<SRV> srvList = new ArrayList<>();
      int srvNum = RandomSingleton.getInstance().nextInt(minSrv, maxSrv + 1);
      for (int j = 1; j <= srvNum; ++j) {
        SRV srv = new SRV();
        srv.setId(++totSrvNum);
        srv.setTotVcpuNum(RandomSingleton.getInstance().nextInt(minSrvVcpu, maxSrvVcpu + 1));
        srv.setTotVmemMb(RandomSingleton.getInstance().nextInt(minSrvVmem, maxSrvVmem + 1));
        srv.setSleepable(RandomSingleton.getInstance().nextFloat() < Constants.SRV_SLEEPABLE_PROB);
        srvList.add(srv);
      }
      Rack rack = new Rack();
      rack.setId(i);
      rack.setSrvList(srvList);
      rackList.add(rack);
    }
    List<SFC> sfcList = new ArrayList<>();
    for (int i = 1; i <= sfcNum; ++i) {
      SFC sfc = new SFC();
      sfc.setId(i);
      sfcList.add(sfc);
    }
    List<VNF> vnfList = new ArrayList<>();
    for (int i = 1; i <= vnfNum; ++i) {
      VNF vnf = new VNF();
      vnf.setId(i);
      vnf.setReqVcpuNum(RandomSingleton.getInstance().nextInt(minVnfVcpu, maxVnfVcpu + 1));
      vnf.setReqVmemMb(RandomSingleton.getInstance().nextInt(minVnfVmem, maxVnfVmem + 1));
      vnf.setMovable(RandomSingleton.getInstance().nextFloat() < Constants.VNF_MOVABLE_PROB);
      var sfcId = RandomSingleton.getInstance().nextInt(1, sfcNum + 1);
      vnf.setSfcId(sfcId);

      for (int j = 0; j < sfcList.size(); ++j) {
        SFC sfc = sfcList.get(j);
        if (sfc.getId() == sfcId) {
          sfc.setLength(sfc.getLength() + 1);
          vnf.setOrderInSfc(sfc.getLength());
          break;
        }
      }

      vnf.setSrvId(RandomSingleton.getInstance().nextInt(1, totSrvNum + 1));
      vnfList.add(vnf);
    }
    State s = new State();
    s.setRackList(rackList);
    s.setVnfList(vnfList);
    s.setSfcList(sfcList);
    return s;
  }

  private static int clamp(int val, int min, int max) {
    return Math.max(Math.min(val, max), min);
  }
}
