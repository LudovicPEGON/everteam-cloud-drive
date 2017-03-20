package com.everteam.storage.common.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * ESUser
 */
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringCodegen", date = "2017-03-17T12:28:04.419Z")

public class ESUser   {
  @JsonProperty("displayName")
  private String displayName = null;

  @JsonProperty("id")
  private String id = null;

  public ESUser displayName(String displayName) {
    this.displayName = displayName;
    return this;
  }

   /**
   * The owner's display name
   * @return displayName
  **/
  @ApiModelProperty(value = "The owner's display name")
  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public ESUser id(String id) {
    this.id = id;
    return this;
  }

   /**
   * The email address of the owner
   * @return id
  **/
  @ApiModelProperty(value = "The email address of the owner")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ESUser esUser = (ESUser) o;
    return Objects.equals(this.displayName, esUser.displayName) &&
        Objects.equals(this.id, esUser.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(displayName, id);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ESUser {\n");
    
    sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
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

