package github.com.stormcc.exception;

/**
 * Create By: Jimmy Song
 * Create At: 2023-02-09 20:21
 */
public class HasNotLogInException extends RuntimeException {

    public HasNotLogInException(String message){
        super(message);
    }

    public HasNotLogInException(String message, Throwable cause) {
        super(message, cause);
    }
}
