package com.github.hanshsieh.pixivj.api;

import com.github.hanshsieh.pixivj.exception.APIException;
import com.github.hanshsieh.pixivj.exception.AuthException;
import com.github.hanshsieh.pixivj.exception.PixivException;
import com.github.hanshsieh.pixivj.model.IllustDetail;
import com.github.hanshsieh.pixivj.model.RankedIllusts;
import com.github.hanshsieh.pixivj.model.RankedIllustsFilter;
import com.github.hanshsieh.pixivj.model.RecommendedIllusts;
import com.github.hanshsieh.pixivj.model.RecommendedIllustsFilter;
import com.github.hanshsieh.pixivj.model.SearchedIllusts;
import com.github.hanshsieh.pixivj.model.SearchedIllustsFilter;
import com.github.hanshsieh.pixivj.token.TokenProvider;
import com.github.hanshsieh.pixivj.http.Header;
import com.github.hanshsieh.pixivj.util.IoUtils;
import com.github.hanshsieh.pixivj.util.QueryParamConverter;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.Validate;
import org.checkerframework.checker.nullness.qual.NonNull;


/**
 * Client for Pixiv API.
 */
public class PixivApiClient implements Closeable {

  /**
   * Builder for {@link PixivApiClient}.
   */
  public static class Builder {
    public static final String DEFAULT_BASE_URL = "https://app-api.pixiv.net";
    private String baseUrl = DEFAULT_BASE_URL;
    private String userAgent = Header.USER_AGENT_ANDROID;
    private TokenProvider tokenProvider = null;

    /**
     * Sets the base URL of the service.
     * Default: {@link #DEFAULT_BASE_URL}.
     * @param baseUrl Base URL.
     * @return This instance.
     */
    @NonNull
    public Builder setBaseUrl(@NonNull String baseUrl) {
      this.baseUrl = baseUrl;
      return this;
    }

    /**
     * Sets the user agent used when sending requests.
     * Default: {@link Header#USER_AGENT_ANDROID}
     * @param userAgent User agent.
     * @return This instance.
     */
    @NonNull
    public Builder setUserAgent(@NonNull String userAgent) {
      this.userAgent = userAgent;
      return this;
    }

    /**
     * Sets the provider for the access token.
     * The created {@link PixivApiClient} will take ownership of the token provider, which means
     * when the {@link PixivApiClient} is closed, the token provider will also be closed.
     * @param tokenProvider Token provider.
     * @return This instance.
     */
    @NonNull
    public Builder setTokenProvider(@NonNull TokenProvider tokenProvider) {
      this.tokenProvider = tokenProvider;
      return this;
    }

    /**
     * Builds the {@link PixivApiClient} instance.
     * @return Built instance.
     */
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
    return sendGetRequest("v1/illust/ranking", filter, RankedIllusts.class);
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
    return sendGetRequest("v1/illust/recommended", filter, RecommendedIllusts.class);
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
  public SearchedIllusts searchIllusts(@NonNull SearchedIllustsFilter filter)
      throws PixivException, IOException {
    return sendGetRequest("v1/search/illust", filter, SearchedIllusts.class);
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

  /**
   * Fetches the content of the given URL as stream.
   * It is often used for downloading the image of an illustration.
   * It is caller's responsibility to close the returned response object.
   * If the status code is not within the range [200, 300), {@link PixivException} is thrown.
   * @param url - URL.
   * @return Response of downloading the resource.
   */
  @NonNull
  public Response download(@NonNull String url) throws PixivException, IOException {
    Request request = new Request.Builder()
        .header("User-Agent", userAgent)
        .header("Referer", baseUrl.toString())
        .header("Accept-Encoding", "gzip")
        .url(url)
        .get()
        .build();
    Response response = httpClient.newCall(request).execute();
    if (!response.isSuccessful()) {
      int code = response.code();
      response.close();
      throw new PixivException("Fail to download. Status code: " + code);
    }
    return response;
  }

  /**
   * Sends a HTTP GET request.
   * @param path The relative URL path.
   * @param filter The filter to used to generate the query parameters.
   * @param respType Type of the response.
   * @param <T> Type of the serialized response.
   * @param <F> Type of the filter.
   * @return Serialized response.
   * @throws PixivException The server returns an error.
   * @throws IOException IO error.
   */
  private <T, F> T sendGetRequest(@NonNull String path, @NonNull F filter, Class<T> respType)
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

  /**
   * Creates an API request builder.
   * @return Created API request builder.
   * @throws AuthException Authentication error when obtaining the access token.
   * @throws IOException IO error.
   */
  private Request.@NonNull Builder createApiReqBuilder() throws AuthException, IOException {
    return new Request.Builder()
        .header("User-Agent", userAgent)
        .header("Authorization", "Bearer " + tokenProvider.getAccessToken());
  }

  /**
   * Closes the client and release the resources.
   * @throws IOException IO error.
   */
  @Override
  public void close() throws IOException {
    this.tokenProvider.close();
    IoUtils.close(this.httpClient);
  }
}
