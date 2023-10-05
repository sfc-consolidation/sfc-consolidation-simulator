package sfc.consolidation.simulator.generated.api;

import sfc.consolidation.simulator.generated.CollectionFormats.*;

import retrofit2.Call;
import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import sfc.consolidation.simulator.generated.model.Episode;
import sfc.consolidation.simulator.generated.model.HTTPValidationError;
import sfc.consolidation.simulator.generated.model.State;
import sfc.consolidation.simulator.generated.model.Step;

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
   * @return Call&lt;Object&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("inference")
  Call<Object> inferenceInferencePost(
    @retrofit2.http.Body State body, @retrofit2.http.Query("algorithm") Object algorithm
  );

  /**
   * Save Episode
   * 
   * @param body  (required)
   * @param algorithm  (required)
   * @return Call&lt;Object&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("save-episode")
  Call<Object> saveEpisodeSaveEpisodePost(
    @retrofit2.http.Body Episode body, @retrofit2.http.Query("algorithm") Object algorithm
  );

  /**
   * Save Step
   * 
   * @param body  (required)
   * @param algorithm  (required)
   * @param id  (required)
   * @return Call&lt;Object&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("save-step")
  Call<Object> saveStepSaveStepPost(
    @retrofit2.http.Body Step body, @retrofit2.http.Query("algorithm") Object algorithm, @retrofit2.http.Query("id") Object id
  );

}
