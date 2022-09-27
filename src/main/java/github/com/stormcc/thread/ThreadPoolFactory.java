package github.com.stormcc.thread;



import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class ThreadPoolFactory {
    private ThreadPoolFactory(){}
    private static final int  DEFAULT_CORE_POOL_SIZE = 64;
    private static final int  DEFAULT_MAXIMUM_POOL_SIZE = 512;
    private static final int  DEFAULT_WORK_QUEUE = 1024;
    private static final long DEFAULT_KEEP_ALIVE_TIME_MILLISECONDS = 60000L;


    public static ThreadPoolExecutor threadPoolExecutor(String namePrefix){
        return  new ThreadPoolExecutor(DEFAULT_CORE_POOL_SIZE, DEFAULT_MAXIMUM_POOL_SIZE,
                DEFAULT_KEEP_ALIVE_TIME_MILLISECONDS, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(DEFAULT_WORK_QUEUE),
                new MyThreadFactory(namePrefix),
                new ThreadPoolExecutor.AbortPolicy());
    }



}
