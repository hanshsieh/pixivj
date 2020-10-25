package org.handoitasdf.pixivj;

import okhttp3.OkHttpClient;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.handoitasdf.pixivj.exception.AuthException;
import org.handoitasdf.pixivj.model.AuthError;
import org.handoitasdf.pixivj.util.JsonUtils;

class AuthRequestSender extends RequestSender<AuthException> {

  public AuthRequestSender(@NonNull OkHttpClient httpClient) {
    super(httpClient);
  }

  @Override
  protected AuthException createExceptionFromRespBody(@NonNull String respStr) {
    AuthError authError = JsonUtils.GSON.fromJson(respStr, AuthError.class);
    return new AuthException(authError);
  }
}
