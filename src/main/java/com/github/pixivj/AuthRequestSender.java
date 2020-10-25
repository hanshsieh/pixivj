package com.github.pixivj;

import okhttp3.OkHttpClient;
import org.checkerframework.checker.nullness.qual.NonNull;
import com.github.pixivj.exception.AuthException;
import com.github.pixivj.model.AuthError;
import com.github.pixivj.util.JsonUtils;

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
