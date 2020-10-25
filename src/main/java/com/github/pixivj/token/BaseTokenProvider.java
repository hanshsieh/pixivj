package com.github.pixivj.token;

import org.apache.commons.lang3.Validate;
import org.checkerframework.checker.nullness.qual.NonNull;
import com.github.pixivj.PixivClient;
import com.github.pixivj.exception.AuthException;

import java.io.IOException;
import java.time.Instant;

public class BaseTokenProvider implements TokenProvider {
  protected PixivClient client;
  protected String accessToken;
  protected String refreshToken;
  protected Instant expireTime;
  @Override
  public void setClient(@NonNull PixivClient client) {
    this.client = client;
  }
  @Override
  public void setTokens(
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
  public String getAccessToken() throws AuthException, IOException {
    return accessToken;
  }

  @Override
  public void close() throws IOException {

  }
}
