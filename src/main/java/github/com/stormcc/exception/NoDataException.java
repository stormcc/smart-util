package github.com.stormcc.exception;

/**
 * Create By: Jimmy Song
 * Create At: 2023-02-09 20:21
 */
public class NoDataException extends RuntimeException {

    public NoDataException(String message){
        super(message);
    }

    public NoDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
