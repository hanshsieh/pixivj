package com.github.hanshsieh.pixivj.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.github.hanshsieh.pixivj.util.JsonUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class IllustDetailTest {
  @Test
  @DisplayName("Deserialize and serialize")
  public void testDeserializeAndSerialize() {
    String str = "{\n"
        + "  \"illust\": {\n"
        + "    \"id\": 58840323,\n"
        + "    \"title\": \"西木野真姬\",\n"
        + "    \"type\": \"illust\",\n"
        + "    \"image_urls\": {\n"
        + "      \"square_medium\": \"https://i.pximg.net/c/360x360_70/img-master/img/2016/09/06/00/19/38/58840323_p0_square1200.jpg\",\n"
        + "      \"medium\": \"https://i.pximg.net/c/540x540_70/img-master/img/2016/09/06/00/19/38/58840323_p0_master1200.jpg\",\n"
        + "      \"large\": \"https://i.pximg.net/c/600x1200_90/img-master/img/2016/09/06/00/19/38/58840323_p0_master1200.jpg\"\n"
        + "    },\n"
        + "    \"caption\": \"\",\n"
        + "    \"restrict\": 0,\n"
        + "    \"user\": {\n"
        + "      \"id\": 7140895,\n"
        + "      \"profile_image_urls\": {\n"
        + "        \"medium\": \"https://i.pximg.net/user-profile/img/2017/10/14/19/53/34/13341815_8ce4e1531a063db27ae13fe61899ba7c_170.jpg\"\n"
        + "      },\n"
        + "      \"name\": \"極道畫師\",\n"
        + "      \"account\": \"jidaohuashi\",\n"
        + "      \"is_followed\": true\n"
        + "    },\n"
        + "    \"tags\": [\n"
        + "      {\n"
        + "        \"name\": \"水墨画\"\n"
        + "      },\n"
        + "      {\n"
        + "        \"name\": \"lovelive!\"\n"
        + "      },\n"
        + "      {\n"
        + "        \"name\": \"西木野真姬\"\n"
        + "      },\n"
        + "      {\n"
        + "        \"name\": \"ラブライブ!\"\n"
        + "      },\n"
        + "      {\n"
        + "        \"name\": \"西木野真姫\"\n"
        + "      },\n"
        + "      {\n"
        + "        \"name\": \"スクフェス\"\n"
        + "      },\n"
        + "      {\n"
        + "        \"name\": \"真姫ちゃんかわいいかきくけこ\"\n"
        + "      },\n"
        + "      {\n"
        + "        \"name\": \"ラブライブ!1000users入り\"\n"
        + "      },\n"
        + "      {\n"
        + "        \"name\": \"solo\"\n"
        + "      },\n"
        + "      {\n"
        + "        \"name\": \"小悪魔編\"\n"
        + "      }\n"
        + "    ],\n"
        + "    \"tools\": [\n"
        + "      \"筆\"\n"
        + "    ],\n"
        + "    \"create_date\": \"2016-09-06T00:19:38+09:00\",\n"
        + "    \"page_count\": 1,\n"
        + "    \"width\": 588,\n"
        + "    \"height\": 838,\n"
        + "    \"sanity_level\": 2,\n"
        + "    \"x_restrict\": 0,\n"
        + "    \"meta_single_page\": {\n"
        + "      \"original_image_url\": \"https://i.pximg.net/img-original/img/2016/09/06/00/19/38/58840323_p0.jpg\"\n"
        + "    },\n"
        + "    \"meta_pages\": [],\n"
        + "    \"total_view\": 38679,\n"
        + "    \"total_bookmarks\": 3128,\n"
        + "    \"is_bookmarked\": false,\n"
        + "    \"visible\": true,\n"
        + "    \"is_muted\": false\n"
        + "  }\n"
        + "}";
    IllustDetail detail = JsonUtils.GSON.fromJson(str, IllustDetail.class);
    IllustDetail detail2 = JsonUtils.GSON.fromJson(JsonUtils.GSON.toJson(detail), IllustDetail.class);
    assertEquals(detail, detail2);
    assertEquals(detail.hashCode(), detail2.hashCode());
    detail2.getIllust().setId(123L);
    assertNotEquals(detail, detail2);
    assertNotEquals(detail.hashCode(), detail2.hashCode());
  }
}
