package com.github.pixivj.exception;

import com.github.pixivj.model.AuthError;
import org.checkerframework.checker.nullness.qual.NonNull;

public class AuthException extends PixivException {
  private final AuthError error;
  public AuthException(@NonNull AuthError authError) {
    super(authError.toString());
    this.error = authError;
  }

  @NonNull
  public AuthError getError() {
    return error;
  }

}
