package com.github.pixivj.model;

import com.github.pixivj.util.JsonUtils;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SearchIllusts that = (SearchIllusts) o;
    return Objects.equals(getIllusts(), that.getIllusts()) &&
            Objects.equals(getNextUrl(), that.getNextUrl());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getIllusts(), getNextUrl());
  }

  @Override
  public String toString() {
    return JsonUtils.GSON.toJson(this);
  }

}