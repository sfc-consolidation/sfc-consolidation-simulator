package alpha.consolidation.simulator;

import java.util.Optional;

import alpha.consolidation.simulator.types.Action;
import alpha.consolidation.simulator.types.State;
import alpha.consolidation.simulator.utils.RandomSingleton;

enum InferenceAlgorithm {
  RANDOM,
  BEST_FIT,
  FIRST_FIT,
  DQN,
  PPO
}

public class Agent {

  public Agent() {
  }

  public Optional<Action> inference(State state, InferenceAlgorithm method) {
    if (method == InferenceAlgorithm.RANDOM) {
      return inferenceRandom(state);
    } else if (method == InferenceAlgorithm.BEST_FIT) {
      // TODO: implement
      return Optional.empty();
    } else if (method == InferenceAlgorithm.FIRST_FIT) {
      // TODO: implement
      return Optional.empty();
    } else if (method == InferenceAlgorithm.DQN) {
      // TODO: implement
      return Optional.empty();
    } else if (method == InferenceAlgorithm.PPO) {
      // TODO: implement
      return Optional.empty();
    } else {
      return Optional.empty();
    }
  }

  public Optional<Action> inferenceRandom(State state) {
    Action action = new Action();
    int vnfNum = state.getVnfList().size();
    int srvNum = (int) state.getRackList().stream().flatMap(rack -> rack.getSrvList().stream()).count();
    action.setVnfId(RandomSingleton.getInstance().nextInt(vnfNum));
    action.setSrvId(RandomSingleton.getInstance().nextInt(srvNum));

    return Optional.ofNullable(action);
  }
}
