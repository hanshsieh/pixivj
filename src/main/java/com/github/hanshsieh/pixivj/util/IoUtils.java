package com.github.hanshsieh.pixivj.util;

import okhttp3.OkHttpClient;
import org.checkerframework.checker.nullness.qual.NonNull;

public class IoUtils {
  public static void close(@NonNull OkHttpClient httpClient) {
    httpClient.dispatcher().executorService().shutdown();
    httpClient.connectionPool().evictAll();
  }
}
