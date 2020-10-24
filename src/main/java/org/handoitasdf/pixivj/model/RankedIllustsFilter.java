package org.handoitasdf.pixivj.model;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;

public class RankedIllustsFilter {
  @SerializedName("filter")
  private FilterType filter;
  @SerializedName("mode")
  private FilterMode mode;
  @SerializedName("date")
  private LocalDate date;
  @SerializedName("offset")
  private Integer offset;

  public FilterType getFilter() {
    return filter;
  }

  public void setFilter(FilterType filter) {
    this.filter = filter;
  }

  public FilterMode getMode() {
    return mode;
  }

  public void setMode(FilterMode mode) {
    this.mode = mode;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public Integer getOffset() {
    return offset;
  }

  public void setOffset(Integer offset) {
    this.offset = offset;
  }
}
