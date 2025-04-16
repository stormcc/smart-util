package github.com.stormcc.util;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Create By: Jimmy Song
 * Create At: 2025-03-07 09:54
 */
@Slf4j
public final class WorkTraceUtil {
    public static final String DATA_FORMAT = "yyyyMMdd_HHmmss_SSS";
    public static final String WORK_TRACE_ID = "Work-Trace-Id";
    public static final String WORK_FAILURE_ORIGINAL = "Work-Failure-Original";
    private static final String SLF4J_MDC_NAME_TRACE_ID= "traceId";

    private WorkTraceUtil(){}

    public static String generateWorkTraceId(){
        return new SimpleDateFormat(DATA_FORMAT).format(new Date());
    }

    public static long costTimeMillis(){
        String traceId = getTraceId();
        if (Strings.isNullOrEmpty(traceId)) {
            log.error("return -1, traceId is blank");
            return -1;
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATA_FORMAT);
            try {
                return System.currentTimeMillis() - simpleDateFormat.parse(traceId).getTime();
            } catch (ParseException e) {
                log.error("return -2, 数据处理异常,", e);
                return -2;
            }
        }
    }

    public static void setTraceId(String traceId){
        MDC.put(SLF4J_MDC_NAME_TRACE_ID, traceId);
    }

    public static void removeTraceId(){
        MDC.remove(SLF4J_MDC_NAME_TRACE_ID);
    }

    public static String getTraceId(){
        return MDC.get(SLF4J_MDC_NAME_TRACE_ID);
    }
}
