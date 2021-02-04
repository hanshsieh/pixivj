package com.github.pixivj;

import com.github.pixivj.model.*;
import okhttp3.*;
import org.checkerframework.checker.nullness.qual.NonNull;
import com.github.pixivj.exception.AuthException;
import com.github.pixivj.exception.PixivException;
import com.github.pixivj.token.ThreadedTokenProvider;
import com.github.pixivj.token.TokenProvider;
import com.github.pixivj.util.HexUtils;
import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class PixivClient implements Closeable {
  public static class Builder {
    public static final String USER_AGENT_ANDROID = "PixivAndroidApp/5.0.64 (Android 6.0)";
    public static final String USER_AGENT_IOS = "PixivIOSApp/7.6.2 (iOS 12.0.1; iPhone8,2)";
    private String authBaseUrl = "https://oauth.secure.pixiv.net";
    private String apiBaseUrl = "https://app-api.pixiv.net";
    private String userAgent = USER_AGENT_ANDROID;
    private TokenProvider tokenProvider = null;

    @NonNull
    public Builder setAuthBaseUrl(@NonNull String authBaseUrl) {
      this.authBaseUrl = authBaseUrl;
      return this;
    }

    @NonNull
    public Builder setApiBaseUrl(@NonNull String apiBaseUrl) {
      this.apiBaseUrl = apiBaseUrl;
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
    public PixivClient build() {
      return new PixivClient(this);
    }
  }
  private final HttpUrl apiBaseUrl;
  private final HttpUrl authBaseUrl;
  private final String userAgent;
  private final OkHttpClient httpClient;
  private final TokenProvider tokenProvider;
  private final QueryParamConverter queryParamConverter;
  private final AuthRequestSender authRequestSender;
  private final ApiRequestSender apiRequestSender;

  private PixivClient(@NonNull Builder builder) {
    this.apiBaseUrl = HttpUrl.parse(builder.apiBaseUrl);
    this.authBaseUrl = HttpUrl.parse(builder.authBaseUrl);
    this.userAgent = builder.userAgent;
    if (builder.tokenProvider == null) {
      this.tokenProvider = new ThreadedTokenProvider();
    } else {
      this.tokenProvider = builder.tokenProvider;
    }
    this.httpClient = new OkHttpClient.Builder()
      .followRedirects(false)
      .followSslRedirects(false)
      .build();
    this.queryParamConverter = new QueryParamConverter();
    this.tokenProvider.setClient(this);
    this.authRequestSender = new AuthRequestSender(httpClient);
    this.apiRequestSender = new ApiRequestSender(httpClient);
  }

  /**
   * Uses the given credential to authenticate with the server, saves the tokens obtained for future API calls.
   * @param credential Credential for authentication.
   * @return Authentication result.
   * @throws AuthException Authentication error.
   * @throws IOException IO error.
   */
  public AuthResult login(@NonNull Credential credential) throws AuthException, IOException {
    AuthResult result = authenticate(credential);
    tokenProvider.setTokens(
        result.getAccessToken(),
        result.getRefreshToken(),
        result.getExpiresIn());
    return result;
  }

  /**
   * Uses the given credential to authenticate with the server.
   * Different from {@link #login(Credential)}, the tokens obtained won't be saved.
   * @param credential Credential for authentication.
   * @return Authentication result.
   * @throws AuthException Authentication error.
   * @throws IOException IO error.
   */
  @NonNull
  public AuthResult authenticate(@NonNull Credential credential) throws AuthException, IOException {
    HttpUrl url = authBaseUrl.newBuilder()
        .addEncodedPathSegments("auth/token")
        .build();
    FormBody.Builder bodyBuilder = new FormBody.Builder()
        .add("client_id", credential.getClientId())
        .add("client_secret", credential.getClientSecret())
        .add("grant_type", credential.getGrantType());
    if (credential.getUsername() != null) {
      bodyBuilder.add("username", credential.getUsername());
    }
    if (credential.getPassword() != null) {
      bodyBuilder.add("password", credential.getPassword());
    }
    if (credential.getRefreshToken() != null) {
      bodyBuilder.add("refresh_token", credential.getRefreshToken());
    }
    FormBody formBody = bodyBuilder.build();
    ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(Instant.now().getEpochSecond()), ZoneOffset.UTC);
    String timeStr = DateTimeFormatter.ISO_ZONED_DATE_TIME.format(zonedDateTime);
    String signature = timeStr + credential.getHashSecret();
    String clientHash = md5Hex(signature.getBytes(StandardCharsets.UTF_8));
    Request request = new Request.Builder()
        .url(url)
        .header("User-Agent", userAgent)
        .header("X-Client-Time", timeStr)
        .header("X-Client-Hash", clientHash)
        .post(formBody)
        .build();
    return authRequestSender.send(request, AuthResult.class);
  }

  /**
   * Grabs a list of ranked illustrations.
   * @param filter the filter to use.
   * @return Ranked Illustrations.
   * @throws PixivException PixivException Error
   * @throws IOException IOException Error
   */
  @NonNull
  public RankedIllusts getRankedIllusts(@NonNull RankedIllustsFilter filter) throws PixivException, IOException {
    return sendQueryRequest("v1/illust/ranking", filter, RankedIllusts.class);
  }
  /**
   * Grabs a list of recommended illustrations.
   * @param filter the filter to use.
   * @return Recommended Illustrations.
   * @throws PixivException PixivException Error
   * @throws IOException IOException Error
   */
  @NonNull
  public RecommendIllusts getRecommendedIllusts(@NonNull RecommendedIllustsFilter filter) throws PixivException, IOException {
    return sendQueryRequest("v1/illust/recommended", filter, RecommendIllusts.class);
  }
  /**
   * Searches illustrations from Pixiv.
   * @param tag the tag to search.
   * @return Search Illustration Results.
   * @throws PixivException PixivException Error
   * @throws IOException IOException Error
   */
  @NonNull
  public SearchIllusts searchIllusts(@NonNull String tag, int offset) throws PixivException, IOException {
    HttpUrl url = apiBaseUrl.newBuilder().addEncodedPathSegments("v1/search/illust")
            .addQueryParameter("search_target", "partial_match_for_tags").addQueryParameter("sort", "date_desc")
            .addQueryParameter("word", tag).addQueryParameter("offset", String.valueOf(offset)).build();
    Request request = createApiReqBuilder().url(url).get().build();
    return apiRequestSender.send(request, SearchIllusts.class);
  }

  /**
   * Grabs the details of a certain illustration.
   * @param illustId the id of the illustration.
   * @return Illustration Detail.
   * @throws PixivException PixivException Error
   * @throws IOException IOException Error
   */
  @NonNull
  public IllustDetail getIllustDetail(long illustId) throws PixivException, IOException {
    HttpUrl url = apiBaseUrl.newBuilder()
        .addEncodedPathSegments("v1/illust/detail")
        .addQueryParameter("illust_id", String.valueOf(illustId))
        .build();
    Request request = createApiReqBuilder()
        .url(url)
        .get()
        .build();
    return apiRequestSender.send(request, IllustDetail.class);
  }

  private <T, F> T sendQueryRequest(@NonNull String path, @NonNull F filter, Class<T> respType) throws PixivException, IOException {
    HttpUrl.Builder urlBuilder = apiBaseUrl.newBuilder()
        .addEncodedPathSegments(path);
    queryParamConverter.toQueryParams(filter, urlBuilder);
    HttpUrl url = urlBuilder
        .build();
    Request request = createApiReqBuilder()
        .url(url)
        .get()
        .build();
    return apiRequestSender.send(request, respType);
  }

  private Request.@NonNull Builder createApiReqBuilder() throws AuthException, IOException {
    return new Request.Builder()
        .header("User-Agent", userAgent)
        .header("Authorization", "Bearer " + tokenProvider.getAccessToken());
  }

  @NonNull
  private String md5Hex(byte[] data) {
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      md.update(data);
      return HexUtils.bytesToHex(md.digest());
    } catch (NoSuchAlgorithmException ex) {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public void close() throws IOException {
    this.tokenProvider.close();
    this.httpClient.dispatcher().executorService().shutdown();
    this.httpClient.connectionPool().evictAll();
  }
}
