package com.github.hanshsieh.pixivj.exception;

import com.github.hanshsieh.pixivj.model.APIError;
import org.checkerframework.checker.nullness.qual.NonNull;

public class APIException extends PixivException {

  private final APIError error;

  public APIException(@NonNull APIError error) {
    super(error.toString());
    this.error = error;
  }

  @NonNull
  public APIError getError() {
    return error;
  }
}
