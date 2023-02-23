package github.com.stormcc.exception;

/**
 * Create By: Jimmy Song
 * Create At: 2023-02-09 20:21
 */
public class InvalidInputParameterException extends RuntimeException {

    public InvalidInputParameterException(String message){
        super(message);
    }

    public InvalidInputParameterException(String message, Throwable cause) {
        super(message, cause);
    }
}
