package alpha.consolidation.simulator.types;

import lombok.Data;

/*
 * VNF
 * Data Type of VNF.
 */
@Data
public class VNF {
  private int id;
  private int srv_id;
  private int sfc_id;
  private int req_vCPU_num;
  private float req_vMEM_gbt;
  private boolean movable;
}
