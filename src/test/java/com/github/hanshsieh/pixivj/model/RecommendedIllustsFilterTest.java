package com.github.hanshsieh.pixivj.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.hanshsieh.pixivj.util.JsonUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RecommendedIllustsFilterTest {
  @Test
  @DisplayName("from URL + equals + hashCode")
  public void testFromUrl() {
    String url = "https://app-api.pixiv.net/v1/illust/recommended?" +
        "filter=for_android&" +
        "min_bookmark_id_for_recent_illust=111&" +
        "max_bookmark_id_for_recommend=222&" +
        "offset=2&" +
        "include_ranking_illusts=true&" +
        "include_privacy_policy=false";
    RecommendedIllustsFilter filter = RecommendedIllustsFilter.fromUrl(url);
    assertEquals(FilterType.FOR_ANDROID, filter.getFilter());
    assertEquals("111", filter.getMinBookmarkIdForRecentIllust());
    assertEquals("222", filter.getMaxBookmarkIdForRecommend());
    assertEquals(2, filter.getOffset());
    assertEquals(true, filter.getIncludeRankingIllusts());
    assertEquals(false, filter.getIncludePrivacyPolicy());
    RecommendedIllustsFilter filter2 = new RecommendedIllustsFilter();
    filter2.setFilter(FilterType.FOR_ANDROID);
    filter2.setMinBookmarkIdForRecentIllust("111");
    filter2.setMaxBookmarkIdForRecommend("222");
    filter2.setOffset(2);
    filter2.setIncludeRankingIllusts(true);
    filter2.setIncludePrivacyPolicy(false);
    assertEquals(filter, filter);
    assertEquals(filter, filter2);
    assertNotEquals(filter, null);
    assertNotEquals(filter, "123");
    assertEquals(filter.hashCode(), filter2.hashCode());
    filter2.setOffset(3);
    assertNotEquals(filter, filter2);
  }
}
