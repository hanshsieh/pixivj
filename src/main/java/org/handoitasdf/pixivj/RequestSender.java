package org.handoitasdf.pixivj;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.handoitasdf.pixivj.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

abstract class RequestSender<E extends Exception> {
  private final OkHttpClient httpClient;
  public RequestSender(@NonNull OkHttpClient httpClient) {
    this.httpClient = httpClient;
  }
  private static final Logger logger = LoggerFactory.getLogger(RequestSender.class);
  public <T> T send(@NonNull Request request, @NonNull Class<T> respType) throws E, IOException  {
    logger.debug("Sending request {} {}", request.method(), request.url());
    try (Response response = httpClient.newCall(request).execute()) {
      ResponseBody respBody = response.body();
      assert respBody != null;
      String respBodyStr = respBody.string();
      logger.debug("Got response status code {}", response.code());
      if (!response.isSuccessful()) {
        throw createException(respBodyStr);
      } else {
        return JsonUtils.GSON.fromJson(respBodyStr, respType);
      }
    }
  }

  protected abstract E createException(@NonNull String respStr);
}
