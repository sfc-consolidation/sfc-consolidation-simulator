package sfc.consolidation.simulator.utils;

import sfc.consolidation.simulator.Env;

public class EnvSingletone {
  private final static Env env = new Env(null, null);

  public static Env getInstance() {
    return env;
  }
}
