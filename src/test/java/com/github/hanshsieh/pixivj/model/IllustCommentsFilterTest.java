package com.github.hanshsieh.pixivj.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class IllustCommentsFilterTest {
  @Test
  @DisplayName("from URL + equal + hashCode")
  public void testFromUrl() {
    String url = "https://app-api.pixiv.net/v2/illust/comments?illust_id=90000399&last_comment_id=118642853";
    IllustCommentsFilter filter = IllustCommentsFilter.fromUrl(url);
    assertEquals(filter.getIllustId(), 90000399L);
    assertEquals(filter.getLastCommentId(), 118642853L);
    IllustCommentsFilter filter2 = new IllustCommentsFilter();
    filter2.setIllustId(90000399L);
    filter2.setLastCommentId(118642853L);
    assertEquals(filter, filter);
    assertEquals(filter, filter2);
    assertNotEquals(filter, null);
    assertNotEquals(filter, "123");
    assertEquals(filter.hashCode(), filter2.hashCode());
  }
}
