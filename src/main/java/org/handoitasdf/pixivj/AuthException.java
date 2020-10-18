package org.handoitasdf.pixivj;

import org.checkerframework.checker.nullness.qual.NonNull;

public class AuthException extends Exception {
  private final AuthErrorDetails details;
  public AuthException(@NonNull AuthError authError) {
    super(authError.getError());
    this.details = authError.getDetails();
  }

  @NonNull
  public AuthErrorDetails getDetails() {
    return details;
  }

}
