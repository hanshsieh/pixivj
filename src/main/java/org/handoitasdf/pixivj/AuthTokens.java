package org.handoitasdf.pixivj;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class AuthTokens {
  @SerializedName("access_token")
  private String accessToken = null;

  @SerializedName("expires_in")
  private long expiresIn = -1;

  @SerializedName("token_type")
  private String tokenType = null;

  @SerializedName("scope")
  private String scope = null;

  @SerializedName("refresh_token")
  private String refreshToken = null;

  @SerializedName("user")
  private User user = null;

  @SerializedName("device_token")
  private String deviceToken = null;

  /**
   * The issued access token that can be used to access other APIs.
   * @return accessToken
   **/
  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  /**
   * The number of seconds from now that the access token will expire.
   * @return expiresIn
   **/
  public long getExpiresIn() {
    return expiresIn;
  }

  public void setExpiresIn(long expiresIn) {
    this.expiresIn = expiresIn;
  }

  /**
   * Get tokenType
   * @return tokenType
   **/
  public String getTokenType() {
    return tokenType;
  }

  public void setTokenType(String tokenType) {
    this.tokenType = tokenType;
  }

  /**
   * Get scope
   * @return scope
   **/
  public String getScope() {
    return scope;
  }

  public void setScope(String scope) {
    this.scope = scope;
  }

  /**
   * The token that can be used to get a refreshed access token within expiry time without doing the authentication again.
   * @return refreshToken
   **/
  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  /**
   * Get user
   * @return user
   **/
  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  /**
   * Get deviceToken
   * @return deviceToken
   **/
  public String getDeviceToken() {
    return deviceToken;
  }

  public void setDeviceToken(String deviceToken) {
    this.deviceToken = deviceToken;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AuthTokens authTokens = (AuthTokens) o;
    return Objects.equals(this.accessToken, authTokens.accessToken) &&
        Objects.equals(this.expiresIn, authTokens.expiresIn) &&
        Objects.equals(this.tokenType, authTokens.tokenType) &&
        Objects.equals(this.scope, authTokens.scope) &&
        Objects.equals(this.refreshToken, authTokens.refreshToken) &&
        Objects.equals(this.user, authTokens.user) &&
        Objects.equals(this.deviceToken, authTokens.deviceToken);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accessToken, expiresIn, tokenType, scope, refreshToken, user, deviceToken);
  }


  @Override
  public String toString() {
    return "class AuthTokens {\n" +
        "    accessToken: " + toIndentedString(accessToken) + "\n" +
        "    expiresIn: " + toIndentedString(expiresIn) + "\n" +
        "    tokenType: " + toIndentedString(tokenType) + "\n" +
        "    scope: " + toIndentedString(scope) + "\n" +
        "    refreshToken: " + toIndentedString(refreshToken) + "\n" +
        "    user: " + toIndentedString(user) + "\n" +
        "    deviceToken: " + toIndentedString(deviceToken) + "\n" +
        "}";
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private static String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}
