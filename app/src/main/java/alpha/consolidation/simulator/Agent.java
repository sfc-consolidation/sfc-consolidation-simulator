package alpha.consolidation.simulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import alpha.consolidation.simulator.generated.api.DefaultApi;
import alpha.consolidation.simulator.generated.model.EpisodeInput;
import alpha.consolidation.simulator.generated.model.StateInput;
import alpha.consolidation.simulator.generated.model.StepInput;
import alpha.consolidation.simulator.types.Action;
import alpha.consolidation.simulator.types.State;
import alpha.consolidation.simulator.types.Info;
import alpha.consolidation.simulator.utils.ApiSingletone;
import alpha.consolidation.simulator.utils.RandomSingleton;

enum InferenceAlgorithm {
  RANDOM,
  BEST_FIT,
  FIRST_FIT,
  DQN,
  PPO
}

public class Agent {
  final String apiAlgorithm;

  public Agent(InferenceAlgorithm method) {
    switch (method) {
      case RANDOM:
        apiAlgorithm = "random";
        break;
      case BEST_FIT:
        apiAlgorithm = "bf";
        break;
      case FIRST_FIT:
        apiAlgorithm = "ff";
        break;
      case DQN:
        apiAlgorithm = "dqn";
        break;
      case PPO:
        apiAlgorithm = "ppo";
        break;
      default:
        apiAlgorithm = "inner_random";
        break;
    }
  }

  public Optional<Action> inference(State state) {
    if (apiAlgorithm == "inner_random")
      return inferenceRandom(state);

    DefaultApi api = ApiSingletone.getInstance();
    StateInput si = state.toReqForm();

    try {
      var req = api.inferenceInferencePost(si, apiAlgorithm);
      var res = req.execute().body();
      return Optional.ofNullable(Action.fromResForm(res));
    } catch (Exception e) {
      e.printStackTrace();
      return Optional.empty();
    }
  }

  private Optional<Action> inferenceRandom(State state) {
    Action action = new Action();
    int vnfNum = state.getVnfList().size();
    int srvNum = (int) state.getRackList().stream().flatMap(rack -> rack.getSrvList().stream()).count();
    action.setVnfId(RandomSingleton.getInstance().nextInt(vnfNum));
    action.setSrvId(RandomSingleton.getInstance().nextInt(srvNum));

    return Optional.ofNullable(action);
  }

  public void submit(List<State> stateList, List<Action> actionList, List<Info> infoList) {
    DefaultApi api = ApiSingletone.getInstance();
    EpisodeInput ei = new EpisodeInput();
    List<StepInput> siList = new ArrayList<>();
    for (int i = 0; i < actionList.size(); ++i) {
      StepInput si = new StepInput();
      si.setState(stateList.get(i).toReqForm());
      si.setAction(actionList.get(i).toReqForm());
      si.setInfo(infoList.get(i).toReqForm());
      siList.add(si);
    }
    ei.setSteps(siList);
    System.out.println(ei);
    try {
      var req = api.saveSaveEpisodePost(ei, apiAlgorithm);
      var res = req.execute();
      System.out.println(res.body());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
