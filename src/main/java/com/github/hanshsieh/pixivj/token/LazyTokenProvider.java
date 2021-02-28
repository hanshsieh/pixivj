package com.github.hanshsieh.pixivj.token;

import com.github.hanshsieh.pixivj.oauth.PixivOAuthClient;
import org.apache.commons.lang3.Validate;
import org.checkerframework.checker.nullness.qual.NonNull;
import com.github.hanshsieh.pixivj.exception.AuthException;
import com.github.hanshsieh.pixivj.model.AuthResult;
import com.github.hanshsieh.pixivj.model.Credential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

/**
 * A token provider that only refreshes the token when the {@link #getAccessToken()} is being
 * called. It is only suitable if there's always at least one call of {@link #getAccessToken()}
 * within the given expiry tolerance. For example, if the expiry tolerance is 10 minutes, then it
 * will try to refresh token when the remaining lifetime of the access token is less than 10 minutes
 * when {@link #getAccessToken()} is invoked. Therefore, if {@link #getAccessToken()} is not called
 * for every 10 minutes, it's possible that the access token will expire.
 */
public class LazyTokenProvider extends OAuthTokenProvider {

  public static final Duration DEFAULT_EXPIRE_TOLERANCE = Duration.ofMinutes(10);
  private static final Logger logger = LoggerFactory.getLogger(LazyTokenProvider.class);
  private final Duration expireTolerance;
  private final PixivOAuthClient client;

  /**
   * Creates a new instance with the given expiry tolerance. Notice that the given {@link
   * PixivOAuthClient} won't be closed when the token provider is closed.
   *
   * @param client          Pixiv OAuth client.
   * @param expireTolerance Expiry tolerance.
   */
  public LazyTokenProvider(@NonNull PixivOAuthClient client, @NonNull Duration expireTolerance) {
    Validate.notNull(client);
    Validate.notNull(expireTolerance, "Expire time tolerance cannot be null");
    this.client = client;
    this.expireTolerance = expireTolerance;
  }

  /**
   * Creates a new instance with expiry tolerance being 10 minutes. Notice that the given {@link
   * PixivOAuthClient} won't be closed when the token provider is closed.
   *
   * @param client Pixiv OAuth client.
   */
  public LazyTokenProvider(@NonNull PixivOAuthClient client) {
    this(client, DEFAULT_EXPIRE_TOLERANCE);
  }

  private boolean isNearExpire() {
    return Instant.now().plus(expireTolerance).isAfter(expireTime);
  }

  @Override
  @NonNull
  public String getAccessToken() throws AuthException, IOException, IllegalStateException {
    Validate.notNull(expireTime, "Expiry time not set. Are you logged in?");
    if (isNearExpire()) {
      Validate.notNull(client, "Client is not set");
      logger.debug("Access token is expired, refreshing it");
      Credential credential = new Credential();
      credential.setRefreshToken(this.refreshToken);
      credential.setGrantType(Credential.GRANT_TYPE_REFRESH_TOKEN);
      AuthResult authResult = client.authenticate(credential);
      setTokens(authResult.getAccessToken(), authResult.getRefreshToken(),
          authResult.getExpiresIn());
    }
    Validate.notNull(accessToken, "Access token not set");
    return this.accessToken;
  }
}
