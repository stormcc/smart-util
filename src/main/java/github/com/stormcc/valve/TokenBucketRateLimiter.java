package github.com.stormcc.valve;

/**
 * Create By: Jimmy Song
 * Create At: 2024-06-12 09:55
 */
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class TokenBucketRateLimiter {
    private final long refillIntervalNanos; // 令牌填充间隔时间（纳秒）
    private final long tokensPerInterval; // 每个时间间隔填充的令牌数
    private long capacity = 0;
    private final AtomicLong availableTokens; // 当前可用的令牌数
    private final ScheduledExecutorService scheduler; // 用于定时任务的线程池
    private final AtomicLong lastRefillTimestamp; // 上次填充令牌的时间戳

    public TokenBucketRateLimiter(long rate, long capacity, long refillIntervalNanos) {
        this.tokensPerInterval = rate;
        this.refillIntervalNanos = refillIntervalNanos;
        this.availableTokens = new AtomicLong(capacity);
        this.capacity = capacity;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.lastRefillTimestamp = new AtomicLong(System.nanoTime());
        scheduler.schedule(this::refillTokens, refillIntervalNanos, TimeUnit.NANOSECONDS);
    }

    private void refillTokens() {
        long now = System.nanoTime();
        long elapsed = now - lastRefillTimestamp.get();
        long tokensToRefill = (elapsed / refillIntervalNanos) * tokensPerInterval;
        if (tokensToRefill > 0) {
            if (tokensToRefill> capacity) {
//                long newTokens = Math.min(capacity, availableTokens.addAndGet(tokensToRefill));
                long value = availableTokens.get() + capacity;
                if (value> capacity) {
                    availableTokens.getAndSet(capacity);
                }else {
                    availableTokens.addAndGet(value);
                }
            } else {
                long newTokens = Math.min(capacity, availableTokens.addAndGet(tokensToRefill));
            }
            lastRefillTimestamp.set(now - (elapsed % refillIntervalNanos));
        }
        log.info("done. tokensToRefill = {}, availableTokens:{}", tokensToRefill, availableTokens.get());
    }

//    private void refillTokens() {
//        while (!Thread.currentThread().isInterrupted()) {
//            long now = System.nanoTime();
//            long elapsed = now - lastRefillTimestamp.get();
//            long tokensToRefill = (elapsed / refillInterval) * tokensPerInterval;
//            if (tokensToRefill > 0) {
//                long newTokens = Math.min(capacity, availableTokens.addAndGet(tokensToRefill));
//                lastRefillTimestamp.set(now - (elapsed % refillInterval));
//                if (newTokens < tokensToRefill) {
//                    // 如果桶已满，下次填充将不会添加更多令牌
//                    break;
//                }
//            }
//            try {
//                TimeUnit.NANOSECONDS.sleep(refillInterval);
//            } catch (InterruptedException e) {
//                log.error("InterruptedException is:{}", LogExceptionStackUtil.logExceptionStack(e));
////                Thread.currentThread().interrupt();
//                break;
//            }
//        }
//    }

    public boolean tryAcquire() {
        while (true) {
            long available = availableTokens.get();
            if (available == 0) {
                return false; // 没有可用令牌，请求被限流
            }
            if (availableTokens.compareAndSet(available, available - 1)) {
                return true; // 成功获取一个令牌
            }
        }
    }

    public void shutdown() {
        scheduler.shutdown();
    }

    public static void main(String[] args) {
        // 每个时间间隔填充的令牌数
        long rate = 5;
        long capacity = 10;
        long refillIntervalNanos = 1000;
        log.info("rate:{}, capacity:{}, refillIntervalNanos is:{}", rate, capacity, refillIntervalNanos);
//        final TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(5, 10, TimeUnit.SECONDS.toNanos(1));
//        final TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(rate, capacity, TimeUnit.MILLISECONDS.toNanos(1));
        final TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(rate, capacity, 1000);
        AtomicLong allowedCount = new AtomicLong(0);
        AtomicLong rejectedCount = new AtomicLong(0);
        int num = 20;
        CountDownLatch countDownLatch = new CountDownLatch(num);
        long startTime = System.nanoTime();
        // 模拟请求
        for (int i = 0; i < num; i++) {
            final int i1 = i;
            Thread thread = new Thread(() -> {
                long now=0;
                try {
                    now = System.nanoTime();
                    for (int j = 0; j < 100; j++) {
                        boolean allowed = limiter.tryAcquire();
                        if (allowed) {
                            allowedCount.incrementAndGet();
//                            log.info("request " + j + " allowed");
                        } else {
                            rejectedCount.incrementAndGet();
//                            log.warn("request " + j + " rejected");
                        }
                        try {
                            TimeUnit.MILLISECONDS.sleep(1);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } finally {
                    countDownLatch.countDown();
                    long cost = System.nanoTime() - now;
                    log.info("cost:{} ns, cost:{} ms.", cost, cost/1000_000);
                }
            });
            thread.setName("Thread:::"+i1);
            thread.start();
        }

        try {
            countDownLatch.await(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        long elapsed = System.nanoTime() - startTime;
        log.info("allowedCount is:{}, rejectedCount is:{}, it cost:{} ns, {} ms", allowedCount.get(), rejectedCount.get(), elapsed, elapsed/1000_000);
        limiter.shutdown();
    }
}