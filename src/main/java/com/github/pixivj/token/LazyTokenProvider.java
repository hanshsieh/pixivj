package com.github.pixivj.token;

import org.apache.commons.lang3.Validate;
import org.checkerframework.checker.nullness.qual.NonNull;
import com.github.pixivj.exception.AuthException;
import com.github.pixivj.model.AuthResult;
import com.github.pixivj.model.Credential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

public class LazyTokenProvider extends BaseTokenProvider {
  public static final Duration DEFAULT_EXPIRE_TOLERANCE = Duration.ofMinutes(1);
  private static final Logger logger = LoggerFactory.getLogger(LazyTokenProvider.class);
  private final Duration expireTolerance;
  public LazyTokenProvider(@NonNull Duration expireTolerance) {
    Validate.notNull(expireTolerance, "Expire time tolerance cannot be null");
    this.expireTolerance = expireTolerance;
  }
  public LazyTokenProvider() {
    this(DEFAULT_EXPIRE_TOLERANCE);
  }

  private boolean isNearExpire() {
    return System.currentTimeMillis() + expireTolerance.toMillis() >= expireTime.toEpochMilli();
  }

  @Override
  @NonNull
  public String getAccessToken() throws AuthException, IOException, IllegalStateException {
    Validate.notNull(expireTime, "Expiry time not set. Are you logged in?");
    if (isNearExpire()) {
      logger.debug("Access token is expired, refreshing it");
      Credential credential = new Credential();
      credential.setRefreshToken(this.refreshToken);
      credential.setGrantType(Credential.GRANT_TYPE_REFRESH_TOKEN);
      AuthResult authResult = client.authenticate(credential);
      this.accessToken = authResult.getAccessToken();
      this.refreshToken = authResult.getRefreshToken();
      this.expireTime = Instant.now().plusSeconds(authResult.getExpiresIn());
    }
    Validate.notNull(accessToken, "Access token not set");
    return this.accessToken;
  }
}
