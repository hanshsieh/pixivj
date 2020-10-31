package com.github.pixivj.model;

import com.google.gson.annotations.SerializedName;

public class AuthErrorDetails {
  @SerializedName("system")
  private ErrorInfo system;

  public ErrorInfo getSystem() {
    return system;
  }

  public void setSystem(ErrorInfo system) {
    this.system = system;
  }
}
