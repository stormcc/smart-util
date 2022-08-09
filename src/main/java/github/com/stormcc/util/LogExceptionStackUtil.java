package github.com.stormcc.util;

import java.io.PrintWriter;
import java.io.StringWriter;


public final class LogExceptionStackUtil {
    private LogExceptionStackUtil(){}

    public static String logExceptionStack(Throwable e) {
        StringWriter errorsWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(errorsWriter));
        return errorsWriter.toString();
    }
}
