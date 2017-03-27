package com.everteam.storage.services;

import java.util.Objects;


/**
 * ESFileId
 */
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringCodegen", date = "2017-03-24T10:07:37.096Z")

public class ESFileId   {
  private String repositoryName = null;

  private String path = null;

  public ESFileId repositoryName(String repositoryName) {
    this.repositoryName = repositoryName;
    return this;
  }

   /**
   * The repository's ID
   * @return repositoryName
  **/
  public String getRepositoryName() {
    return repositoryName;
  }

  public void setRepositoryName(String repositoryName) {
    this.repositoryName = repositoryName;
  }

  public ESFileId path(String path) {
    this.path = path;
    return this;
  }

   /**
   * The file path in the repository
   * @return path
  **/
  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
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
        Objects.equals(this.path, esFileId.path);
  }

  @Override
  public int hashCode() {
    return Objects.hash(repositoryName, path);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ESFileId {\n");
    
    sb.append("    repositoryName: ").append(toIndentedString(repositoryName)).append("\n");
    sb.append("    path: ").append(toIndentedString(path)).append("\n");
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

