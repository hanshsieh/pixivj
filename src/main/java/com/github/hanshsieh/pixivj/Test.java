package com.github.hanshsieh.pixivj;

import com.github.hanshsieh.pixivj.model.RecommendedIllustsFilter;
import com.github.hanshsieh.pixivj.util.QueryParamConverter;

public class Test {
  public static void main(String[] args) throws Exception {
    QueryParamConverter converter = new QueryParamConverter();
    RecommendedIllustsFilter filter = converter.fromQueryParams("https://app-api.pixiv.net/v2/illust/comments?include_ranking_illusts=true&filter=for_ios",
        RecommendedIllustsFilter.class);
    System.out.printf("getIncludeRankingIllusts: %s\n", filter.getIncludeRankingIllusts());
    System.out.printf("getFilter: %s\n", filter.getFilter());
  }
}
