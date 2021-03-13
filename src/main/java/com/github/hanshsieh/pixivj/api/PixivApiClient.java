package com.github.hanshsieh.pixivj.api;

import com.github.hanshsieh.pixivj.exception.AuthException;
import com.github.hanshsieh.pixivj.exception.PixivException;
import com.github.hanshsieh.pixivj.model.IllustDetail;
import com.github.hanshsieh.pixivj.model.RankedIllusts;
import com.github.hanshsieh.pixivj.model.RankedIllustsFilter;
import com.github.hanshsieh.pixivj.model.RecommendedIllusts;
import com.github.hanshsieh.pixivj.model.RecommendedIllustsFilter;
import com.github.hanshsieh.pixivj.model.SearchedIllusts;
import com.github.hanshsieh.pixivj.model.SearchIllustsFilter;
import com.github.hanshsieh.pixivj.token.TokenProvider;
import com.github.hanshsieh.pixivj.util.Header;
import com.github.hanshsieh.pixivj.util.IoUtils;
import com.github.hanshsieh.pixivj.util.QueryParamConverter;
import java.io.Closeable;
import java.io.IOException;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.commons.lang3.Validate;
import org.checkerframework.checker.nullness.qual.NonNull;

public class PixivApiClient implements Closeable {

  public static class Builder {

    private String baseUrl = "https://app-api.pixiv.net";
    private String userAgent = Header.USER_AGENT_ANDROID;
    private TokenProvider tokenProvider = null;

    @NonNull
    public Builder setBaseUrl(@NonNull String baseUrl) {
      this.baseUrl = baseUrl;
      return this;
    }

    @NonNull
    public Builder setUserAgent(@NonNull String userAgent) {
      this.userAgent = userAgent;
      return this;
    }

    @NonNull
    public Builder setTokenProvider(@NonNull TokenProvider tokenProvider) {
      this.tokenProvider = tokenProvider;
      return this;
    }

    @NonNull
    public PixivApiClient build() {
      return new PixivApiClient(this);
    }
  }

  private final HttpUrl baseUrl;
  private final String userAgent;
  private final OkHttpClient httpClient;
  private final TokenProvider tokenProvider;
  private final QueryParamConverter queryParamConverter;
  private final ApiRequestSender requestSender;

  private PixivApiClient(@NonNull Builder builder) {
    Validate.notNull(builder.tokenProvider, "Token provider is not set");
    Validate.notNull(builder.baseUrl, "Base URL not set");
    Validate.notNull(builder.userAgent, "User agent not set");
    this.baseUrl = HttpUrl.parse(builder.baseUrl);
    this.userAgent = builder.userAgent;
    this.tokenProvider = builder.tokenProvider;
    this.httpClient = new OkHttpClient.Builder()
        .followRedirects(false)
        .followSslRedirects(false)
        .build();
    this.queryParamConverter = new QueryParamConverter();
    this.requestSender = new ApiRequestSender(httpClient);
  }

  /**
   * Grabs a list of ranked illustrations.
   *
   * @param filter the filter to use.
   * @return Ranked Illustrations.
   * @throws PixivException PixivException Error
   * @throws IOException    IOException Error
   */
  @NonNull
  public RankedIllusts getRankedIllusts(@NonNull RankedIllustsFilter filter)
      throws PixivException, IOException {
    return sendQueryRequest("v1/illust/ranking", filter, RankedIllusts.class);
  }

  /**
   * Grabs a list of recommended illustrations.
   *
   * @param filter the filter to use.
   * @return Recommended Illustrations.
   * @throws PixivException PixivException Error
   * @throws IOException    IOException Error
   */
  @NonNull
  public RecommendedIllusts getRecommendedIllusts(@NonNull RecommendedIllustsFilter filter)
      throws PixivException, IOException {
    return sendQueryRequest("v1/illust/recommended", filter, RecommendedIllusts.class);
  }

  /**
   * Searches illustrations.
   *
   * @param filter The filter to use.
   * @return Search Illustration Results.
   * @throws PixivException PixivException Error
   * @throws IOException    IOException Error
   */
  @NonNull
  public SearchedIllusts searchIllusts(@NonNull SearchIllustsFilter filter)
      throws PixivException, IOException {
    return sendQueryRequest("v1/search/illust", filter, SearchedIllusts.class);
  }

  /**
   * Grabs the details of a certain illustration.
   *
   * @param illustId the id of the illustration.
   * @return Illustration Detail.
   * @throws PixivException PixivException Error
   * @throws IOException    IOException Error
   */
  @NonNull
  public IllustDetail getIllustDetail(long illustId) throws PixivException, IOException {
    HttpUrl url = baseUrl.newBuilder()
        .addEncodedPathSegments("v1/illust/detail")
        .addQueryParameter("illust_id", String.valueOf(illustId))
        .build();
    Request request = createApiReqBuilder()
        .url(url)
        .get()
        .build();
    return requestSender.send(request, IllustDetail.class);
  }

  private <T, F> T sendQueryRequest(@NonNull String path, @NonNull F filter, Class<T> respType)
      throws PixivException, IOException {
    HttpUrl.Builder urlBuilder = baseUrl.newBuilder()
        .addEncodedPathSegments(path);
    queryParamConverter.toQueryParams(filter, urlBuilder);
    HttpUrl url = urlBuilder
        .build();
    Request request = createApiReqBuilder()
        .url(url)
        .get()
        .build();
    return requestSender.send(request, respType);
  }

  private Request.@NonNull Builder createApiReqBuilder() throws AuthException, IOException {
    return new Request.Builder()
        .header("User-Agent", userAgent)
        .header("Authorization", "Bearer " + tokenProvider.getAccessToken());
  }

  @Override
  public void close() throws IOException {
    this.tokenProvider.close();
    IoUtils.close(this.httpClient);
  }
}
