package github.com.stormcc.thread;

import github.com.stormcc.util.LogExceptionStackUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


/**
 * Create By: Jimmy Song
 * Create At: 2023-02-25 22:20
 */
@Slf4j
public class MyAppThreadTest {
    private static final int  DEFAULT_CORE_POOL_SIZE = 3;
    private static final  int  DEFAULT_MAXIMUM_POOL_SIZE = 4;
    private static final  int  DEFAULT_WORK_QUEUE = 256;
    private static final long DEFAULT_KEEP_ALIVE_TIME_MICROSECONDS = 8_000L;
    private static final int loop = 32;
    @Test
    public void test(){
        CountDownLatch countDownLatch = new CountDownLatch(loop );
        ThreadFactory threadFactory = new MyThreadFactory("threadPool-=-");

        ExecutorService executorService = new MyThreadPoolExecutor(DEFAULT_CORE_POOL_SIZE, DEFAULT_MAXIMUM_POOL_SIZE,
                DEFAULT_KEEP_ALIVE_TIME_MICROSECONDS, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(DEFAULT_WORK_QUEUE), threadFactory, new ThreadPoolExecutor.AbortPolicy());
        List<Future> futureList = new ArrayList<>();
        for (int i = 0; i < loop; i++) {
            Future future = executorService.submit(() -> {
                log.info("ok");
                TimeUnit.SECONDS.sleep(1);
                countDownLatch.countDown();
                throw new RuntimeException("test");
            });
            futureList.add(future);
        }
        for (Future future : futureList) {
            try {
                future.get(2, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                log.error("exception is:{}", LogExceptionStackUtil.logExceptionStack(e));
            }
        }

        try {
            countDownLatch.await(3L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("InterruptedException");
        }
        executorService.shutdown();
    }

}