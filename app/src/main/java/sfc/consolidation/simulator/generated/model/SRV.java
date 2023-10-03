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
 * SRV
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2023-10-03T10:19:01.754399281+09:00[Asia/Seoul]")

public class SRV {
  @SerializedName("id")
  private Object id = null;

  @SerializedName("totVcpuNum")
  private Object totVcpuNum = null;

  @SerializedName("totVmemMb")
  private Object totVmemMb = null;

  @SerializedName("sleepable")
  private Object sleepable = null;

  public SRV id(Object id) {
    this.id = id;
    return this;
  }

   /**
   * Get id
   * @return id
  **/
  @Schema(required = true, description = "")
  public Object getId() {
    return id;
  }

  public void setId(Object id) {
    this.id = id;
  }

  public SRV totVcpuNum(Object totVcpuNum) {
    this.totVcpuNum = totVcpuNum;
    return this;
  }

   /**
   * Get totVcpuNum
   * @return totVcpuNum
  **/
  @Schema(required = true, description = "")
  public Object getTotVcpuNum() {
    return totVcpuNum;
  }

  public void setTotVcpuNum(Object totVcpuNum) {
    this.totVcpuNum = totVcpuNum;
  }

  public SRV totVmemMb(Object totVmemMb) {
    this.totVmemMb = totVmemMb;
    return this;
  }

   /**
   * Get totVmemMb
   * @return totVmemMb
  **/
  @Schema(required = true, description = "")
  public Object getTotVmemMb() {
    return totVmemMb;
  }

  public void setTotVmemMb(Object totVmemMb) {
    this.totVmemMb = totVmemMb;
  }

  public SRV sleepable(Object sleepable) {
    this.sleepable = sleepable;
    return this;
  }

   /**
   * Get sleepable
   * @return sleepable
  **/
  @Schema(required = true, description = "")
  public Object getSleepable() {
    return sleepable;
  }

  public void setSleepable(Object sleepable) {
    this.sleepable = sleepable;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SRV SRV = (SRV) o;
    return Objects.equals(this.id, SRV.id) &&
        Objects.equals(this.totVcpuNum, SRV.totVcpuNum) &&
        Objects.equals(this.totVmemMb, SRV.totVmemMb) &&
        Objects.equals(this.sleepable, SRV.sleepable);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, totVcpuNum, totVmemMb, sleepable);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SRV {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    totVcpuNum: ").append(toIndentedString(totVcpuNum)).append("\n");
    sb.append("    totVmemMb: ").append(toIndentedString(totVmemMb)).append("\n");
    sb.append("    sleepable: ").append(toIndentedString(sleepable)).append("\n");
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
