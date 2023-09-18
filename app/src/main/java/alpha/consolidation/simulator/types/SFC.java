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
  private List<Integer> vnfList;
}
