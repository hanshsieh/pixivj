package com.github.hanshsieh.pixivj;

import com.github.hanshsieh.pixivj.exception.AuthException;
import com.github.hanshsieh.pixivj.model.AuthError;
import okhttp3.OkHttpClient;
import org.checkerframework.checker.nullness.qual.NonNull;
import com.github.hanshsieh.pixivj.util.JsonUtils;

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
