package sfc.consolidation.simulator.types;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/*
 * State
 * Data Type of State.
 * For RL Agent.
 */
@Data
@JsonIgnoreProperties({ "srvList", "info" })
public class State {
  private List<Rack> rackList;
  private List<SFC> sfcList;
  private List<VNF> vnfList;

  public State capture() {
    State temp = new State();
    temp.setRackList(rackList.stream().map(Rack::capture).toList());
    temp.setSfcList(sfcList.stream().map(SFC::capture).toList());
    temp.setVnfList(vnfList.stream().map(VNF::capture).toList());
    return temp;
  }

  public List<SRV> getSrvList() {
    return rackList.stream().flatMap(rack -> rack.getSrvList().stream()).toList();
  }

  public void print() {
    List<SRV> srvList = getSrvList();
    System.out.printf("Rack: %d, SRV: %d, SFC: %d, VNF: %d%n", rackList.size(), srvList.size(), sfcList.size(),
        vnfList.size());
    for (SRV srv : srvList) {
      System.out.printf("SRV: %s%n", srv.toString());
    }
    for (VNF vnf : vnfList) {
      System.out.printf("VNF: %s%n", vnf.toString());
    }
  }

  public sfc.consolidation.simulator.generated.model.StateInput toReqForm() {
    var state = new sfc.consolidation.simulator.generated.model.StateInput();
    state.setRackList(rackList.stream().map(Rack::toReqForm).toList());
    state.setSfcList(sfcList.stream().map(SFC::toReqForm).toList());
    state.setVnfList(vnfList.stream().map(VNF::toReqForm).toList());
    return state;
  }
}
