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
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.IOException;
/**
 * StateInput
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2023-09-25T19:59:40.601719331+09:00[Asia/Seoul]")

public class StateInput {
  @SerializedName("rackList")
  private Object rackList = null;

  @SerializedName("sfcList")
  private Object sfcList = null;

  @SerializedName("vnfList")
  private Object vnfList = null;

  public StateInput rackList(Object rackList) {
    this.rackList = rackList;
    return this;
  }

   /**
   * Get rackList
   * @return rackList
  **/
  @Schema(required = true, description = "")
  public Object getRackList() {
    return rackList;
  }

  public void setRackList(Object rackList) {
    this.rackList = rackList;
  }

  public StateInput sfcList(Object sfcList) {
    this.sfcList = sfcList;
    return this;
  }

   /**
   * Get sfcList
   * @return sfcList
  **/
  @Schema(required = true, description = "")
  public Object getSfcList() {
    return sfcList;
  }

  public void setSfcList(Object sfcList) {
    this.sfcList = sfcList;
  }

  public StateInput vnfList(Object vnfList) {
    this.vnfList = vnfList;
    return this;
  }

   /**
   * Get vnfList
   * @return vnfList
  **/
  @Schema(required = true, description = "")
  public Object getVnfList() {
    return vnfList;
  }

  public void setVnfList(Object vnfList) {
    this.vnfList = vnfList;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StateInput stateInput = (StateInput) o;
    return Objects.equals(this.rackList, stateInput.rackList) &&
        Objects.equals(this.sfcList, stateInput.sfcList) &&
        Objects.equals(this.vnfList, stateInput.vnfList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rackList, sfcList, vnfList);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StateInput {\n");
    
    sb.append("    rackList: ").append(toIndentedString(rackList)).append("\n");
    sb.append("    sfcList: ").append(toIndentedString(sfcList)).append("\n");
    sb.append("    vnfList: ").append(toIndentedString(vnfList)).append("\n");
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
