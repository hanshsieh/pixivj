package com.github.pixivj.model;

import com.github.pixivj.util.JsonUtils;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SearchIllusts {
  @SerializedName("illusts")
  private List<Illustration> illusts = new ArrayList<>();

  @SerializedName("next_url")
  private String nextUrl = null;

  /**
   * Get illusts
   * @return illusts
   **/
  public List<Illustration> getIllusts() {
    return illusts;
  }

  public void setIllusts(List<Illustration> illusts) {
    this.illusts = illusts;
  }

  /**
   * The next URL that can be used for querying the next page, if any.
   * @return nextUrl
   **/
  public String getNextUrl() {
    return nextUrl;
  }

  public void setNextUrl(String nextUrl) {
    this.nextUrl = nextUrl;
  }

  @Override
  public String toString() {
    return JsonUtils.GSON.toJson(this);
  }

}