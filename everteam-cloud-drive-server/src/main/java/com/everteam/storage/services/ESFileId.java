package com.everteam.storage.services;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;


/**
 * ESFileId
 */
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringCodegen", date = "2017-03-24T10:07:37.096Z")

public class ESFileId   {
  private String repositoryId = null;
  private String path = null;

  public ESFileId repositoryName(String repositoryName) {
    this.repositoryId = repositoryName;
    return this;
  }
  
  public ESFileId(String repositoryId, String fileId) {
      if (fileId == null) {
          fileId = "";
      }
      this.repositoryId = repositoryId;
      this.path = fileId;
  }
  
  public String toUri() throws URISyntaxException {
      return new URI(repositoryId, null, "/" + path, null).toString();
  }
  
  
  public ESFileId(String uri) throws URISyntaxException {
      URI u = new URI(uri);
      this.repositoryId = u.getScheme();
      this.path = u.getPath().substring(1);
  }

   /**
   * The repository's ID
   * @return repositoryName
  **/
  public String getRepositoryId() {
    return repositoryId;
  }

  public void setRepositoryId(String repositoryId) {
    this.repositoryId = repositoryId;
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
    return Objects.equals(this.repositoryId, esFileId.repositoryId) &&
        Objects.equals(this.path, esFileId.path);
  }

  @Override
  public int hashCode() {
    return Objects.hash(repositoryId, path);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ESFileId {\n");
    
    sb.append("    repositoryId: ").append(toIndentedString(repositoryId)).append("\n");
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

