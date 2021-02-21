package com.github.hanshsieh.pixivj.model;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.com.google.common.base.Objects;

public class Credential {
  public static final String CLIENT_ID_MOBILE = "MOBrBDS8blbauoSck0ZfDbtuzpyT";
  public static final String CLIENT_SECRET_MOBILE = "lsACyCD94FhDUtGTXi3QzcFE2uU1hqtDaKeqrdwj";
  public static final String GRANT_TYPE_PASSWORD = "password";
  public static final String GRANT_TYPE_REFRESH_TOKEN = "refresh_token";
  public static final String DEFAULT_HASH_SECRET = "28c1fdd170a5204386cb1313c7077b34f83e4aaf4aa829ce78c231e05b0bae2c";
  private String clientId = CLIENT_ID_MOBILE;
  private String clientSecret = CLIENT_SECRET_MOBILE;
  private String grantType = GRANT_TYPE_PASSWORD;
  private String username;
  private String password;
  private String refreshToken;
  private String hashSecret = DEFAULT_HASH_SECRET;

  /**
   * Gets the OAuth client ID.
   * @return Client ID.
   */
  public String getClientId() {
    return clientId;
  }

  /**
   * Sets the OAuth client ID.
   * The default value is for acting as the official mobile app.
   * @param clientId Client ID.
   */
  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  /**
   * Gets the OAuth client secret.
   * @return Client secret.
   */
  public String getClientSecret() {
    return clientSecret;
  }

  /***
   * Sets OAuth client secret.
   * The default value is for acting as the official mobile app.
   * @param clientSecret Client secret.
   */
  public void setClientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
  }

  /**
   * Gets grant type.
   * @return Grant type.
   */
  public String getGrantType() {
    return grantType;
  }

  /**
   * Sets grant type.
   * @param grantType Grant type.
   */
  public void setGrantType(String grantType) {
    this.grantType = grantType;
  }

  /**
   * Gets username.
   * It's required when the grant type is "password".
   * @return Username.
   */
  public String getUsername() {
    return username;
  }

  /**
   * Sets username.
   * It's required when the grant type is "password".
   * @param username Username.
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Gets password.
   * @return Password.
   */
  public String getPassword() {
    return password;
  }

  /**
   * Sets password.
   * It's required when the grant type is "password".
   * @param password Password.
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Gets refresh token.
   * @return Refresh token.
   */
  public String getRefreshToken() {
    return refreshToken;
  }

  /**
   * Sets refresh token.
   * It's required when the grant type is "refresh_token".
   * @param refreshToken Refresh token.
   */
  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  /**
   * Gets hash secret used for generating the client hash.
   * @return Hash secret.
   */
  public String getHashSecret() {
    return hashSecret;
  }

  /**
   * Sets the hash secret used for generating the client hash.
   * @param hashSecret Hash secret.
   */
  public void setHashSecret(String hashSecret) {
    this.hashSecret = hashSecret;
  }

  @Override
  public boolean equals(@Nullable Object other) {
    if (other == null || other.getClass() != getClass()) {
      return false;
    }
    Credential that = (Credential) other;
    return Objects.equal(clientId, that.clientId) &&
            Objects.equal(clientSecret, that.clientSecret) &&
            Objects.equal(grantType, that.grantType) &&
            Objects.equal(username, that.username) &&
            Objects.equal(password, that.password) &&
            Objects.equal(refreshToken, that.refreshToken) &&
            Objects.equal(hashSecret, that.hashSecret);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(
        clientId,
        clientSecret,
        grantType,
        username,
        password,
        refreshToken,
        hashSecret
    );
  }
}
