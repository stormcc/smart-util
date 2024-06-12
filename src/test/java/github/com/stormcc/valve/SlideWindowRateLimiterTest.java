package github.com.stormcc.valve;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Create By: Jimmy Song
 * Create At: 2024-06-12 09:44
 */
public class SlideWindowRateLimiterTest {

    @Test
    public void acquire() {
        SlideWindowRateLimiter rateLimiter = new SlideWindowRateLimiter(5, 1000); // 每秒最多5个请求

        // 模拟请求
        for (int i = 0; i < 30; i++) {
            try {
                if (rateLimiter.acquire()) {
                    System.out.println("Request " + (i + 1) + " allowed at " + System.currentTimeMillis());
                    // 模拟请求处理时间
//                    Thread.sleep(100);
                } else {
                    System.out.println("Request " + (i + 1) + " rejected at " + System.currentTimeMillis());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}