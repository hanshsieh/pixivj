package com.github.pixivj.model;

import com.github.pixivj.util.JsonUtils;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class MetaPage {
  @SerializedName("image_urls")
  private MetaPageImageUrls imageUrls = null;

  /**
   * Get imageUrls
   * @return imageUrls
   **/
  public MetaPageImageUrls getImageUrls() {
    return imageUrls;
  }

  public void setImageUrls(MetaPageImageUrls imageUrls) {
    this.imageUrls = imageUrls;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MetaPage metaPage = (MetaPage) o;
    return Objects.equals(this.imageUrls, metaPage.imageUrls);
  }

  @Override
  public int hashCode() {
    return Objects.hash(imageUrls);
  }


  @Override
  public String toString() {
    return JsonUtils.GSON.toJson(this);
  }

}
