package com.github.hanshsieh.pixivj.model;

import com.github.hanshsieh.pixivj.util.JsonUtils;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.checkerframework.checker.nullness.qual.Nullable;

public class RankedIllusts {

  @SerializedName("illusts")
  private List<Illustration> illusts = new ArrayList<>();

  @SerializedName("next_url")
  private String nextUrl = null;

  /**
   * Get illusts
   *
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
   *
   * @return nextUrl
   **/
  public String getNextUrl() {
    return nextUrl;
  }

  public void setNextUrl(String nextUrl) {
    this.nextUrl = nextUrl;
  }

  @Override
  public boolean equals(@Nullable Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RankedIllusts rankedIllustrations = (RankedIllusts) o;
    return Objects.equals(this.illusts, rankedIllustrations.illusts) &&
        Objects.equals(this.nextUrl, rankedIllustrations.nextUrl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(illusts, nextUrl);
  }


  @Override
  public String toString() {
    return JsonUtils.GSON.toJson(this);
  }
}

