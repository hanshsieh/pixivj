package com.github.hanshsieh.pixivj.model;

import com.google.gson.annotations.SerializedName;

public class RecommendedIllustsFilter {

  @SerializedName("filter")
  private FilterType filter;
  @SerializedName("min_bookmark_id_for_recent_illust")
  private String minBookmarkIdForRecentIllust;
  @SerializedName("max_bookmark_id_for_recommend")
  private String maxBookmarkIdForRecommend;
  @SerializedName("offset")
  private Integer offset;
  @SerializedName("include_ranking_illusts")
  private Boolean includeRankingIllusts;
  @SerializedName("include_privacy_policy")
  private Boolean includePrivacyPolicy;

  public FilterType getFilter() {
    return filter;
  }

  public void setFilter(FilterType filter) {
    this.filter = filter;
  }

  public String getMinBookmarkIdForRecentIllust() {
    return minBookmarkIdForRecentIllust;
  }

  public void setMinBookmarkIdForRecentIllust(String minBookmarkIdForRecentIllust) {
    this.minBookmarkIdForRecentIllust = minBookmarkIdForRecentIllust;
  }

  public String getMaxBookmarkIdForRecommend() {
    return maxBookmarkIdForRecommend;
  }

  public void setMaxBookmarkIdForRecommend(String maxBookmarkIdForRecommend) {
    this.maxBookmarkIdForRecommend = maxBookmarkIdForRecommend;
  }

  public Integer getOffset() {
    return offset;
  }

  public void setOffset(Integer offset) {
    this.offset = offset;
  }

  public Boolean getIncludeRankingIllusts() {
    return includeRankingIllusts;
  }

  public void setIncludeRankingIllusts(Boolean includeRankingIllusts) {
    this.includeRankingIllusts = includeRankingIllusts;
  }

  public Boolean getIncludePrivacyPolicy() {
    return includePrivacyPolicy;
  }

  public void setIncludePrivacyPolicy(Boolean includePrivacyPolicy) {
    this.includePrivacyPolicy = includePrivacyPolicy;
  }
}
