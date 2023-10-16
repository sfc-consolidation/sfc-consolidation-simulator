package sfc.consolidation.simulator.dtos;

import lombok.Data;
import sfc.consolidation.simulator.types.Info;
import sfc.consolidation.simulator.types.State;

@Data
public class StepResponseDto {
  private State state;
  private Info info;
  private boolean done;
}
