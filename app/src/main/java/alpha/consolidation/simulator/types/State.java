package alpha.consolidation.simulator.types;

import java.util.List;

import lombok.Data;

/*
 * State
 * Data Type of State.
 * For RL Agent.
 */
@Data
public class State {
  private List<SRV> srvList;
  private List<SFC> sfcList;
  private List<VNF> vnfList;

  public State capture() {
    State temp = new State();
    temp.setSrvList(srvList.stream().map(SRV::capture).toList());
    temp.setSfcList(sfcList.stream().map(SFC::capture).toList());
    temp.setVnfList(vnfList.stream().map(VNF::capture).toList());
    return temp;
  }
}
