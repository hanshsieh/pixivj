package com.github.hanshsieh.pixivj.token;

import mockit.Mock;
import mockit.MockUp;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BaseTokenProviderTest {
    @Test
    public void testSetAndGetTokens() {
        Instant now = Instant.ofEpochMilli(10000000);
        new MockUp<Instant>() {
            @Mock
            Instant now() {
                return now;
            }
        };
        BaseTokenProvider tokenProvider = new BaseTokenProvider();
        tokenProvider.setTokens("test_access_token", "test_refresh_token", 100);
        assertEquals("test_access_token", tokenProvider.accessToken);
        assertEquals("test_refresh_token", tokenProvider.refreshToken);
        assertEquals(10100000L, tokenProvider.expireTime.toEpochMilli());
        tokenProvider.close();
    }
}
