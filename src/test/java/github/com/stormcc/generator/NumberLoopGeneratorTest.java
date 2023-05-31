package github.com.stormcc.generator;

import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;


/**
 * Create By: Jimmy Song
 * Create At: 2023-05-31 17:08
 */
public class NumberLoopGeneratorTest {
    private static final int THREAD_NUMBER = 100;
    private static final int TEST_NUMBER = 2000_000;
    private static final int RANGE_START = 10;
    private static final int RANGE_END = 19;

    @Test
    public void newNumber() {
        ConcurrentHashMap<Integer, AtomicLong> map = generateConcurrentHashMap();
        NumberLoopGenerator loopGenerator = new NumberLoopGenerator(RANGE_START,RANGE_END);
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_NUMBER);
        CountDownLatch countDownLatch = new CountDownLatch(THREAD_NUMBER);
        for (int i = 0; i < THREAD_NUMBER; i++) {
            executorService.execute(()->{
                try {
                    for (int j = 0; j < TEST_NUMBER; j++) {
                        int n = loopGenerator.newNumber();
                        map.get(n).incrementAndGet();
                    }
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        executorService.shutdownNow();
        AtomicLong n = new AtomicLong(0);
        map.forEach((k,v)-> {
            System.out.println("k:"+k+", v:"+v);
            n.getAndAdd(v.get());
        });

        System.out.println("total test number is:"+n.get());
    }

    private  ConcurrentHashMap<Integer, AtomicLong> generateConcurrentHashMap() {
        ConcurrentHashMap<Integer, AtomicLong> map = new ConcurrentHashMap<>();
        for (int i = 10; i <= 19; i++) {
            map.put(i, new AtomicLong(0L));
        }
        return map;
    }
}