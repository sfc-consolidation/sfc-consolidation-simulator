package alpha.consolidation.simulator.utils;

import java.util.ArrayList;
import java.util.List;

import alpha.consolidation.simulator.types.Rack;
import alpha.consolidation.simulator.types.SFC;
import alpha.consolidation.simulator.types.SRV;
import alpha.consolidation.simulator.types.State;
import alpha.consolidation.simulator.types.VNF;

public class GeneratorSingleton {
  static public long getStorageMb(int vMemMb) {
    return vMemMb * (long) 100;
  }

  /*
   * getMaxPower
   * 
   * Maximum power consumption of a server is 500W + alpha (0 <= alpha <= 2500)
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
   * Idle power consumption of a server is 150W + alpha (0 <= alpha <= 350)
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
  static public State getRandomState(
      int minRack, int maxRack,
      int minSrv, int maxSrv,
      int minVnf, int maxVnf,
      int minSfc, int maxSfc,
      int minSrvVcpu, int maxSrvVcpu,
      int minSrvVmem, int maxSrvVmem,
      int minVnfVcpu, int maxVnfVcpu,
      int minVnfVmem, int maxVnfVmem) {

    minRack = clamp(minRack, Constants.MIN_RACK_NUM, Constants.MAX_RACK_NUM);
    maxRack = clamp(maxRack, minRack, Constants.MAX_RACK_NUM);

    minSrv = clamp(minSrv, Constants.MIN_SRV_NUM_IN_SINGLE_RACK, Constants.MAX_SRV_NUM_IN_SINGLE_RACK);
    maxSrv = clamp(maxSrv, minSrv, Constants.MAX_SRV_NUM_IN_SINGLE_RACK);

    minVnf = clamp(minVnf, Constants.MIN_VNF_NUM, Constants.MAX_VNF_NUM);
    maxVnf = clamp(maxVnf, minVnf, Constants.MAX_VNF_NUM);

    minSfc = clamp(minSfc, Constants.MIN_SFC_NUM, Constants.MAX_SFC_NUM);
    maxSfc = clamp(maxSfc, minSfc, Constants.MAX_SFC_NUM);

    minSrvVcpu = clamp(minSrvVcpu, Constants.MIN_SRV_VCPU_NUM, Constants.MAX_SRV_VCPU_NUM);
    maxSrvVcpu = clamp(maxSrvVcpu, minSrvVcpu, Constants.MAX_SRV_VCPU_NUM);

    minSrvVmem = clamp(minSrvVmem, Constants.MIN_SRV_VMEM_MB, Constants.MAX_SRV_VMEM_MB);
    maxSrvVmem = clamp(maxSrvVmem, minSrvVmem, Constants.MAX_SRV_VMEM_MB);

    minVnfVcpu = clamp(minVnfVcpu, Constants.MIN_VNF_VCPU_NUM, Constants.MAX_VNF_VCPU_NUM);
    maxVnfVcpu = clamp(maxVnfVcpu, minVnfVcpu, Constants.MAX_VNF_VCPU_NUM);

    minVnfVmem = clamp(minVnfVmem, Constants.MIN_VNF_VMEM_MB, Constants.MAX_VNF_VMEM_MB);
    maxVnfVmem = clamp(maxVnfVmem, minVnfVmem, Constants.MAX_VNF_VMEM_MB);

    int rackNum = RandomSingleton.getInstance().nextInt(minRack, maxRack + 1);
    int vnfNum = RandomSingleton.getInstance().nextInt(minVnf, maxVnf + 1);
    int sfcNum = RandomSingleton.getInstance().nextInt(minSfc, maxSfc + 1);
    int totSrvNum = 0;

    List<Rack> rackList = new ArrayList<>();
    for (int i = 0; i < rackNum; ++i) {
      List<SRV> srvList = new ArrayList<>();
      int srvNum = RandomSingleton.getInstance().nextInt(minSrv, maxSrv + 1);
      for (int j = 0; j < srvNum; ++j) {
        SRV srv = new SRV();
        srv.setId(totSrvNum);
        srv.setTotVcpuNum(RandomSingleton.getInstance().nextInt(minSrvVcpu, maxSrvVcpu + 1));
        srv.setTotVmemMb(RandomSingleton.getInstance().nextInt(minSrvVmem, maxSrvVmem + 1));
        srv.setSleepable(RandomSingleton.getInstance().nextFloat() < Constants.SRV_SLEEPABLE_PROB);
        srvList.add(srv);
        totSrvNum++;
      }
      Rack rack = new Rack();
      rack.setId(i);
      rack.setSrvList(srvList);
      rackList.add(rack);
    }
    List<SFC> sfcList = new ArrayList<>();
    for (int i = 0; i < sfcNum; ++i) {
      SFC sfc = new SFC();
      sfc.setId(i);
      sfcList.add(sfc);
    }
    // TODO: vnf의 분배가 불가능한 경우 어쩔 것인가?
    List<VNF> vnfList = new ArrayList<>();
    for (int i = 0; i < vnfNum; ++i) {
      VNF vnf = new VNF();
      vnf.setId(i);
      vnf.setReqVcpuNum(RandomSingleton.getInstance().nextInt(minVnfVcpu, maxVnfVcpu + 1));
      vnf.setReqVmemMb(RandomSingleton.getInstance().nextInt(minVnfVmem, maxVnfVmem + 1));
      vnf.setMovable(RandomSingleton.getInstance().nextFloat() < Constants.VNF_MOVABLE_PROB);
      var sfcId = RandomSingleton.getInstance().nextInt(0, sfcNum);
      vnf.setSfcId(sfcId);
      vnf.setOrderInSfc(sfcList.get(sfcId).getLength());
      sfcList.get(sfcId).setLength(sfcList.get(sfcId).getLength() + 1);
      vnf.setSrvId(RandomSingleton.getInstance().nextInt(0, totSrvNum));
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
