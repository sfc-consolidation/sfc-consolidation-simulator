package alpha.consolidation.simulator.types;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Info {
  private List<Float> powerList = new ArrayList<>();
  private List<Float> cpuUtilList = new ArrayList<>();
  private List<Float> memUtilList = new ArrayList<>();
  private List<Integer> bwUtilList = new ArrayList<>();
  private int sleepNum;

  public alpha.consolidation.simulator.generated.model.Info toReqForm() {
    var info = new alpha.consolidation.simulator.generated.model.Info();
    info.setPower(powerList);
    info.setCpuUtil(cpuUtilList);
    info.setMemUtil(memUtilList);
    info.setBandwidth(bwUtilList);
    info.setSleepNum(sleepNum);
    return info;
  }

  public void print() {
    for (int i = 0; i < powerList.size(); ++i) {
      System.out.printf("[Host %d] Power: %8.1f W, CPU: %5.1f %%, MEM: %5.1f %%, BW: %d Mpbs%n", i, powerList.get(i),
          cpuUtilList.get(i) * 100,
          memUtilList.get(i) * 100, bwUtilList.get(i));
    }
  }
}
