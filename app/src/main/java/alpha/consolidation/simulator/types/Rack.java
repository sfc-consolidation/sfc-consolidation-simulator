package alpha.consolidation.simulator.types;

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
}
