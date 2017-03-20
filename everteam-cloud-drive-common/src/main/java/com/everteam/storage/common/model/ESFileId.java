package com.everteam.storage.common.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * ESFileId
 */
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringCodegen", date = "2017-03-17T12:28:04.419Z")

public class ESFileId   {
  @JsonProperty("repositoryName")
  private String repositoryName = null;

  @JsonProperty("relativeId")
  private String relativeId = null;

  public ESFileId repositoryName(String repositoryName) {
    this.repositoryName = repositoryName;
    return this;
  }

   /**
   * The repository's ID
   * @return repositoryName
  **/
  @ApiModelProperty(example = "d290f1ee-6c54-4b01-90e6-d701748f0851", value = "The repository's ID")
  public String getRepositoryName() {
    return repositoryName;
  }

  public void setRepositoryName(String repositoryName) {
    this.repositoryName = repositoryName;
  }

  public ESFileId relativeId(String relativeId) {
    this.relativeId = relativeId;
    return this;
  }

   /**
   * The file ID in the repository
   * @return relativeId
  **/
  @ApiModelProperty(value = "The file ID in the repository")
  public String getRelativeId() {
    return relativeId;
  }

  public void setRelativeId(String relativeId) {
    this.relativeId = relativeId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ESFileId esFileId = (ESFileId) o;
    return Objects.equals(this.repositoryName, esFileId.repositoryName) &&
        Objects.equals(this.relativeId, esFileId.relativeId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(repositoryName, relativeId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ESFileId {\n");
    
    sb.append("    repositoryName: ").append(toIndentedString(repositoryName)).append("\n");
    sb.append("    relativeId: ").append(toIndentedString(relativeId)).append("\n");
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

