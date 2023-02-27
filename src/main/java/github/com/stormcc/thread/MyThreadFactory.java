package github.com.stormcc.thread;

import github.com.stormcc.util.LogExceptionStackUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadFactory;

/**
 * Create By: Jimmy Song
 * Create At: 2022-08-21 10:34
 */
@Slf4j
public class MyThreadFactory implements ThreadFactory {
    private final String poolName ;

    public MyThreadFactory(String poolName) {
        this.poolName = poolName;
    }

    @Override
    public Thread newThread(@NotNull Runnable r) {
        System.out.println("new thread....");
        log.info("create new Thread......");
        MyAppThread myAppThread = new MyAppThread(r, poolName);
        myAppThread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.out.println("uncaughtException");
                log.error("Thread id is:{}, Name is:{}, Throwable is:{}",
                        t.getId(), t.getName(), LogExceptionStackUtil.logExceptionStack(e));
            }
        });
        return myAppThread;
    }
}
