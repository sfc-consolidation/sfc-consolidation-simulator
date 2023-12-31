package sfc.consolidation.simulator.types;

import java.util.List;

import org.cloudsimplus.network.switches.EdgeSwitch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties({ "torSwitch" })
public class Rack {
  int id;
  List<SRV> srvList;

  EdgeSwitch torSwitch; // ToR(Top of Rack) Switch

  public Rack capture() {
    Rack temp = new Rack();
    temp.setId(id);
    temp.setSrvList(srvList.stream().map(SRV::capture).toList());
    return temp;
  }

  public sfc.consolidation.simulator.generated.model.Rack toReqForm() {
    var rack = new sfc.consolidation.simulator.generated.model.Rack();
    rack.setId(id);
    rack.setSrvList(srvList.stream().map(SRV::toReqForm).toList());
    return rack;
  }
}
