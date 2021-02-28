package com.github.hanshsieh.pixivj.token;

import com.github.hanshsieh.pixivj.oauth.PixivOAuthClient;
import org.apache.commons.lang3.Validate;
import org.checkerframework.checker.nullness.qual.NonNull;
import com.github.hanshsieh.pixivj.exception.AuthException;

import java.io.IOException;
import java.time.Instant;

/**
 * A basic token provider that never refresh the given access token.
 */
public class OAuthTokenProvider implements TokenProvider {

  protected volatile String accessToken;
  protected volatile String refreshToken;
  protected volatile Instant expireTime;

  protected void setTokens(
      @NonNull String accessToken,
      @NonNull String refreshToken,
      long expiresInSec
  ) {
    Validate.notNull(accessToken, "Access token cannot be null");
    Validate.notNull(refreshToken, "Refresh token cannot be null");
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.expireTime = Instant.now().plusSeconds(expiresInSec);
  }

  @Override
  @NonNull
  public String getAccessToken() throws AuthException, IllegalStateException, IOException {
    if (accessToken == null) {
      throw new IllegalStateException("Access token is not set");
    }
    return accessToken;
  }

  @Override
  public void close() {
  }
}
