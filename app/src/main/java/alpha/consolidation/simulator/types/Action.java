package alpha.consolidation.simulator.types;

import lombok.Data;

/*
 * Action
 * Data Type of Action.
 * For RL Agent.
 */
@Data
public class Action {
  private int vnfId;
  private int srvId;
}
