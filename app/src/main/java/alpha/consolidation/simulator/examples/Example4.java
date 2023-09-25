package alpha.consolidation.simulator.examples;

import java.util.ArrayList;

import alpha.consolidation.simulator.generated.api.DefaultApi;
import alpha.consolidation.simulator.generated.model.Action;
import alpha.consolidation.simulator.generated.model.StateInput;
import alpha.consolidation.simulator.utils.ApiSingletone;
import retrofit2.Call;

public class Example4 {
  public static void main(String[] args) {
    new Example4();
  }

  public Example4() {
    DefaultApi api = ApiSingletone.getInstance();
    StateInput stateInput = new StateInput();
    stateInput.setRackList(new ArrayList<>());
    stateInput.setSfcList(new ArrayList<>());
    stateInput.setVnfList(new ArrayList<>());
    Call<Action> actionRequest = api.inferenceInferencePost(stateInput, "dqn");
    try {
      Action response = actionRequest.execute().body();
      System.out.println(response);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
