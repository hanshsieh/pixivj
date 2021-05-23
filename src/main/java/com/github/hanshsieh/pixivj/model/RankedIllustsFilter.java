package com.github.hanshsieh.pixivj.model;

import com.github.hanshsieh.pixivj.util.QueryParamConverter;
import com.google.gson.annotations.SerializedName;
import java.time.LocalDate;
import java.util.Objects;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

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

  @NonNull
  public static RankedIllustsFilter fromUrl(@NonNull String url) throws IllegalArgumentException {
    return new QueryParamConverter()
        .fromQueryParams(url, RankedIllustsFilter.class);
  }

  @Override
  public boolean equals(@Nullable Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RankedIllustsFilter that = (RankedIllustsFilter) o;
    return filter == that.filter &&
        mode == that.mode &&
        Objects.equals(date, that.date) &&
        Objects.equals(offset, that.offset);
  }

  @Override
  public int hashCode() {
    return Objects.hash(filter, mode, date, offset);
  }
}
