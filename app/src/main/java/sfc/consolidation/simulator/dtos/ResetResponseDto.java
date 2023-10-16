package sfc.consolidation.simulator.dtos;

import lombok.Data;

import sfc.consolidation.simulator.types.State;
import sfc.consolidation.simulator.types.Info;

@Data
public class ResetResponseDto {
  private State state;
  private Info info;
  private boolean done;
}
