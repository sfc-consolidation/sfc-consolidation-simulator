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

package sfc.consolidation.simulator.generated.model;

import java.util.Objects;
import java.util.Arrays;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.IOException;
/**
 * Action
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2023-10-05T04:07:07.371478955Z[Etc/UTC]")

public class Action {
  @SerializedName("vnfId")
  private Object vnfId = null;

  @SerializedName("srvId")
  private Object srvId = null;

  public Action vnfId(Object vnfId) {
    this.vnfId = vnfId;
    return this;
  }

   /**
   * Get vnfId
   * @return vnfId
  **/
  @Schema(required = true, description = "")
  public Object getVnfId() {
    return vnfId;
  }

  public void setVnfId(Object vnfId) {
    this.vnfId = vnfId;
  }

  public Action srvId(Object srvId) {
    this.srvId = srvId;
    return this;
  }

   /**
   * Get srvId
   * @return srvId
  **/
  @Schema(required = true, description = "")
  public Object getSrvId() {
    return srvId;
  }

  public void setSrvId(Object srvId) {
    this.srvId = srvId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Action action = (Action) o;
    return Objects.equals(this.vnfId, action.vnfId) &&
        Objects.equals(this.srvId, action.srvId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(vnfId, srvId);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Action {\n");
    
    sb.append("    vnfId: ").append(toIndentedString(vnfId)).append("\n");
    sb.append("    srvId: ").append(toIndentedString(srvId)).append("\n");
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
