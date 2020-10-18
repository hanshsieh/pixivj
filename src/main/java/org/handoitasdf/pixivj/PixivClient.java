package org.handoitasdf.pixivj;

import com.google.gson.Gson;
import okhttp3.*;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.units.qual.C;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class PixivClient {

  public static class Builder {
    public static final String USER_AGENT_ANDROID = "PixivAndroidApp/5.0.64 (Android 6.0)";
    public static final String USER_AGENT_IOS = "PixivIOSApp/7.6.2 (iOS 12.0.1; iPhone8,2)";
    private String authBaseUrl = "https://oauth.secure.pixiv.net";
    private String apiBaseUrl = "https://app-api.pixiv.net/v1";
    private String userAgent = USER_AGENT_ANDROID;

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
    public PixivClient build() {
      return new PixivClient(this);
    }
  }

  private static final MediaType FORM_URL_ENCODED = MediaType.get("application/x-www-form-urlencoded;charset=utf-8");
  private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
  private final HttpUrl apiBaseUrl;
  private final HttpUrl authBaseUrl;
  private final String userAgent;
  private final OkHttpClient httpClient;
  private final Gson gson;

  private PixivClient(@NonNull Builder builder) {
    this.gson = new Gson();
    this.apiBaseUrl = HttpUrl.parse(builder.apiBaseUrl);
    this.authBaseUrl = HttpUrl.parse(builder.authBaseUrl);
    this.userAgent = builder.userAgent;
    this.httpClient = new OkHttpClient.Builder()
      .followRedirects(false)
      .followSslRedirects(false)
      .build();
  }

  @NonNull
  public AuthTokens getAuthTokens(@NonNull Credential credential) throws AuthException, IOException {

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
        .method("POST", formBody)
        .build();
    try (Response response = httpClient.newCall(request).execute()) {
      ResponseBody respBody = response.body();
      assert respBody != null;
      String respBodyStr = respBody.string();
      if (!response.isSuccessful()) {
        AuthError authError = gson.fromJson(respBodyStr, AuthError.class);
        throw new AuthException(authError);
      } else {
        return gson.fromJson(respBodyStr, AuthTokens.class);
      }
    }
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
}
