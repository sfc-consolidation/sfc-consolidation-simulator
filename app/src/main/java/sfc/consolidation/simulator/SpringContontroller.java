package sfc.consolidation.simulator;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import okhttp3.Response;

import org.springframework.web.bind.annotation.RequestBody;

import sfc.consolidation.simulator.dtos.ResetRequestDto;
import sfc.consolidation.simulator.dtos.ResetResponseDto;
import sfc.consolidation.simulator.dtos.StepRequestDto;
import sfc.consolidation.simulator.dtos.StepResponseDto;
import sfc.consolidation.simulator.types.Action;
import sfc.consolidation.simulator.types.Info;
import sfc.consolidation.simulator.types.ResetArg;
import sfc.consolidation.simulator.types.State;
import sfc.consolidation.simulator.utils.EnvSingletone;

@RestController
public class SpringContontroller {
  @GetMapping("/healthy")
  public ResponseEntity<String> index() {
    return ResponseEntity.ok("Simulator API in ready.");
  }

  @PostMapping("/reset")
  public ResponseEntity<ResetResponseDto> reset(@RequestBody ResetRequestDto requestBody) {
    final State state;
    final Env env;
    if (requestBody.getResetArg().isPresent()) {
      ResetArg arg = requestBody.getResetArg().get();
      env = EnvSingletone.getInstance();
      state = env.reset(arg);
    } else {
      env = EnvSingletone.getInstance();
      state = env.reset();
    }
    Info info = env.getResult();
    ResetResponseDto res = new ResetResponseDto();
    {
      res.setState(state);
      res.setInfo(info);
      res.setDone(!info.isSuccess());
    }
    return ResponseEntity.ok(res);
  }

  @PostMapping("/step")
  public ResponseEntity<StepResponseDto> step(@RequestBody StepRequestDto requestBody) {
    Action action = requestBody.getAction();
    Env env = EnvSingletone.getInstance();
    State state = env.step(action);
    Info info = env.getResult();
    StepResponseDto res = new StepResponseDto();
    {
      res.setState(state);
      res.setInfo(info);
      res.setDone(!info.isSuccess());
    }
    return ResponseEntity.ok(res);
  }
}
