package com.github.hanshsieh.pixivj.model;

import com.google.gson.annotations.SerializedName;

public class SearchIllustsFilter {
  public static final String SORT_DATE_ASC = "date_asc";
  public static final String SORT_DATE_DESC = "date_desc";
  @SerializedName("filter")
  private FilterType filter;
  @SerializedName("include_translated_tag_results")
  private Boolean includeTranslatedTagResults;
  @SerializedName("merge_plain_keyword_results")
  private Boolean mergePlainKeywordResults;
  @SerializedName("word")
  private String word;
  @SerializedName("sort")
  private String sort;
  @SerializedName("offset")
  private Integer offset;
  @SerializedName("search_target")
  private SearchTarget searchTarget;

  public FilterType getFilter() {
    return filter;
  }

  public void setFilter(FilterType filter) {
    this.filter = filter;
  }

  public Boolean getIncludeTranslatedTagResults() {
    return includeTranslatedTagResults;
  }

  public void setIncludeTranslatedTagResults(Boolean includeTranslatedTagResults) {
    this.includeTranslatedTagResults = includeTranslatedTagResults;
  }

  public Boolean getMergePlainKeywordResults() {
    return mergePlainKeywordResults;
  }

  public void setMergePlainKeywordResults(Boolean mergePlainKeywordResults) {
    this.mergePlainKeywordResults = mergePlainKeywordResults;
  }

  public String getWord() {
    return word;
  }

  /**
   * Sets the word to search. It may contain several words separated by spaces.
   * @param word The word to search.
   */
  public void setWord(String word) {
    this.word = word;
  }

  public String getSort() {
    return sort;
  }

  /**
   * Sets the sorting criteria.
   * It can be one of the constants:
   * - {@link #SORT_DATE_ASC}
   * - {@link #SORT_DATE_DESC}
   * @param sort Sorting criteria.
   */
  public void setSort(String sort) {
    this.sort = sort;
  }

  public Integer getOffset() {
    return offset;
  }

  /**
   * Sets the offset. Default 0.
   * @param offset Offset.
   */
  public void setOffset(Integer offset) {
    this.offset = offset;
  }

  public SearchTarget getSearchTarget() {
    return searchTarget;
  }

  public void setSearchTarget(SearchTarget searchTarget) {
    this.searchTarget = searchTarget;
  }
}
