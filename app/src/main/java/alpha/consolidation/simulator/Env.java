package alpha.consolidation.simulator;

import alpha.consolidation.simulator.types.Action;
import alpha.consolidation.simulator.types.State;

public class Env {
  public Env() {
  }

  public State step(State state, Action action) {
    return new State();
  }

  public State reset() {
    return new State();
  }
}
