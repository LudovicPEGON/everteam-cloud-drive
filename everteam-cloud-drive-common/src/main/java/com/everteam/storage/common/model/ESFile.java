package com.everteam.storage.common.model;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * ESFile
 */
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringCodegen", date = "2017-03-27T09:11:31.184Z")

public class ESFile   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("repositoryId")
  private String repositoryId = null;

  @JsonProperty("parents")
  private List<ESParent> parents = new ArrayList<ESParent>();

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("fileSize")
  private Long fileSize = null;

  @JsonProperty("directory")
  private Boolean directory = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("mimeType")
  private String mimeType = null;

  @JsonProperty("creationTime")
  private OffsetDateTime creationTime = null;

  @JsonProperty("lastModifiedTime")
  private OffsetDateTime lastModifiedTime = null;

  @JsonProperty("lastAccessTime")
  private OffsetDateTime lastAccessTime = null;

  @JsonProperty("lastModifiedUser")
  private ESUser lastModifiedUser = null;

  @JsonProperty("owners")
  private List<ESUser> owners = new ArrayList<ESUser>();

  @JsonProperty("permissions")
  private List<ESPermission> permissions = new ArrayList<ESPermission>();

  @JsonProperty("checksum")
  private String checksum = null;

  public ESFile id(String id) {
    this.id = id;
    return this;
  }

   /**
   * The file's id.
   * @return id
  **/
  @ApiModelProperty(example = "d290f1ee-6c54-4b01-90e6-d701748f0851", value = "The file's id.")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public ESFile repositoryId(String repositoryId) {
    this.repositoryId = repositoryId;
    return this;
  }

   /**
   * The repository's id.
   * @return repositoryId
  **/
  @ApiModelProperty(example = "d290f1ee-6c54-4b01-90e6-d701748f0851", value = "The repository's id.")
  public String getRepositoryId() {
    return repositoryId;
  }

  public void setRepositoryId(String repositoryId) {
    this.repositoryId = repositoryId;
  }

  public ESFile parents(List<ESParent> parents) {
    this.parents = parents;
    return this;
  }

  public ESFile addParentsItem(ESParent parentsItem) {
    this.parents.add(parentsItem);
    return this;
  }

   /**
   * The files parent list. Some drives juste manage one parent
   * @return parents
  **/
  @ApiModelProperty(value = "The files parent list. Some drives juste manage one parent")
  public List<ESParent> getParents() {
    return parents;
  }

  public void setParents(List<ESParent> parents) {
    this.parents = parents;
  }

  public ESFile name(String name) {
    this.name = name;
    return this;
  }

   /**
   * The title of the file
   * @return name
  **/
  @ApiModelProperty(example = "My file.txt", value = "The title of the file")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ESFile fileSize(Long fileSize) {
    this.fileSize = fileSize;
    return this;
  }

   /**
   * The size of the file in bytes.
   * @return fileSize
  **/
  @ApiModelProperty(value = "The size of the file in bytes.")
  public Long getFileSize() {
    return fileSize;
  }

  public void setFileSize(Long fileSize) {
    this.fileSize = fileSize;
  }

  public ESFile directory(Boolean directory) {
    this.directory = directory;
    return this;
  }

   /**
   * true if file is a directory, false otherwise
   * @return directory
  **/
  @ApiModelProperty(value = "true if file is a directory, false otherwise")
  public Boolean getDirectory() {
    return directory;
  }

  public void setDirectory(Boolean directory) {
    this.directory = directory;
  }

  public ESFile description(String description) {
    this.description = description;
    return this;
  }

   /**
   * A short description of the file
   * @return description
  **/
  @ApiModelProperty(example = "My first file", value = "A short description of the file")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public ESFile mimeType(String mimeType) {
    this.mimeType = mimeType;
    return this;
  }

   /**
   * The MIME type of the file
   * @return mimeType
  **/
  @ApiModelProperty(example = "text-plain", value = "The MIME type of the file")
  public String getMimeType() {
    return mimeType;
  }

  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }

  public ESFile creationTime(OffsetDateTime creationTime) {
    this.creationTime = creationTime;
    return this;
  }

   /**
   * Creation time for this file
   * @return creationTime
  **/
  @ApiModelProperty(value = "Creation time for this file")
  public OffsetDateTime getCreationTime() {
    return creationTime;
  }

  public void setCreationTime(OffsetDateTime creationTime) {
    this.creationTime = creationTime;
  }

  public ESFile lastModifiedTime(OffsetDateTime lastModifiedTime) {
    this.lastModifiedTime = lastModifiedTime;
    return this;
  }

   /**
   * Last time this file was modified by anyone
   * @return lastModifiedTime
  **/
  @ApiModelProperty(value = "Last time this file was modified by anyone")
  public OffsetDateTime getLastModifiedTime() {
    return lastModifiedTime;
  }

  public void setLastModifiedTime(OffsetDateTime lastModifiedTime) {
    this.lastModifiedTime = lastModifiedTime;
  }

  public ESFile lastAccessTime(OffsetDateTime lastAccessTime) {
    this.lastAccessTime = lastAccessTime;
    return this;
  }

   /**
   * Last time this file was acceded by anyone
   * @return lastAccessTime
  **/
  @ApiModelProperty(value = "Last time this file was acceded by anyone")
  public OffsetDateTime getLastAccessTime() {
    return lastAccessTime;
  }

  public void setLastAccessTime(OffsetDateTime lastAccessTime) {
    this.lastAccessTime = lastAccessTime;
  }

  public ESFile lastModifiedUser(ESUser lastModifiedUser) {
    this.lastModifiedUser = lastModifiedUser;
    return this;
  }

   /**
   * Get lastModifiedUser
   * @return lastModifiedUser
  **/
  @ApiModelProperty(value = "")
  public ESUser getLastModifiedUser() {
    return lastModifiedUser;
  }

  public void setLastModifiedUser(ESUser lastModifiedUser) {
    this.lastModifiedUser = lastModifiedUser;
  }

  public ESFile owners(List<ESUser> owners) {
    this.owners = owners;
    return this;
  }

  public ESFile addOwnersItem(ESUser ownersItem) {
    this.owners.add(ownersItem);
    return this;
  }

   /**
   * Get owners
   * @return owners
  **/
  @ApiModelProperty(value = "")
  public List<ESUser> getOwners() {
    return owners;
  }

  public void setOwners(List<ESUser> owners) {
    this.owners = owners;
  }

  public ESFile permissions(List<ESPermission> permissions) {
    this.permissions = permissions;
    return this;
  }

  public ESFile addPermissionsItem(ESPermission permissionsItem) {
    this.permissions.add(permissionsItem);
    return this;
  }

   /**
   * Get permissions
   * @return permissions
  **/
  @ApiModelProperty(value = "")
  public List<ESPermission> getPermissions() {
    return permissions;
  }

  public void setPermissions(List<ESPermission> permissions) {
    this.permissions = permissions;
  }

  public ESFile checksum(String checksum) {
    this.checksum = checksum;
    return this;
  }

   /**
   * The file's content  MD5 checksum
   * @return checksum
  **/
  @ApiModelProperty(example = "2B693E6A1483B70BF5CD7C511035879E", value = "The file's content  MD5 checksum")
  public String getChecksum() {
    return checksum;
  }

  public void setChecksum(String checksum) {
    this.checksum = checksum;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ESFile esFile = (ESFile) o;
    return Objects.equals(this.id, esFile.id) &&
        Objects.equals(this.repositoryId, esFile.repositoryId) &&
        Objects.equals(this.parents, esFile.parents) &&
        Objects.equals(this.name, esFile.name) &&
        Objects.equals(this.fileSize, esFile.fileSize) &&
        Objects.equals(this.directory, esFile.directory) &&
        Objects.equals(this.description, esFile.description) &&
        Objects.equals(this.mimeType, esFile.mimeType) &&
        Objects.equals(this.creationTime, esFile.creationTime) &&
        Objects.equals(this.lastModifiedTime, esFile.lastModifiedTime) &&
        Objects.equals(this.lastAccessTime, esFile.lastAccessTime) &&
        Objects.equals(this.lastModifiedUser, esFile.lastModifiedUser) &&
        Objects.equals(this.owners, esFile.owners) &&
        Objects.equals(this.permissions, esFile.permissions) &&
        Objects.equals(this.checksum, esFile.checksum);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, repositoryId, parents, name, fileSize, directory, description, mimeType, creationTime, lastModifiedTime, lastAccessTime, lastModifiedUser, owners, permissions, checksum);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ESFile {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    repositoryId: ").append(toIndentedString(repositoryId)).append("\n");
    sb.append("    parents: ").append(toIndentedString(parents)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    fileSize: ").append(toIndentedString(fileSize)).append("\n");
    sb.append("    directory: ").append(toIndentedString(directory)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    mimeType: ").append(toIndentedString(mimeType)).append("\n");
    sb.append("    creationTime: ").append(toIndentedString(creationTime)).append("\n");
    sb.append("    lastModifiedTime: ").append(toIndentedString(lastModifiedTime)).append("\n");
    sb.append("    lastAccessTime: ").append(toIndentedString(lastAccessTime)).append("\n");
    sb.append("    lastModifiedUser: ").append(toIndentedString(lastModifiedUser)).append("\n");
    sb.append("    owners: ").append(toIndentedString(owners)).append("\n");
    sb.append("    permissions: ").append(toIndentedString(permissions)).append("\n");
    sb.append("    checksum: ").append(toIndentedString(checksum)).append("\n");
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

