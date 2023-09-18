package alpha.consolidation.simulator.types;

import java.util.List;

import lombok.Data;

/**
 * SFC
 * Data Type of SFC.
 */
@Data
public class SFC {
  private String id;
  private List<Integer> vnfList;
}
