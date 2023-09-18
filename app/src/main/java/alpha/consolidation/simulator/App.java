/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package alpha.consolidation.simulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import alpha.consolidation.simulator.types.Action;
import alpha.consolidation.simulator.types.State;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(description = "Simulate a VNF Consolidation Scenario", mixinStandardHelpOptions = true, version = "0.0.1")
public class App implements Runnable {
  @Option(names = { "-s", "--srv_n" }, description = "Number of servers", defaultValue = "0")
  static int srvNum;
  @Option(names = { "-v", "--vnf_n" }, description = "Number of VNFs", defaultValue = "0")
  static int vnfNum;
  @Option(names = { "-f", "--sfc_n" }, description = "Number of SFCs", defaultValue = "0")
  static int sfcNum;
  @Option(names = { "-d", "--debug" }, arity = "0..1", description = "Debug mode", defaultValue = "false")
  static boolean debug;
  @Option(names = { "-u",
      "--upload" }, arity = "0..1", description = "Upload results to remote DB", defaultValue = "false")
  static boolean upload;

  static final long seed = System.currentTimeMillis();
  static final Random rand = new Random(seed);

  static final int MAX_SRV_NUM = 100;
  static final int MAX_VNF_NUM = 200;
  static final int MAX_SFC_NUM = 100;
  static final int MAX_EPISODE_LEN = 100;

  public void run() {
    if (srvNum == 0)
      srvNum = rand.nextInt(MAX_SRV_NUM + 1);
    if (vnfNum == 0)
      vnfNum = rand.nextInt(MAX_VNF_NUM + 1);
    if (sfcNum == 0)
      sfcNum = rand.nextInt(MAX_SFC_NUM + 1);

    List<State> states = new ArrayList<>();
    List<Action> actions = new ArrayList<>();

    Env env = new Env();
    State state = env.reset();
    Agent agent = new Agent();

    // Run Simulation.
    for (int i = 1; i < MAX_EPISODE_LEN; ++i) {
      Optional<Action> action = agent.inference(state);
      if (!action.isPresent())
        break;
      states.add(state);
      actions.add(action.get());
      state = env.step(state, action.get());
    }
    states.add(state);

    // Print Debug Info.
    if (debug) {
      for (int i = 0; i < actions.size(); ++i) {
        System.out.printf(
            "Episode: %d, State: %s, Action: %s%n",
            i + 1,
            states.get(i).toString(),
            actions.get(i).toString());
      }
      System.out.printf("Init State: %s%n", states.get(0).toString());
      System.out.printf("Final State: %s%n", state.toString());
    }

    // Upload results to remote DB.
    if (upload) {
      System.out.println("Saving results to remote DB...");
      // TODO: Implement this. (Make JSON format)
    }
  }

  public static void main(String[] args) {
    new CommandLine(new App()).execute(args);
  }
}