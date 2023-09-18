package alpha.consolidation.simulator;

import java.util.ArrayList;
import java.util.List;

import org.cloudsimplus.cloudlets.network.NetworkCloudlet;
import org.cloudsimplus.core.CloudSimPlus;
import org.cloudsimplus.hosts.network.NetworkHost;
import org.cloudsimplus.vms.network.NetworkVm;

import alpha.consolidation.simulator.types.Action;
import alpha.consolidation.simulator.types.SFC;
import alpha.consolidation.simulator.types.SRV;
import alpha.consolidation.simulator.types.State;
import alpha.consolidation.simulator.types.VNF;
import alpha.consolidation.simulator.utils.Constants;
import alpha.consolidation.simulator.utils.RandomSingleton;

public class Env {
  private final State initState;
  private State curState;

  private final CloudSimPlus simulator;
  private final List<NetworkHost> hostList;
  private final List<NetworkVm> vmList;
  private final List<NetworkCloudlet> cloudletList;

  public Env(int srvNum, int sfcNum, int vnfNum, List<SRV> srvList, List<SFC> sfcList, List<VNF> vnfList) {
    simulator = new CloudSimPlus();
    vmList = new ArrayList<>();
    cloudletList = new ArrayList<>();
    hostList = new ArrayList<>();
    initState = new State();
    if (srvList == null) {
      srvList = generate_random_srvs(srvNum);
    }
    if (sfcList == null) {
      sfcList = generate_random_sfcs(sfcNum);
    }
    if (vnfList == null) {
      vnfList = generate_random_vnfs(srvNum, sfcNum, vnfNum);
    }
    initState.setSrvList(srvList);
    initState.setSfcList(sfcList);
    initState.setVnfList(vnfList);
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
      srv.setTot_vCPU_num(
          RandomSingleton.getInstance().nextInt(Constants.MIN_SRV_VCPU_NUM, Constants.MAX_SRV_VCPU_NUM));
      srv.setUse_vCPU_num(0);
      srv.setTot_vMEM_gbt(
          RandomSingleton.getInstance().nextInt(Constants.MIN_SRV_VMEM_GBT, Constants.MAX_SRV_VMEM_GBT));
      srv.setUse_vMEM_gbt(0);
      srv.setRun_VNFs(null);
      srv.setSleepable(true);
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
      sfc.setVnfList(null);
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
  private List<VNF> generate_random_vnfs(int srvNum, int sfcNum, int vnfNum) {
    List<VNF> vnfList = new ArrayList<>();
    for (int i = 0; i < vnfNum; ++i) {
      VNF vnf = new VNF();
      vnf.setId(i);
      vnf.setSrv_id(RandomSingleton.getInstance().nextInt(srvNum));
      vnf.setSfc_id(RandomSingleton.getInstance().nextInt(sfcNum));
      vnf.setReq_vCPU_num(
          RandomSingleton.getInstance().nextInt(Constants.MIN_VNF_VCPU_NUM, Constants.MAX_VNF_VCPU_NUM));
      vnf.setReq_vMEM_gbt(
          RandomSingleton.getInstance().nextFloat(Constants.MAX_VNF_VMEM_GBT, Constants.MIN_VNF_VMEM_GBT));
      vnf.setMovable(true);
      vnfList.add(vnf);
    }
    return vnfList;
  }

  public State step(State state, Action action) {
    simulator.start();
    // TODO: Implement this.
    simulator.pause();
    return curState;
  }

  public State reset() {
    // TODO: Implement this.
    return curState;
  }
}
