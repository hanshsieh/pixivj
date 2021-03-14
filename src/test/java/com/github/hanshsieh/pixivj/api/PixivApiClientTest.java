package com.github.hanshsieh.pixivj.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.hanshsieh.pixivj.model.IllustDetail;
import com.github.hanshsieh.pixivj.token.TokenProvider;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Verifications;
import okhttp3.Call;
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

  @BeforeEach
  public void beforeEach() throws Exception {
    new Expectations() {{
      httpClientBuilder.build();
      result = httpClient;

      httpClient.newCall(withInstanceOf(Request.class));
      result = httpCall;

      httpCall.execute();
      result = response;

      response.body();
      result = responseBody;
    }};
  }

  @Test
  @DisplayName("Get illustration detail")
  public void testGetIllustDetail() throws Exception {
    new Expectations() {{
      response.isSuccessful();
      result = true;

      responseBody.string();
      result = "{\n"
          + "  \"illust\": {\n"
          + "    \"id\": 58840323\n"
          + "  }\n"
          + "}";
      tokenProvider.getAccessToken();
      result = "test access token";
    }};
    PixivApiClient client = new PixivApiClient.Builder()
        .setBaseUrl("http://pixiv.example.com")
        .setTokenProvider(tokenProvider)
        .setUserAgent("test user agent")
        .build();
    new Verifications() {{
      httpClientBuilder.followRedirects(false);
      httpClientBuilder.followSslRedirects(false);
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
}
