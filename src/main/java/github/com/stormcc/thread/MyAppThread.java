package github.com.stormcc.thread;

import github.com.stormcc.exception.UnknownApplicationException;
import github.com.stormcc.util.LogExceptionStackUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Create By: Jimmy Song
 * Create At: 2022-08-21 10:14
 */
@Slf4j
public class MyAppThread extends Thread{
    public static final String DEFAULT_NAME = "myAppThread";
    private static volatile boolean debugLifecycle = false;
    private static final AtomicInteger createNumber = new AtomicInteger(0);
    private static final AtomicInteger aliveNumber = new AtomicInteger(0);

    public MyAppThread(Runnable r) {
        this(r, DEFAULT_NAME);
    }

    public MyAppThread(Runnable runnable, String name) {
        super(runnable, name+"-"+createNumber.incrementAndGet());
        setUncaughtExceptionHandler(
                (t, e) -> {
                   log.error("SEVERE uncaught in thread:{}, Exception is:{}", t.getName(), LogExceptionStackUtil.logExceptionStack(e));
                   throw new UnknownApplicationException("未知且未处理错误", e);
                }
        );
    }

    @Override
    public void run() {
        boolean debug = debugLifecycle;
        if (debug) {
            log.debug("Create MyAppThread:"+getName());
        }
        try {
            aliveNumber.incrementAndGet();
            super.run();
        } finally {
            aliveNumber.decrementAndGet();
            if (debug) {
                log.debug("Exiting "+ getName());
            }
        }
    }

    public static int getThreadsCreated(){
        return createNumber.get();
    }

    public static int getThreadAlive(){
        return aliveNumber.get();
    }

    public static boolean getDebug(){
        return debugLifecycle;
    }

    public static void setDebug(boolean b){
        debugLifecycle=b;
    }
}
