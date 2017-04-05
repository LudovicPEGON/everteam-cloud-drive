package com.everteam.storage.common.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * ESParent
 */
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringCodegen", date = "2017-03-27T09:11:31.184Z")

public class ESParent   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("paths")
  private List<String> paths = new ArrayList<String>();

  public ESParent id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Parent folders which contain this file
   * @return id
  **/
  @ApiModelProperty(example = "d290f1ee-6c54-4b01-90e6-d701748f0851", value = "Parent folders which contain this file")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public ESParent paths(List<String> paths) {
    this.paths = paths;
    return this;
  }

  public ESParent addPathsItem(String pathsItem) {
    this.paths.add(pathsItem);
    return this;
  }

   /**
   * The list of parent names in order from root to direct parent file
   * @return paths
  **/
  @ApiModelProperty(value = "The list of parent names in order from root to direct parent file")
  public List<String> getPaths() {
    return paths;
  }

  public void setPaths(List<String> paths) {
    this.paths = paths;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ESParent esParent = (ESParent) o;
    return Objects.equals(this.id, esParent.id) &&
        Objects.equals(this.paths, esParent.paths);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, paths);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ESParent {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    paths: ").append(toIndentedString(paths)).append("\n");
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

