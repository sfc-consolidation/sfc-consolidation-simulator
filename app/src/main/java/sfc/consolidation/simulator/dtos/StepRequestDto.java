package sfc.consolidation.simulator.dtos;

import lombok.Data;

import sfc.consolidation.simulator.types.Action;

@Data
public class StepRequestDto {
  private Action action;
}
