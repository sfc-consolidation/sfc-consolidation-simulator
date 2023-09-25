package alpha.consolidation.simulator.generated.api;

import alpha.consolidation.simulator.generated.CollectionFormats.*;

import retrofit2.Call;
import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import alpha.consolidation.simulator.generated.model.Action;
import alpha.consolidation.simulator.generated.model.EpisodeInput;
import alpha.consolidation.simulator.generated.model.EpisodeOutput;
import alpha.consolidation.simulator.generated.model.HTTPValidationError;
import alpha.consolidation.simulator.generated.model.StateInput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface DefaultApi {
  /**
   * Inference
   * 
   * @param body  (required)
   * @param algorithm  (required)
   * @return Call&lt;Action&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("inference")
  Call<Action> inferenceInferencePost(
    @retrofit2.http.Body StateInput body, @retrofit2.http.Query("algorithm") Object algorithm
  );

  /**
   * Save
   * 
   * @param body  (required)
   * @param algorithm  (required)
   * @return Call&lt;Object&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("save-episode")
  Call<Object> saveSaveEpisodePost(
    @retrofit2.http.Body EpisodeInput body, @retrofit2.http.Query("algorithm") Object algorithm
  );

  /**
   * Simulate
   * 
   * @param body  (required)
   * @param algorithm  (required)
   * @return Call&lt;EpisodeOutput&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("simulate")
  Call<EpisodeOutput> simulateSimulatePost(
    @retrofit2.http.Body StateInput body, @retrofit2.http.Query("algorithm") Object algorithm
  );

}
