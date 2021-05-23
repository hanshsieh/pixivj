package com.github.hanshsieh.pixivj.model;

import org.checkerframework.checker.nullness.qual.NonNull;

public enum Restrict {
  PUBLIC("public"),
  PRIVATE("private");
  private final String strVal;
  Restrict(@NonNull String strVal) {
    this.strVal = strVal;
  }
  @NonNull
  public String string() {
    return strVal;
  }
}
