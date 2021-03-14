package com.github.hanshsieh.pixivj.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.github.hanshsieh.pixivj.util.JsonUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RecommendedIllustsTest {
  @Test
  @DisplayName("Deserialize and serialize")
  public void testDeserializeAndSerialize() {
    String str = "{\n"
        + "  \"illusts\": [\n"
        + "    {\n"
        + "      \"id\": 88353368,\n"
        + "      \"title\": \"Wedding\",\n"
        + "      \"type\": \"illust\",\n"
        + "      \"image_urls\": {\n"
        + "        \"square_medium\": \"https://i.pximg.net/c/360x360_70/img-master/img/2021/03/10/20/00/01/88353368_p0_square1200.jpg\",\n"
        + "        \"medium\": \"https://i.pximg.net/c/540x540_70/img-master/img/2021/03/10/20/00/01/88353368_p0_master1200.jpg\",\n"
        + "        \"large\": \"https://i.pximg.net/c/600x1200_90/img-master/img/2021/03/10/20/00/01/88353368_p0_master1200.jpg\"\n"
        + "      },\n"
        + "      \"caption\": \"去年、12月の作品です。<br />コスチュームを作りました。 <br /><br /><strong><a href=\\\"https://twitter.com/Goth_lunacle\\\" target=\\\"_blank\\\">twitter/Goth_lunacle</a></strong>\",\n"
        + "      \"restrict\": 0,\n"
        + "      \"user\": {\n"
        + "        \"id\": 15657814,\n"
        + "        \"profile_image_urls\": {\n"
        + "          \"medium\": \"https://i.pximg.net/user-profile/img/2017/02/19/18/21/25/12170140_91433554c3d8f06f3d579ff276489ff9_170.jpg\"\n"
        + "        },\n"
        + "        \"name\": \"Lunacle @お仕事募集中\",\n"
        + "        \"account\": \"lunapolla0619\",\n"
        + "        \"is_followed\": false\n"
        + "      },\n"
        + "      \"tags\": [\n"
        + "        {\n"
        + "          \"name\": \"女の子\"\n"
        + "        },\n"
        + "        {\n"
        + "          \"name\": \"原神\"\n"
        + "        },\n"
        + "        {\n"
        + "          \"name\": \"Lumine\"\n"
        + "        },\n"
        + "        {\n"
        + "          \"name\": \"蛍(原神)\"\n"
        + "        },\n"
        + "        {\n"
        + "          \"name\": \"荧\"\n"
        + "        },\n"
        + "        {\n"
        + "          \"name\": \"ウェディングドレス\"\n"
        + "        },\n"
        + "        {\n"
        + "          \"name\": \"原神5000users入り\"\n"
        + "        }\n"
        + "      ],\n"
        + "      \"tools\": [\n"
        + "        \"Photoshop\"\n"
        + "      ],\n"
        + "      \"create_date\": \"2021-03-10T20:00:01+09:00\",\n"
        + "      \"page_count\": 1,\n"
        + "      \"width\": 620,\n"
        + "      \"height\": 877,\n"
        + "      \"sanity_level\": 2,\n"
        + "      \"x_restrict\": 0,\n"
        + "      \"meta_single_page\": {\n"
        + "        \"original_image_url\": \"https://i.pximg.net/img-original/img/2021/03/10/20/00/01/88353368_p0.jpg\"\n"
        + "      },\n"
        + "      \"meta_pages\": [],\n"
        + "      \"total_view\": 20788,\n"
        + "      \"total_bookmarks\": 7325,\n"
        + "      \"is_bookmarked\": false,\n"
        + "      \"visible\": true,\n"
        + "      \"is_muted\": false\n"
        + "    }\n"
        + "  ],\n"
        + "  \"contest_exists\": false,\n"
        + "  \"privacy_policy\": {\n"
        + "    \"version\": \"2-ja\",\n"
        + "    \"message\": \"pixivは2020年3月30日付けでプライバシーポリシーを改定しました。\",\n"
        + "    \"url\": \"https://www.pixiv.net/terms/?page=new_privacy&appname=pixiv_ios\"\n"
        + "  },\n"
        + "  \"next_url\": \"https://app-api.pixiv.net/v1/illust/recommended?filter=for_android&include_ranking_illusts=false&include_privacy_policy=false&min_bookmark_id_for_recent_illust=10970031153&max_bookmark_id_for_recommend=2668303492&offset=0\"\n"
        + "}";
    RecommendedIllusts illusts1 = JsonUtils.GSON.fromJson(str, RecommendedIllusts.class);
    RecommendedIllusts illusts2 = JsonUtils.GSON.fromJson(JsonUtils.GSON.toJson(illusts1), RecommendedIllusts.class);
    assertEquals(illusts1, illusts2);
    assertEquals(illusts1.hashCode(), illusts2.hashCode());
    illusts2.getIllusts().get(0).setId(123L);
    assertNotEquals(illusts1, illusts2);
    assertNotEquals(illusts1.hashCode(), illusts2.hashCode());
  }
}
