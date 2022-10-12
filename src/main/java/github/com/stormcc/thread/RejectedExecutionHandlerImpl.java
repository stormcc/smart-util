package github.com.stormcc.thread;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import github.com.stormcc.util.JacksonUtil;
import github.com.stormcc.util.LogExceptionStackUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Create By: Jimmy Song
 * Create At: 2022-09-27 21:05
 */
@Slf4j
public class RejectedExecutionHandlerImpl implements RejectedExecutionHandler {
    /**
     * 【核心方法】从FutureTask获取原始的任务对象
     * @param futureTask
     */
    private <T> T getOriWork(Object futureTask) {
        boolean isFutureTask = futureTask instanceof FutureTask;
        if (!isFutureTask) {
            ObjectMapper objectMapper =  JacksonUtil.serializeObjectMapper();
            String s = null;
            try {
                s = objectMapper.writeValueAsString(futureTask);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(LogExceptionStackUtil.logExceptionStack(e));
            }
            throw new RuntimeException("不是FutureTask 无法获取原始任务："+s);
        }

        try {
            // 获取 FutureTask.callable
            Field callableField = FutureTask.class.getDeclaredField("callable");
            callableField.setAccessible(true);
            Object callableObj = callableField.get(futureTask);

            // 获取 上一步callable的数据类型：Executors的内部类RunnableAdapter
            Class<?>[] classes = Executors.class.getDeclaredClasses();
            Class tarClass = null;
            for (Class<?> cls : classes) {
                if (cls.getName().equals("java.util.concurrent.Executors$RunnableAdapter")) {
                    tarClass = cls;
                    break;
                }
            }

            // 获取原始任务对象
            Field taskField = tarClass.getDeclaredField("task");
            taskField.setAccessible(true);

            return  (T)taskField.get(callableObj);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("从FutureTask获取原始任务对象失败:"+ LogExceptionStackUtil.logExceptionStack(e));
        }
    }

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        // 获取原始任务对象
        MyAppThread oriWork = getOriWork(r);
        log.error("拒绝任务 id is {}.", oriWork.getId());
    }
}
