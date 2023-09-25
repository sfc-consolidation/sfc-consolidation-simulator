package alpha.consolidation.simulator.types;

import java.util.List;

import org.cloudsimplus.hosts.network.NetworkHost;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/*
 * SRV
 * Data Type of Server.
 */
@Data
@JsonIgnoreProperties({ "host" })
public class SRV {
  private int id;
  private int totVcpuNum;
  private int totVmemMb;
  private boolean sleepable;

  private NetworkHost host;

  public List<VNF> getVNFList(List<VNF> fullVnfList) {
    return fullVnfList.stream().filter(vnf -> vnf.getSrvId() == id).toList();
  }

  public SRV capture() {
    SRV temp = new SRV();
    temp.setId(id);
    temp.setTotVcpuNum(totVcpuNum);
    temp.setTotVmemMb(totVmemMb);
    temp.setSleepable(sleepable);
    return temp;
  }

  public alpha.consolidation.simulator.generated.model.SRV toReqForm() {
    var srv = new alpha.consolidation.simulator.generated.model.SRV();
    srv.setId(id);
    srv.setTotVcpuNum(totVcpuNum);
    srv.setTotVmemMb(totVmemMb);
    srv.setSleepable(sleepable);
    return srv;
  }
}
