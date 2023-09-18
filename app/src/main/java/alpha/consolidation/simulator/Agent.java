package alpha.consolidation.simulator;

import java.util.Optional;

import alpha.consolidation.simulator.types.Action;
import alpha.consolidation.simulator.types.State;

public class Agent {
  public Agent() {
  }

  public Optional<Action> inference(State state) {
    return Optional.ofNullable(new Action());
  }
}
