package sfc.consolidation.simulator.examples;

import java.lang.reflect.InvocationTargetException;

public class Main {
  static int num;

  public static void main(String[] args) {
    if (args.length == 1) {
      num = Integer.parseInt(args[0]);
    } else {
      num = 0;
    }
    String className = "sfc.consolidation.simulator.examples.Example";
    try {
      Class<?> cls = Class.forName(className + num);
      cls.getDeclaredConstructor().newInstance();
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException
        | InvocationTargetException e) {
      e.printStackTrace();
    }
  }
}
