package com.github.hanshsieh.pixivj.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RankedIllustsFilterTest {
  @Test
  @DisplayName("from URL + equals + hashCode")
  public void testFromUrl() {
    String url = "https://app-api.pixiv.net/v1/illust/ranking?" +
        "filter=for_android&" +
        "mode=week_rookie&" +
        "date=2020-01-01&" +
        "offset=2";
    RankedIllustsFilter filter = RankedIllustsFilter.fromUrl(url);
    assertEquals(FilterType.FOR_ANDROID, filter.getFilter());
    assertEquals(FilterMode.WEEK_ROOKIE, filter.getMode());
    RankedIllustsFilter filter2 = new RankedIllustsFilter();
    filter2.setFilter(FilterType.FOR_ANDROID);
    filter2.setMode(FilterMode.WEEK_ROOKIE);
    filter2.setDate(LocalDate.of(2020, 1, 1));
    filter2.setOffset(2);
    assertEquals(filter, filter);
    assertEquals(filter, filter2);
    assertNotEquals(filter, null);
    assertNotEquals(filter, "123");
    assertEquals(filter.hashCode(), filter2.hashCode());
    filter2.setOffset(3);
    assertNotEquals(filter, filter2);
  }
}
