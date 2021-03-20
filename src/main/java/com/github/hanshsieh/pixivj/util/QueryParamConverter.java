package com.github.hanshsieh.pixivj.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Map;
import okhttp3.HttpUrl;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * This converts the properties of an object to URL query parameters.
 */
public class QueryParamConverter {

  /**
   * Converts the properties of {@code obj} to query parameters, and add the query parameters to
   * the given {@code urlBuilder}. The values of the properties will be serialized as JSON strings.
   * @param obj The object to provide the query parameters values.
   * @param urlBuilder The URL builder to add the generated query parameters.
   * @param <T> The type of the object.
   */
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
