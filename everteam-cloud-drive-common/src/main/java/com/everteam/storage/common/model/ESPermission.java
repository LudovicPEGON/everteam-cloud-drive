package com.everteam.storage.common.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.annotations.ApiModelProperty;

/**
 * ESPermission
 */
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringCodegen", date = "2017-03-27T09:11:31.184Z")

public class ESPermission   {
  /**
   * The permission type. Allow or deny
   */
  public enum TypeEnum {
    ALLOW("allow"),
    
    DENY("deny");

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

  /**
   * The account type.
   */
  public enum AccountTypeEnum {
    USER("user"),
    
    GROUP("group"),
    
    DOMAIN("domain"),
    
    ANYONE("anyone");

    private String value;

    AccountTypeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static AccountTypeEnum fromValue(String text) {
      for (AccountTypeEnum b : AccountTypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("accountType")
  private AccountTypeEnum accountType = null;

  @JsonProperty("userId")
  private String userId = null;

  @JsonProperty("domain")
  private String domain = null;

  /**
   * The persmision role for this user
   */
  public enum RolesEnum {
    OWNER("owner"),
    
    READER("reader"),
    
    WRITER("writer");

    private String value;

    RolesEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static RolesEnum fromValue(String text) {
      for (RolesEnum b : RolesEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("roles")
  private List<RolesEnum> roles = new ArrayList<RolesEnum>();

  public ESPermission type(TypeEnum type) {
    this.type = type;
    return this;
  }

   /**
   * The permission type. Allow or deny
   * @return type
  **/
  @ApiModelProperty(value = "The permission type. Allow or deny")
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public ESPermission accountType(AccountTypeEnum accountType) {
    this.accountType = accountType;
    return this;
  }

   /**
   * The account type.
   * @return accountType
  **/
  @ApiModelProperty(value = "The account type.")
  public AccountTypeEnum getAccountType() {
    return accountType;
  }

  public void setAccountType(AccountTypeEnum accountType) {
    this.accountType = accountType;
  }

  public ESPermission userId(String userId) {
    this.userId = userId;
    return this;
  }

   /**
   * The id (can be its email or user code) of the user or group this permission refers to
   * @return userId
  **/
  @ApiModelProperty(value = "The id (can be its email or user code) of the user or group this permission refers to")
  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public ESPermission domain(String domain) {
    this.domain = domain;
    return this;
  }

   /**
   * The domain name of the entity this permission refers to. It is present when the permission type is user, group or domain
   * @return domain
  **/
  @ApiModelProperty(value = "The domain name of the entity this permission refers to. It is present when the permission type is user, group or domain")
  public String getDomain() {
    return domain;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }

  public ESPermission roles(List<RolesEnum> roles) {
    this.roles = roles;
    return this;
  }

  public ESPermission addRolesItem(RolesEnum rolesItem) {
    this.roles.add(rolesItem);
    return this;
  }

   /**
   * Get roles
   * @return roles
  **/
  @ApiModelProperty(value = "")
  public List<RolesEnum> getRoles() {
    return roles;
  }

  public void setRoles(List<RolesEnum> roles) {
    this.roles = roles;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ESPermission esPermission = (ESPermission) o;
    return Objects.equals(this.type, esPermission.type) &&
        Objects.equals(this.accountType, esPermission.accountType) &&
        Objects.equals(this.userId, esPermission.userId) &&
        Objects.equals(this.domain, esPermission.domain) &&
        Objects.equals(this.roles, esPermission.roles);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, accountType, userId, domain, roles);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ESPermission {\n");
    
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    accountType: ").append(toIndentedString(accountType)).append("\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    domain: ").append(toIndentedString(domain)).append("\n");
    sb.append("    roles: ").append(toIndentedString(roles)).append("\n");
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

