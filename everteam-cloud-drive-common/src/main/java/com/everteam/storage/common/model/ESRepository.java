package com.everteam.storage.common.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.annotations.ApiModelProperty;

/**
 * ESRepository
 */
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringCodegen", date = "2017-03-27T09:11:31.184Z")

public class ESRepository   {
  @JsonProperty("id")
  private String id = null;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    FS("FS"),
    
    GOOGLE("GOOGLE"),
    
    ONEDRIVE("ONEDRIVE");

    private String value;

    TypeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static TypeEnum fromValue(String text) {
      for (TypeEnum b : TypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("type")
  private TypeEnum type = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("rootDirectory")
  private String rootDirectory = null;

  @JsonProperty("clientSecret")
  private String clientSecret = null;
  
  @JsonProperty("clientId")
  private String clientId = null;

  public ESRepository id(String id) {
    this.id = id;
    return this;
  }

   /**
   * The repository's id
   * @return id
  **/
  @ApiModelProperty(example = "d290f1ee-6c54-4b01-90e6-d701748f0851", value = "The repository's id")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public ESRepository type(TypeEnum type) {
    this.type = type;
    return this;
  }

   /**
   * Get type
   * @return type
  **/
  @ApiModelProperty(value = "")
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public ESRepository name(String name) {
    this.name = name;
    return this;
  }

   /**
   * Get name
   * @return name
  **/
  @ApiModelProperty(value = "")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ESRepository rootDirectory(String rootDirectory) {
    this.rootDirectory = rootDirectory;
    return this;
  }

   /**
   * can be a reference to json file containing all client secrets
   * @return rootDirectory
  **/
  @ApiModelProperty(value = "can be a reference to json file containing all client secrets")
  public String getRootDirectory() {
    return rootDirectory;
  }

  public void setRootDirectory(String rootDirectory) {
    this.rootDirectory = rootDirectory;
  }

  public ESRepository clientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
    return this;
  }

   /**
   * can be a reference to json file containing all client secret
   * @return clientSecrets
  **/
  @ApiModelProperty(value = "can be a reference to json file containing all client secret")
  public String getClientSecret() {
    return clientSecret;
  }

  public void setClientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
  }
  
  public ESRepository clientId(String clientId) {
      this.clientId = clientId;
      return this;
    }

     /**
     * can be a reference to json file containing all client id
     * @return clientId
    **/
    @ApiModelProperty(value = "can be a reference to json file containing all client id")
    public String getClientId() {
      return clientId;
    }

    public void setClientId(String clientId) {
      this.clientId = clientId;
    }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ESRepository esRepository = (ESRepository) o;
    return Objects.equals(this.id, esRepository.id) &&
        Objects.equals(this.type, esRepository.type) &&
        Objects.equals(this.name, esRepository.name) &&
        Objects.equals(this.rootDirectory, esRepository.rootDirectory) &&
        Objects.equals(this.clientSecret, esRepository.clientSecret) &&
        Objects.equals(this.clientId, esRepository.clientId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, type, name, rootDirectory, clientSecret, clientId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ESRepository {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    rootDirectory: ").append(toIndentedString(rootDirectory)).append("\n");
    sb.append("    clientSecret: ").append(toIndentedString(clientSecret)).append("\n");
    sb.append("    clientId: ").append(toIndentedString(clientId)).append("\n");
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

