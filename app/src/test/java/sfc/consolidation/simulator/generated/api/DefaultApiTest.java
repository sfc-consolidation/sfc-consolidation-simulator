package sfc.consolidation.simulator.generated.api;

import sfc.consolidation.simulator.generated.ApiClient;
import sfc.consolidation.simulator.generated.model.Action;
import sfc.consolidation.simulator.generated.model.Episode;
import sfc.consolidation.simulator.generated.model.HTTPValidationError;
import sfc.consolidation.simulator.generated.model.State;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for DefaultApi
 */
public class DefaultApiTest {

  private DefaultApi api;

  @Before
  public void setup() {
    api = new ApiClient().createService(DefaultApi.class);
  }

  /**
   * Inference
   *
   * 
   */
  @Test
  public void inferenceInferencePostTest() {
    State body = null;
    Object algorithm = null;
    // Action response = api.inferenceInferencePost(body, algorithm);

    // TODO: test validations
  }

  /**
   * Save
   *
   * 
   */
  @Test
  public void saveSaveEpisodePostTest() {
    Episode body = null;
    Object algorithm = null;
    // Object response = api.saveSaveEpisodePost(body, algorithm);

    // TODO: test validations
  }
}
