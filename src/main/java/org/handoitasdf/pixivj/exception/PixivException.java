package org.handoitasdf.pixivj.exception;

import org.checkerframework.checker.nullness.qual.NonNull;

public class PixivException extends Exception {
  public PixivException(@NonNull String message) {
    super(message);
  }
}
