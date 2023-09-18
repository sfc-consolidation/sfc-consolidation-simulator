package alpha.consolidation.simulator.types;

import java.util.Map;

import lombok.Data;

/*
 * SRV
 * Data Type of Server.
 */
@Data
public class SRV {
  int id;
  int tot_vCPU_num;
  int use_vCPU_num;
  int tot_vMEM_gbt;
  int use_vMEM_gbt;
  Map<String, Integer> run_VNFs;
  boolean sleepable;
}
