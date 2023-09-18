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
  int tot_vCPU;
  int use_vCPU;
  int tot_vMEM;
  int use_vMEM;
  Map<String, Integer> run_VNFs;
}
