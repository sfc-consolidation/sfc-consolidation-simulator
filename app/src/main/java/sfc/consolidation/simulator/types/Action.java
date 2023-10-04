package sfc.consolidation.simulator.types;

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

  public sfc.consolidation.simulator.generated.model.Action toReqForm() {
    var action = new sfc.consolidation.simulator.generated.model.Action();
    action.setVnfId(vnfId);
    action.setSrvId(srvId);
    return action;
  }

  public static Action fromResForm(sfc.consolidation.simulator.generated.model.Action action) {
    Action temp = new Action();
    try {
      temp.setVnfId((int) Double.parseDouble(action.getVnfId().toString()));
      temp.setSrvId((int) Double.parseDouble(action.getSrvId().toString()));
      return temp;
    } catch (Exception e) {
      System.out.println(e);
      return null;
    }
  }

  public void print() {
    System.out.printf("Action: VNF: %d, SRV: %d%n", vnfId, srvId);
  }
}
