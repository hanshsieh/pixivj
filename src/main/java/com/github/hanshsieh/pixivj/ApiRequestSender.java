package com.github.hanshsieh.pixivj;

import okhttp3.OkHttpClient;
import org.checkerframework.checker.nullness.qual.NonNull;
import com.github.hanshsieh.pixivj.exception.APIException;
import com.github.hanshsieh.pixivj.model.APIError;
import com.github.hanshsieh.pixivj.util.JsonUtils;

class ApiRequestSender extends RequestSender<APIException> {

  public ApiRequestSender(@NonNull OkHttpClient httpClient) {
    super(httpClient);
  }

  @Override
  protected APIException createExceptionFromRespBody(@NonNull String respStr) {
    APIError error = JsonUtils.GSON.fromJson(respStr, APIError.class);
    return new APIException(error);
  }
}
