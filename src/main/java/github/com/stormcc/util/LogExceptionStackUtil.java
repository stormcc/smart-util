package github.com.stormcc.util;

import java.io.PrintWriter;
import java.io.StringWriter;


public class LogExceptionStackUtil {

    public static String logExceptionStack(Throwable e) {
        StringWriter errorsWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(errorsWriter));
        return errorsWriter.toString();
    }


}
