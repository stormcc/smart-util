package github.com.stormcc.thread;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Create By: Jimmy Song
 * Create At: 2022-09-27 21:25
 */
@Slf4j
public class ThreadPoolFactoryTest {

    private void sleep(long seconds){
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void threadPoolExecutor() {
        ThreadPoolExecutor threadPoolExecutor = ThreadPoolFactory.threadPoolExecutor("test");
        for (long i = 0; i < 128; i++) {
            final long second = i;
            threadPoolExecutor.submit(()->{
                sleep(second);
            });
        }
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(4);
        scheduledThreadPoolExecutor.schedule(new Runnable() {
            @Override
            public void run() {
                log.info("hello.");
            }
        }, 10000L, TimeUnit.MILLISECONDS);
    }
}