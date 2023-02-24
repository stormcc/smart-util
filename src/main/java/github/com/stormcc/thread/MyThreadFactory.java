package github.com.stormcc.thread;

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
        log.debug("create new Thread......");
        return new MyAppThread(r, poolName);
    }
}
