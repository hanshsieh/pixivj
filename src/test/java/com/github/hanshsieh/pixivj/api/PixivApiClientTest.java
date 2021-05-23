package com.github.hanshsieh.pixivj.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.hanshsieh.pixivj.exception.APIException;
import com.github.hanshsieh.pixivj.exception.PixivException;
import com.github.hanshsieh.pixivj.model.FilterMode;
import com.github.hanshsieh.pixivj.model.FilterType;
import com.github.hanshsieh.pixivj.model.IllustDetail;
import com.github.hanshsieh.pixivj.model.RankedIllusts;
import com.github.hanshsieh.pixivj.model.RankedIllustsFilter;
import com.github.hanshsieh.pixivj.model.RecommendedIllusts;
import com.github.hanshsieh.pixivj.model.RecommendedIllustsFilter;
import com.github.hanshsieh.pixivj.model.SearchedIllusts;
import com.github.hanshsieh.pixivj.model.SearchedIllustsFilter;
import com.github.hanshsieh.pixivj.token.TokenProvider;
import java.lang.reflect.Executable;
import java.time.LocalDate;
import java.util.concurrent.ExecutorService;
import java.util.regex.Pattern;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Verifications;
import okhttp3.Call;
import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PixivApiClientTest {
  @Injectable
  private TokenProvider tokenProvider;
  @Mocked
  private OkHttpClient.Builder httpClientBuilder;
  @Injectable
  private OkHttpClient httpClient;
  @Injectable
  private Call httpCall;
  @Injectable
  private Response response;
  @Injectable
  private ResponseBody responseBody;
  @Injectable
  private Dispatcher dispatcher;
  @Injectable
  private ExecutorService executorService;
  @Injectable
  private ConnectionPool connectionPool;
  private PixivApiClient client;

  @BeforeEach
  public void beforeEach() throws Exception {
    new Expectations() {{
      httpClientBuilder.build();
      result = httpClient;

      httpClient.newCall(withInstanceOf(Request.class));
      result = httpCall;
      minTimes = 0;

      httpCall.execute();
      result = response;
      minTimes = 0;

      response.body();
      result = responseBody;
      minTimes = 0;

      response.isSuccessful();
      result = true;
      minTimes = 0;

      tokenProvider.getAccessToken();
      result = "test access token";
      minTimes = 0;

      httpClient.dispatcher();
      result = dispatcher;
      minTimes = 0;

      dispatcher.executorService();
      result = executorService;
      minTimes = 0;

      httpClient.connectionPool();
      result = connectionPool;
      minTimes = 0;
    }};
    client = new PixivApiClient.Builder()
        .setBaseUrl("http://pixiv.example.com")
        .setTokenProvider(tokenProvider)
        .setUserAgent("test user agent")
        .build();
  }

  @Test
  @DisplayName("Constructed successfully")
  public void testConstruct() {
    new Verifications() {{
      httpClientBuilder.followRedirects(false);
      httpClientBuilder.followSslRedirects(false);
    }};
  }

  @Test
  @DisplayName("Close")
  public void testClose() throws Exception {
    client.close();
    new Verifications() {{
      tokenProvider.close();
      times = 1;

      executorService.shutdown();
      connectionPool.evictAll();
    }};
  }

  @Test
  @DisplayName("Get ranked illustrations")
  public void testGetRankedIllustrations() throws Exception {
    new Expectations() {{
      responseBody.string();
      result = "{\n"
        + "  \"illusts\": [],\n"
        + "  \"next_url\": \"http://next.url.com\"\n"
        + "}";
    }};
    RankedIllustsFilter filter = new RankedIllustsFilter();
    filter.setDate(LocalDate.of(2020, 1, 1));
    filter.setFilter(FilterType.FOR_ANDROID);
    filter.setMode(FilterMode.DAY);
    filter.setOffset(10);
    RankedIllusts illusts = client.getRankedIllusts(filter);
    new Verifications() {{
      Request request;
      httpClient.newCall(request = withCapture());
      assertEquals("GET", request.method());
      assertEquals(HttpUrl.parse("http://pixiv.example.com/v1/illust/ranking?"
              + "filter=for_android&mode=day&date=2020-01-01&offset=10"),
          request.url());
      assertEquals("test user agent", request.header("User-Agent"));
      assertEquals("Bearer test access token", request.header("Authorization"));
    }};
    assertEquals("http://next.url.com", illusts.getNextUrl());
  }

  @Test
  @DisplayName("Get recommended illustrations")
  public void testGetRecommendedIllustrations() throws Exception {
    new Expectations() {{
      responseBody.string();
      result = "{\n"
          + "  \"illusts\": [],\n"
          + "  \"next_url\": \"http://next.url.com\"\n"
          + "}";
    }};
    RecommendedIllustsFilter filter = new RecommendedIllustsFilter();
    filter.setFilter(FilterType.FOR_ANDROID);
    filter.setOffset(10);
    RecommendedIllusts illusts = client.getRecommendedIllusts(filter);
    new Verifications() {{
      Request request;
      httpClient.newCall(request = withCapture());
      assertEquals("GET", request.method());
      assertEquals(HttpUrl.parse("http://pixiv.example.com/v1/illust/recommended?"
              + "filter=for_android&offset=10"),
          request.url());
      assertEquals("test user agent", request.header("User-Agent"));
      assertEquals("Bearer test access token", request.header("Authorization"));
    }};
    assertEquals("http://next.url.com", illusts.getNextUrl());
  }

  @Test
  @DisplayName("Search illustrations")
  public void testSearchIllustrations() throws Exception {
    new Expectations() {{
      responseBody.string();
      result = "{\n"
          + "  \"illusts\": [],\n"
          + "  \"next_url\": \"http://next.url.com\"\n"
          + "}";
    }};
    SearchedIllustsFilter filter = new SearchedIllustsFilter();
    filter.setWord("グラスワンダー");
    filter.setFilter(FilterType.FOR_ANDROID);
    filter.setOffset(10);
    SearchedIllusts illusts = client.searchIllusts(filter);
    new Verifications() {{
      Request request;
      httpClient.newCall(request = withCapture());
      assertEquals("GET", request.method());
      assertEquals(HttpUrl.parse("http://pixiv.example.com/v1/search/illust?"
              + "filter=for_android&word=%E3%82%B0%E3%83%A9%E3%82%B9%E3%83%AF%E3%83%B3%E3%83%80%E3%83%BC&offset=10"),
          request.url());
      assertEquals("test user agent", request.header("User-Agent"));
      assertEquals("Bearer test access token", request.header("Authorization"));
    }};
    assertEquals("http://next.url.com", illusts.getNextUrl());
  }

  @Test
  @DisplayName("Get illustration detail")
  public void testGetIllustDetail() throws Exception {
    new Expectations() {{
      responseBody.string();
      result = "{\n"
          + "  \"illust\": {\n"
          + "    \"id\": 58840323\n"
          + "  }\n"
          + "}";
    }};
    IllustDetail detail = client.getIllustDetail(100);
    new Verifications() {{
      Request request;
      httpClient.newCall(request = withCapture());
      assertEquals("GET", request.method());
      assertEquals(HttpUrl.parse("http://pixiv.example.com/v1/illust/detail?illust_id=100"),
          request.url());
      assertEquals("test user agent", request.header("User-Agent"));
      assertEquals("Bearer test access token", request.header("Authorization"));
    }};
    assertEquals(58840323, detail.getIllust().getId());
  }
  @Test
  @DisplayName("Get illustration detail and get 500")
  public void testGetIllustDetailAndGet500() throws Exception {
    new Expectations() {{
      response.isSuccessful();
      result = false;

      responseBody.string();
      result = "{\n"
          + "  \"error\": {\n"
          + "    \"user_message\": \"test user message\",\n"
          + "    \"message\": \"test message\",\n"
          + "    \"reason\": \"test reason\",\n"
          + "    \"user_message_details\": \"{}\"\n"
          + "  }\n"
          + "}";
    }};
    try {
      client.getIllustDetail(100);
      throw new AssertionError("Expecting exception");
    } catch (APIException ex) {
      assertEquals("test user message", ex.getError().getError().getUserMessage());
      assertEquals("test message", ex.getError().getError().getMessage());
    }
  }

  @Test
  @DisplayName("Download from an image URL")
  public void testDownload() throws Exception {
    new Expectations() {{
      response.isSuccessful();
      result = true;
    }};
    String url = "https://i.pximg.net/c/600x1200_90_webp/img-master/img/2021/05/21/00/47/53/89979147_p0_master1200.jpg";
    try(Response resp = client.download(url)) {
      assertNotNull(resp);
      new Verifications() {{
        response.close();
        times = 0;

        Request request;
        httpClient.newCall(request = withCapture());
        assertEquals("GET", request.method());
        assertEquals(HttpUrl.parse(url),
            request.url());
        assertEquals("test user agent", request.header("User-Agent"));
        assertEquals("http://pixiv.example.com/", request.header("Referer"));
        assertNull(request.header("Authorization"));
      }};
    }
  }

  @Test
  @DisplayName("Download from an image URL and get 500")
  public void testDownloadAnd500() throws Exception {
    new Expectations() {{
      response.isSuccessful();
      result = false;

      response.code();
      result = 500;
    }};
    String url = "https://i.pximg.net/c/600x1200_90_webp/img-master/img/2021/05/21/00/47/53/89979147_p0_master1200.jpg";
    try {
      client.download(url);
      throw new AssertionError("Expecting exception");
    } catch (PixivException ex) {
      assertTrue(Pattern.compile(".*500.*").matcher(ex.getMessage()).matches());
      new Verifications() {{
        response.close();
        times = 1;
      }};
    }
  }
}
