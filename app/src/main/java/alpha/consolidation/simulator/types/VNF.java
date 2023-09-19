package alpha.consolidation.simulator.types;

import org.cloudsimplus.cloudlets.network.NetworkCloudlet;
import org.cloudsimplus.vms.network.NetworkVm;

import lombok.Data;

/*
 * VNF
 * Data Type of VNF.
 */
@Data
public class VNF {
  private int id;
  private int srvId;
  private int sfcId;
  private int reqVcpuNum;
  private int reqVmemMb;
  private boolean movable;

  // for CloudSimPlus
  private NetworkVm vm;
  private NetworkCloudlet cloudlet;

  public VNF capture() {
    VNF temp = new VNF();
    temp.setId(id);
    temp.setSrvId(srvId);
    temp.setSfcId(sfcId);
    temp.setReqVcpuNum(reqVcpuNum);
    temp.setReqVmemMb(reqVmemMb);
    temp.setMovable(movable);
    return temp;
  }
}
