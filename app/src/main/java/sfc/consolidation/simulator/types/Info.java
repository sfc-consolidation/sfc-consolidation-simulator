package sfc.consolidation.simulator.types;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Info {
  private List<Float> powerList = new ArrayList<>();
  private List<Float> cpuUtilList = new ArrayList<>();
  private List<Float> memUtilList = new ArrayList<>();
  private List<Integer> bwUtilList = new ArrayList<>();
  private List<Boolean> sleepList = new ArrayList<>();
  private boolean success;
  private int sleepNum;

  public sfc.consolidation.simulator.generated.model.Info toReqForm() {
    var info = new sfc.consolidation.simulator.generated.model.Info();
    info.setPower(powerList);
    info.setCpuUtil(cpuUtilList);
    info.setMemUtil(memUtilList);
    info.setBandwidth(bwUtilList);
    info.setSleep(sleepList);
    info.setSleepNum(sleepNum);
    info.setIsSuccess(success);
    return info;
  }

  public void print() {
    if (!success) {
      System.out.println("Failed to organize State.");
      return;
    }
    for (int i = 0; i < powerList.size(); ++i) {
      System.out.printf("[Host %d] #Sleep: %s, Power: %8.1f W, CPU: %5.1f %%, MEM: %5.1f %%, BW: %10d Mpbs%n",
          i + 1,
          sleepList.get(i) ? "💤" : "🌞",
          powerList.get(i),
          cpuUtilList.get(i) * 100,
          memUtilList.get(i) * 100,
          bwUtilList.get(i));
    }
  }
}
