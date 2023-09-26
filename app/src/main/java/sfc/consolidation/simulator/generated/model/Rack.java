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
 * Rack
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2023-09-25T19:59:40.601719331+09:00[Asia/Seoul]")

public class Rack {
  @SerializedName("id")
  private Object id = null;

  @SerializedName("srvList")
  private Object srvList = null;

  public Rack id(Object id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * 
   * @return id
   **/
  @Schema(required = true, description = "")
  public Object getId() {
    return id;
  }

  public void setId(Object id) {
    this.id = id;
  }

  public Rack srvList(Object srvList) {
    this.srvList = srvList;
    return this;
  }

  /**
   * Get srvList
   * 
   * @return srvList
   **/
  @Schema(required = true, description = "")
  public Object getSrvList() {
    return srvList;
  }

  public void setSrvList(Object srvList) {
    this.srvList = srvList;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Rack rack = (Rack) o;
    return Objects.equals(this.id, rack.id) &&
        Objects.equals(this.srvList, rack.srvList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, srvList);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Rack {\n");

    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    srvList: ").append(toIndentedString(srvList)).append("\n");
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