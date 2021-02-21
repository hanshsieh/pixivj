package com.github.hanshsieh.pixivj.token;

import com.github.hanshsieh.pixivj.PixivClient;
import com.github.hanshsieh.pixivj.model.AuthResult;
import com.github.hanshsieh.pixivj.model.Credential;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Verifications;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

public class ThreadedTokenProviderTest {

    @Injectable
    private ScheduledExecutorService executor;
    @Injectable
    private ScheduledFuture<?> future1, future2, future3;
    @Injectable
    private PixivClient client;
    @Mocked
    private Executors executors;
    private ThreadedTokenProvider tokenProvider;

    @BeforeEach
    public void beforeEach() {
        new Expectations(){{
            Executors.newSingleThreadScheduledExecutor(withInstanceOf(ThreadFactory.class));
            result = executor;
        }};
    }

    @Test
    @DisplayName("Constructor will create a single thread executor")
    public void testConstructor(@Injectable Runnable runnable) {
        tokenProvider = new ThreadedTokenProvider();
        List<ThreadFactory> threadFactories = new ArrayList<>();
        new Verifications(){{
            ThreadFactory threadFactory;
            Executors.newSingleThreadScheduledExecutor(threadFactory = withCapture());
            times = 1;
            threadFactories.add(threadFactory);
        }};
        assertEquals(1, threadFactories.size());
        Thread gotThread = threadFactories.get(0).newThread(runnable);
        assertTrue(gotThread.isDaemon());
    }

    @ParameterizedTest(name = "Delay percentage {0} is invalid")
    @ValueSource(doubles = { 0.0, -0.1, 1.0, 1.1 })
    @DisplayName("Construct with invalid delay percentage")
    public void testConstructWithInvalidDelayPercentage(double delayPercentage) {
        assertThrows(IllegalArgumentException.class, () -> new ThreadedTokenProvider(delayPercentage, Duration.ofSeconds(10)));
    }

    @ParameterizedTest(name = "Delay percentage {0} is valid")
    @ValueSource(doubles = { 0.1, 0.99 })
    @DisplayName("Construct with valid delay percentage")
    public void testConstructWithValidDelayPercentage(double delayPercentage) {
        assertDoesNotThrow(() -> new ThreadedTokenProvider(delayPercentage, Duration.ofSeconds(10)));
    }

    @Test
    @DisplayName("Set tokens and schedule for refresh")
    public void testSetTokens() throws Exception {
        tokenProvider = new ThreadedTokenProvider();
        new Expectations(){{
            executor.schedule(withInstanceOf(Runnable.class), anyLong, withInstanceOf(TimeUnit.class));
            returns(future1, future2, future3);
        }};
        tokenProvider.setClient(client);
        // Set the token, and is scheduled to refresh
        tokenProvider.setTokens("test_access_token", "test_refresh_token", 100);
        assertEquals("test_access_token", tokenProvider.getAccessToken());
        List<Runnable> runnables = new ArrayList<>();
        new Verifications() {{
            Runnable runnable;
            executor.schedule(runnable = withCapture(), 50000, TimeUnit.MILLISECONDS);
            times = 1;
            runnables.add(runnable);
        }};

        // Scheduled refresh is run, and fail
        new Expectations() {{
            client.authenticate(withInstanceOf(Credential.class));
            result = new IOException();
        }};
        runnables.get(0).run();
        runnables.clear();
        new Verifications() {{
            Credential credential;
            client.authenticate(credential = withCapture());
            times = 1;
            assertEquals("test_refresh_token", credential.getRefreshToken());
            assertEquals(Credential.GRANT_TYPE_REFRESH_TOKEN, credential.getGrantType());

            Runnable runnable;
            executor.schedule(runnable = withCapture(), 3000, TimeUnit.MILLISECONDS);
            times = 1;
            runnables.add(runnable);

            future1.cancel(false);
            times = 1;

            future2.cancel(anyBoolean);
            times = 0;
        }};

        // Refresh is retried after a while
        AuthResult authResult = new AuthResult();
        authResult.setAccessToken("test_access_token2");
        authResult.setRefreshToken("test_refresh_token2");
        authResult.setExpiresIn(200);
        new Expectations() {{
            client.authenticate(withInstanceOf(Credential.class));
            result = authResult;
        }};
        runnables.get(0).run();
        new Verifications() {{
            client.authenticate(withInstanceOf(Credential.class));
            times = 1;

            executor.schedule(withInstanceOf(Runnable.class), 100000, TimeUnit.MILLISECONDS);
            times = 1;

            future2.cancel(false);
            times = 1;

            future3.cancel(anyBoolean);
            times = 0;
        }};
        assertEquals("test_access_token2", tokenProvider.getAccessToken());
    }

    @Test
    @DisplayName("Close")
    public void testClose() {
        tokenProvider = new ThreadedTokenProvider();
        tokenProvider.close();
        new Verifications(){{
            executor.shutdownNow();
            times = 1;
        }};
    }
}
