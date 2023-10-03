package sfc.consolidation.simulator.examples;

import java.util.ArrayList;

import sfc.consolidation.simulator.generated.api.DefaultApi;
import sfc.consolidation.simulator.generated.model.Action;
import sfc.consolidation.simulator.generated.model.State;
import sfc.consolidation.simulator.utils.ApiSingletone;
import retrofit2.Call;

public class Example4 {
  public static void main(String[] args) {
    new Example4();
  }

  public Example4() {
    DefaultApi api = ApiSingletone.getInstance();
    State state = new State();
    state.setRackList(new ArrayList<>());
    state.setSfcList(new ArrayList<>());
    state.setVnfList(new ArrayList<>());
    Call<Action> actionRequest = api.inferenceInferencePost(state, "dqn");
    try {
      Action response = actionRequest.execute().body();
      System.out.println(response);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
