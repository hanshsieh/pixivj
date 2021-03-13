package com.github.hanshsieh.pixivj.token;

import java.time.Instant;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A token provider that has the token refreshing functionality.
 * A {@link TokenRefresher} should automatically refresh the token if possible.
 * The situation to refresh a token is implementation dependent.
 */
public interface TokenRefresher extends TokenProvider {

  /**
   * Updates the tokens stored internally.
   * @param accessToken Access token.
   * @param refreshToken Refresh token.
   * @param expiryTime Expiry time of the access token.
   */
  void updateTokens(
      @NonNull String accessToken,
      @NonNull String refreshToken,
      @NonNull Instant expiryTime);

  /**
   * Gets refresh token.
   * @return The Access token
   */
  @NonNull
  String getRefreshToken();
}
