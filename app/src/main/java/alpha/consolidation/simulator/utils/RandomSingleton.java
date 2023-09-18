package alpha.consolidation.simulator.utils;

import java.util.Random;

public class RandomSingleton {
  private static final long SEED = System.currentTimeMillis();
  private static final Random INSTANCE = new Random(SEED);

  private RandomSingleton() {
  }

  public static Random getInstance() {
    return INSTANCE;
  }
}
