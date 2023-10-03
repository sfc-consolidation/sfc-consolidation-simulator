package sfc.consolidation.simulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import sfc.consolidation.simulator.generated.api.DefaultApi;
import sfc.consolidation.simulator.generated.model.Episode;
import sfc.consolidation.simulator.generated.model.Step;
import sfc.consolidation.simulator.types.Action;
import sfc.consolidation.simulator.types.State;
import sfc.consolidation.simulator.types.Info;
import sfc.consolidation.simulator.utils.ApiSingletone;
import sfc.consolidation.simulator.utils.RandomSingleton;

enum InferenceAlgorithm {
  RANDOM,
  FIRST_FIT,
  EEHVMC,
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
      case EEHVMC:
        apiAlgorithm = "eehvmc";
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
    sfc.consolidation.simulator.generated.model.State si = state.toReqForm();

    try {
      var req = api.inferenceInferencePost(si, apiAlgorithm);
      var res = req.execute().body();
      Action action = Action.fromResForm(res);
      // if vnf movable is false, return empty
      if (!state.getVnfList().get(action.getVnfId()).isMovable()) {
        return Optional.empty();
      }
      return Optional.ofNullable(action);
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
    Episode ei = new Episode();
    List<Step> siList = new ArrayList<>();
    for (int i = 0; i < actionList.size(); ++i) {
      Step si = new Step();
      si.setState(stateList.get(i).toReqForm());
      si.setAction(actionList.get(i).toReqForm());
      si.setInfo(infoList.get(i).toReqForm());
      siList.add(si);
    }
    ei.setSteps(siList);
    try {
      var req = api.saveSaveEpisodePost(ei, apiAlgorithm);
      req.execute();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
