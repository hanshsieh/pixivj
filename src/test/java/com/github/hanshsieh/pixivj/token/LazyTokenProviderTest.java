package com.github.hanshsieh.pixivj.token;

import com.github.hanshsieh.pixivj.oauth.PixivOAuthClient;
import com.github.hanshsieh.pixivj.model.AuthResult;
import com.github.hanshsieh.pixivj.model.Credential;
import mockit.*;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LazyTokenProviderTest {

  @Injectable
  private PixivOAuthClient client;

  @Test
  public void testSetTokens() throws Exception {
    Instant[] now = new Instant[]{Instant.now()};
    new MockUp<Instant>() {
      @Mock
      Instant now() {
        return now[0];
      }
    };
    LazyTokenProvider tokenProvider = new LazyTokenProvider(client);
    tokenProvider.setTokens(
        "test_access_token",
        "test_refresh_token",
        1000);
    // Get the access token immediately
    assertEquals("test_access_token", tokenProvider.getAccessToken());

    // After 400 seconds
    now[0] = now[0].plusSeconds(400);
    assertEquals("test_access_token", tokenProvider.getAccessToken());

    // After 1 ms, it's near expiration
    now[0] = now[0].plusMillis(1);
    AuthResult authResult = new AuthResult();
    authResult.setExpiresIn(2000);
    authResult.setAccessToken("test_access_token2");
    authResult.setRefreshToken("test_refresh_token2");

    AuthResult authResult2 = new AuthResult();
    authResult2.setExpiresIn(3000);
    authResult2.setAccessToken("test_access_token3");
    authResult2.setRefreshToken("test_refresh_token3");
    new Expectations() {{
      client.authenticate(withInstanceOf(Credential.class));
      returns(authResult, authResult2);
    }};
    assertEquals("test_access_token2", tokenProvider.getAccessToken());
    new Verifications() {{
      Credential credential;
      client.authenticate(credential = withCapture());
      times = 1;
      Credential wantCredential = new Credential();
      wantCredential.setRefreshToken("test_refresh_token");
      wantCredential.setGrantType(Credential.GRANT_TYPE_REFRESH_TOKEN);
      assertEquals(wantCredential, credential);
    }};

    // After 1400 seconds
    now[0] = now[0].plusSeconds(1400);
    assertEquals("test_access_token2", tokenProvider.getAccessToken());

    // After 1 seconds, it's near expiration again
    now[0] = now[0].plusSeconds(1);
    assertEquals("test_access_token3", tokenProvider.getAccessToken());
    new Verifications() {{
      Credential credential;
      client.authenticate(credential = withCapture());
      times = 2;
      Credential wantCredential = new Credential();
      wantCredential.setRefreshToken("test_refresh_token2");
      wantCredential.setGrantType(Credential.GRANT_TYPE_REFRESH_TOKEN);
      assertEquals(wantCredential, credential);
    }};
  }
}
