package pub.tbc.toolkit.ratelimit;

/**
 * @Author tbc by 2021/1/7 13:29
 */
public abstract class TokenBucketRateLimiter implements RateLimiter {
    /**
     * token 容量
     */
    private double maxToken;

    private double stableIntervalMicros;

    public static void main(String[] args) {
        // com.google.common.util.concurrent.RateLimiter.create()
    }


}
