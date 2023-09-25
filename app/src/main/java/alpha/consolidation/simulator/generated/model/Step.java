/*
 * FastAPI
 * No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 * OpenAPI spec version: 0.1.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package alpha.consolidation.simulator.generated.model;

import java.util.Objects;
import java.util.Arrays;
import alpha.consolidation.simulator.generated.model.Action;
import alpha.consolidation.simulator.generated.model.Info;
import alpha.consolidation.simulator.generated.model.State;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.IOException;
/**
 * Step
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2023-09-25T12:35:12.047132721+09:00[Asia/Seoul]")

public class Step {
  @SerializedName("state")
  private State state = null;

  @SerializedName("action")
  private Action action = null;

  @SerializedName("info")
  private Info info = null;

  public Step state(State state) {
    this.state = state;
    return this;
  }

   /**
   * Get state
   * @return state
  **/
  @Schema(required = true, description = "")
  public State getState() {
    return state;
  }

  public void setState(State state) {
    this.state = state;
  }

  public Step action(Action action) {
    this.action = action;
    return this;
  }

   /**
   * Get action
   * @return action
  **/
  @Schema(required = true, description = "")
  public Action getAction() {
    return action;
  }

  public void setAction(Action action) {
    this.action = action;
  }

  public Step info(Info info) {
    this.info = info;
    return this;
  }

   /**
   * Get info
   * @return info
  **/
  @Schema(required = true, description = "")
  public Info getInfo() {
    return info;
  }

  public void setInfo(Info info) {
    this.info = info;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Step step = (Step) o;
    return Objects.equals(this.state, step.state) &&
        Objects.equals(this.action, step.action) &&
        Objects.equals(this.info, step.info);
  }

  @Override
  public int hashCode() {
    return Objects.hash(state, action, info);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Step {\n");
    
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
    sb.append("    action: ").append(toIndentedString(action)).append("\n");
    sb.append("    info: ").append(toIndentedString(info)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}