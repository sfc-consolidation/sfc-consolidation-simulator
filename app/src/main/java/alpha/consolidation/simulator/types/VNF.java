package alpha.consolidation.simulator.types;

import org.cloudsimplus.cloudlets.network.NetworkCloudlet;
import org.cloudsimplus.vms.network.NetworkVm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/*
 * VNF
 * Data Type of VNF.
 */
@Data
@JsonIgnoreProperties({ "vm", "cloudlet" })
public class VNF {
  private int id;
  private int srvId;
  private int sfcId;
  private int orderInSfc;
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

  public alpha.consolidation.simulator.generated.model.VNF toReqForm() {
    var vnf = new alpha.consolidation.simulator.generated.model.VNF();
    vnf.setId(id);
    vnf.setSrvId(srvId);
    vnf.setSfcId(sfcId);
    vnf.setOrderInSfc(orderInSfc);
    vnf.setReqVcpuNum(reqVcpuNum);
    vnf.setReqVmemMb(reqVmemMb);
    vnf.setMovable(movable);
    return vnf;
  }
}
