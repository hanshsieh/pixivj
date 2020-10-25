package org.handoitasdf.pixivj;

import okhttp3.OkHttpClient;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.handoitasdf.pixivj.exception.APIException;
import org.handoitasdf.pixivj.model.APIError;
import org.handoitasdf.pixivj.util.JsonUtils;

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
