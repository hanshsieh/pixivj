package com.github.hanshsieh.pixivj.token;

import com.github.hanshsieh.pixivj.model.AuthResult;
import com.github.hanshsieh.pixivj.model.Credential;
import org.apache.commons.lang3.Validate;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * A token provider that refresh the access token in a background thread.
 */
public class ThreadedTokenProvider extends BaseTokenProvider {
    public static final double DEFAULT_DELAY_PERCENTAGE = 0.5;
    public static final Duration DEFAULT_RETRY_DELAY = Duration.ofSeconds(3);
    private static final Logger logger = LoggerFactory.getLogger(ThreadedTokenProvider.class);
    private final double delayPercentage;
    private final Duration retryDelay;
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor((Runnable runnable) -> {
        Thread thread = new Thread(runnable);
        thread.setDaemon(true);
        return thread;
    });
    private ScheduledFuture<?> refreshFuture;

    /**
     * Creates a new instance that will refresh the token when the life of the current token has exceeded the given
     * percentage.
     * For example, if 0.1 is given, at the remaining life time of the current token is 100 seconds, then it will
     * try to refresh the token after 10 seconds.
     * @param delayPercentage Percentage between 0.0 (exclusive) and 1.0 (exclusive).
     * @param retryDelay The delay between retries when refresh fails.
     */
    public ThreadedTokenProvider(double delayPercentage, @NonNull Duration retryDelay) {
        Validate.isTrue(delayPercentage < 1.0 && delayPercentage > 0.0);
        this.delayPercentage = delayPercentage;
        this.retryDelay = retryDelay;
    }

    /**
     * Creates a new instance that will refresh the token after half of the remaining lifetime, and if refresh fails,
     * retry after 3 seconds.
     */
    public ThreadedTokenProvider() {
        this(DEFAULT_DELAY_PERCENTAGE, DEFAULT_RETRY_DELAY);
    }

    @Override
    public void setTokens(
            @NonNull String accessToken,
            @NonNull String refreshToken,
            long expiresInSec
    ) {
        super.setTokens(accessToken, refreshToken, expiresInSec);
        long refreshDelayMs = (long) Math.floor(
                TimeUnit.MILLISECONDS.convert(expiresInSec, TimeUnit.SECONDS) * delayPercentage);
        replaceRefreshFuture(executor.schedule(this::refreshToken, refreshDelayMs, TimeUnit.MILLISECONDS));
    }

    private void refreshToken() {
        try {
            Credential credential = new Credential();
            credential.setRefreshToken(this.refreshToken);
            credential.setGrantType(Credential.GRANT_TYPE_REFRESH_TOKEN);
            AuthResult authResult = client.authenticate(credential);
            logger.debug("Tokens are refreshed");
            setTokens(authResult.getAccessToken(), authResult.getRefreshToken(), authResult.getExpiresIn());
        } catch (Exception ex) {
            logger.error("Failed to refresh token: ", ex);
            replaceRefreshFuture(executor.schedule(this::refreshToken, retryDelay.toMillis(), TimeUnit.MILLISECONDS));
        }
    }

    private synchronized void replaceRefreshFuture(@NonNull ScheduledFuture<?> newRefreshFuture) {
        if (refreshFuture != null) {
            refreshFuture.cancel(false);
        }
        refreshFuture = newRefreshFuture;
    }

    @Override
    public void close() {
        executor.shutdownNow();
    }
}
