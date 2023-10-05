package sfc.consolidation.simulator.types;

import lombok.Data;
import com.google.gson.Gson;

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

  public static Action fromResForm(Object action) {
    Action temp = new Action();
    Gson gson = new Gson();
    try {
      System.out.println(action);
      if (action != null) {
        var act = gson.fromJson(gson.toJson(action),
            sfc.consolidation.simulator.generated.model.Action.class);
        temp.setVnfId((int) Double.parseDouble(act.getVnfId().toString()));
        temp.setSrvId((int) Double.parseDouble(act.getSrvId().toString()));
        return temp;
      }
      return null;

    } catch (Exception e) {
      System.out.println(e);
      return null;
    }
  }

  public void print() {
    System.out.printf("Action: VNF: %d, SRV: %d%n", vnfId, srvId);
  }
}
