package alpha.consolidation.simulator.types;

import java.util.List;

import lombok.Data;

/**
 * SFC
 * Data Type of SFC.
 */
@Data
public class SFC {
  private int id;

  public List<VNF> getVNFList(List<VNF> fullVnfList) {
    return fullVnfList.stream().filter(vnf -> vnf.getSfcId() == id).toList();
  }

  public SFC capture() {
    SFC temp = new SFC();
    temp.setId(id);
    return temp;
  }
}
