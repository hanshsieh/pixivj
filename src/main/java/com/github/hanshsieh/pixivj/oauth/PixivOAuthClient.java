package com.github.hanshsieh.pixivj.oauth;

import com.github.hanshsieh.pixivj.exception.AuthException;
import com.github.hanshsieh.pixivj.model.AuthResult;
import com.github.hanshsieh.pixivj.model.Credential;
import com.github.hanshsieh.pixivj.util.Header;
import com.github.hanshsieh.pixivj.util.HexUtils;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.commons.lang3.Validate;
import org.checkerframework.checker.nullness.qual.NonNull;

public class PixivOAuthClient {

  public static class Builder {

    private String baseUrl = "https://oauth.secure.pixiv.net";
    private String userAgent = Header.USER_AGENT_ANDROID;

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
    public PixivOAuthClient build() {
      return new PixivOAuthClient(this);
    }
  }

  private final String userAgent;
  private final HttpUrl baseUrl;
  private final OkHttpClient httpClient;
  private final AuthRequestSender requestSender;

  private PixivOAuthClient(@NonNull Builder builder) {
    Validate.notNull(builder.baseUrl, "Base URL not set");
    Validate.notNull(builder.userAgent, "User agent not set");
    this.userAgent = builder.userAgent;
    this.baseUrl = HttpUrl.parse(builder.baseUrl);
    this.httpClient = new OkHttpClient.Builder()
        .followRedirects(false)
        .followSslRedirects(false)
        .build();
    this.requestSender = new AuthRequestSender(httpClient);
  }

  @NonNull
  public AuthResult authenticate(@NonNull Credential credential) throws AuthException, IOException {
    HttpUrl url = baseUrl.newBuilder()
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
    ZonedDateTime zonedDateTime = ZonedDateTime
        .ofInstant(Instant.ofEpochSecond(Instant.now().getEpochSecond()), ZoneOffset.UTC);
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
    return requestSender.send(request, AuthResult.class);
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
