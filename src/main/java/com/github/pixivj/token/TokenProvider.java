package com.github.pixivj.token;

import com.github.pixivj.PixivClient;
import org.checkerframework.checker.nullness.qual.NonNull;
import com.github.pixivj.exception.AuthException;
import com.github.pixivj.model.Credential;

import java.io.Closeable;
import java.io.IOException;

/**
 * A provider for obtaining the access token for authentication.
 * It should be used by {@link PixivClient} in the following way:
 * - On initialization, {@link PixivClient} set itself into the token provider by calling {@link #setClient(PixivClient)}.
 * - When {@link PixivClient#login(Credential)} is invoked, the obtained access token and refresh token should be set
 *   into this token provider.
 * - Afterward, {@link PixivClient} will call {@link #getAccessToken()} to obtain the access token before calling the
 *   RESTful APIs.
 */
public interface TokenProvider extends Closeable {
  void setClient(@NonNull PixivClient client);
  void setTokens(
      @NonNull String accessToken,
      @NonNull String refreshToken,
      long expiresInSec
  );

  /**
   * Gets and possibly refresh the access token.
   * Whether the access token should be refreshed immediately or by a background is decided by the implementation.
   * If access token is not available, an exception should be thrown instead of returning null.
   * @return Access token. Always non-null.
   * @throws AuthException Authentication error.
   * @throws IOException IO error.
   * @throws IllegalStateException The current state doesn't allow getting an access token. For example, the client or
   * the access token is never set into this token provider.
   */
  @NonNull
  String getAccessToken() throws AuthException, IOException;
}
