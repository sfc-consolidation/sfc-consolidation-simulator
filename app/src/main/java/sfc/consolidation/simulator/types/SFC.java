package sfc.consolidation.simulator.types;

import java.util.List;

import org.cloudsimplus.brokers.DatacenterBroker;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * SFC
 * Data Type of SFC.
 */
@Data
@JsonIgnoreProperties({ "broker" })
public class SFC {
  private int id;
  private int length = 0;

  private DatacenterBroker broker;

  public List<VNF> getVNFList(List<VNF> fullVnfList) {
    return fullVnfList.stream().filter(vnf -> vnf.getSfcId() == id).toList();
  }

  public SFC capture() {
    SFC temp = new SFC();
    temp.setId(id);
    temp.setLength(length);
    return temp;
  }

  public sfc.consolidation.simulator.generated.model.SFC toReqForm() {
    var sfc = new sfc.consolidation.simulator.generated.model.SFC();
    sfc.setId(id);
    sfc.setLength(length);
    return sfc;
  }
}
