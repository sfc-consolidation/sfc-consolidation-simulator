package sfc.consolidation.simulator.generated.api;

import sfc.consolidation.simulator.generated.CollectionFormats.*;

import retrofit2.Call;
import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import sfc.consolidation.simulator.generated.model.Action;
import sfc.consolidation.simulator.generated.model.Episode;
import sfc.consolidation.simulator.generated.model.HTTPValidationError;
import sfc.consolidation.simulator.generated.model.State;

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
    @retrofit2.http.Body State body, @retrofit2.http.Query("algorithm") Object algorithm
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
    @retrofit2.http.Body Episode body, @retrofit2.http.Query("algorithm") Object algorithm
  );

}
