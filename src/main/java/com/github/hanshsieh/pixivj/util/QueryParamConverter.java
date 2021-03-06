package com.github.hanshsieh.pixivj.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Map;
import okhttp3.HttpUrl;
import org.checkerframework.checker.nullness.qual.NonNull;

public class QueryParamConverter {

  public <T> void toQueryParams(@NonNull T obj, HttpUrl.@NonNull Builder urlBuilder) {
    JsonObject jsonObj = JsonUtils.GSON.toJsonTree(obj)
        .getAsJsonObject();
    for (Map.Entry<String, JsonElement> entry : jsonObj.entrySet()) {
      if (entry.getValue() != null) {
        urlBuilder.addQueryParameter(entry.getKey(), entry.getValue().getAsString());
      }
    }
  }
}
