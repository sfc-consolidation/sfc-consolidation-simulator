package sfc.consolidation.simulator.dtos;

import java.util.Optional;

import lombok.Data;
import sfc.consolidation.simulator.types.ResetArg;

@Data
public class ResetRequestDto {
  Optional<ResetArg> resetArg;
}
