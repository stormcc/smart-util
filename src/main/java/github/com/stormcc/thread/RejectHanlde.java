package github.com.stormcc.thread;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.concurrent.Callable;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/** 存在问题
 * Create By: Jimmy Song
 * Create At: 2022-09-28 09:09
 */
@Slf4j
public class RejectHanlde implements RejectedExecutionHandler {
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor){
        Field field= null;
        try {
            field = r.getClass().getDeclaredField("callable");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        field.setAccessible(true);
        Callable callable= null;
        try {
            callable = (Callable)field.get(r);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        Field invocationField= null;
        try {
            invocationField = callable.getClass().getDeclaredField("arg$2");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        invocationField.setAccessible(true);
//        MethodInvocation invocation= null;
//        try {
//            invocation = (MethodInvocation)invocationField.get(callable);
//        } catch (IllegalAccessException e) {
//            throw new RuntimeException(e);
//        }
//        Object[] args=invocation.getArguments();
//        BusinessObject object=(BusinessObject)args[0];
//        log.info("拒绝策略拒绝任务:{}",new ObjectMapper().writeValueAsString(object));
        //可以用LinkedBlokingQueue的put方法阻塞入队
        //executor.getQueue().put(r);
    }

}
