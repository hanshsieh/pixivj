package org.handoitasdf.pixivj.model;

import com.google.gson.annotations.SerializedName;
import org.handoitasdf.pixivj.model.ErrorInfo;

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
