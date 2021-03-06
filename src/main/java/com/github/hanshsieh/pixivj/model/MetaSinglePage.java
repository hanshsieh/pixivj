package com.github.hanshsieh.pixivj.model;

import com.github.hanshsieh.pixivj.util.JsonUtils;
import com.google.gson.annotations.SerializedName;
import java.util.Objects;

public class MetaSinglePage {

  @SerializedName("original_image_url")
  private String originalImageUrl = null;

  /**
   * The URL to the original size of the illustration.
   *
   * @return originalImageUrl
   **/
  public String getOriginalImageUrl() {
    return originalImageUrl;
  }

  public void setOriginalImageUrl(String originalImageUrl) {
    this.originalImageUrl = originalImageUrl;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MetaSinglePage metaSinglePage = (MetaSinglePage) o;
    return Objects.equals(this.originalImageUrl, metaSinglePage.originalImageUrl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(originalImageUrl);
  }


  @Override
  public String toString() {
    return JsonUtils.GSON.toJson(this);
  }

}