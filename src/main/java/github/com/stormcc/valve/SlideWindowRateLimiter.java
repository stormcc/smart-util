package github.com.stormcc.valve;

import java.util.concurrent.Semaphore;

/**
 * Create By: Jimmy Song
 * Create At: 2024-06-12 09:28
 */
public class SlideWindowRateLimiter {
    private final int permits; // 允许的请求数
    private final long interval; // 时间窗口长度，单位为毫秒
    private long lastTimestamp = 0; // 上一次请求的时间戳
    private Semaphore semaphore; // 信号量，用于控制并发访问

    public SlideWindowRateLimiter(int permits, long interval) {
        this.permits = permits;
        this.interval = interval;
        this.semaphore = new Semaphore(permits, true);
    }

    public boolean acquire() throws InterruptedException {
        long now = System.currentTimeMillis();
        // 如果当前时间已经超过了时间窗口的结束时间，重置时间窗口
        if (now - lastTimestamp >= interval) {
            lastTimestamp = now;
            semaphore.release(permits); // 重置信号量
        }

        return semaphore.tryAcquire();
        // 尝试获取一个信号量
//        if (!semaphore.tryAcquire()) {
//            return false; // 如果获取失败，返回false，表示限流
//        }


        // 记录当前时间作为下一次检查的基准
//        lastTimestamp = now;
//        return true; // 成功获取信号量，表示未被限流
    }

}
