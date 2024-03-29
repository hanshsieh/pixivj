package com.github.hanshsieh.pixivj.model;

import com.github.hanshsieh.pixivj.util.JsonUtils;
import com.google.gson.annotations.SerializedName;

public class AuthError {

  @SerializedName("error")
  private String error;
  @SerializedName("errors")
  private AuthErrorDetails details;

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public AuthErrorDetails getDetails() {
    return details;
  }

  public void setDetails(AuthErrorDetails details) {
    this.details = details;
  }

  @Override
  public String toString() {
    return JsonUtils.GSON.toJson(this);
  }
}
