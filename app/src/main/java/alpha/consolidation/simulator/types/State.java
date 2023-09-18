package alpha.consolidation.simulator.types;

import java.util.List;

import lombok.Data;

/*
 * State
 * Data Type of State.
 * For RL Agent.
 */
@Data
public class State {
  List<SRV> srvList;
  List<SFC> sfcList;
  List<VNF> vnfList;
}
