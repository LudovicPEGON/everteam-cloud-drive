package com.everteam.storage.common.model;

import java.util.Objects;
import com.everteam.storage.common.model.ESFile;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;

/**
 * ESFileList
 */
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringCodegen", date = "2017-03-14T08:27:34.208Z")

public class ESFileList   {
  @JsonProperty("path")
  private String path = null;

  @JsonProperty("items")
  private List<ESFile> items = new ArrayList<ESFile>();

  public ESFileList path(String path) {
    this.path = path;
    return this;
  }

   /**
   * The list of files.
   * @return path
  **/
  @ApiModelProperty(value = "The list of files.")
  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public ESFileList items(List<ESFile> items) {
    this.items = items;
    return this;
  }

  public ESFileList addItemsItem(ESFile itemsItem) {
    this.items.add(itemsItem);
    return this;
  }

   /**
   * The list of files.
   * @return items
  **/
  @ApiModelProperty(value = "The list of files.")
  public List<ESFile> getItems() {
    return items;
  }

  public void setItems(List<ESFile> items) {
    this.items = items;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ESFileList esFileList = (ESFileList) o;
    return Objects.equals(this.path, esFileList.path) &&
        Objects.equals(this.items, esFileList.items);
  }

  @Override
  public int hashCode() {
    return Objects.hash(path, items);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ESFileList {\n");
    
    sb.append("    path: ").append(toIndentedString(path)).append("\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
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

