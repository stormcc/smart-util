package github.com.stormcc.exception;

/**
 * Create By: Jimmy Song
 * Create At: 2023-02-22 16:44
 */
public class IncompleteException extends RuntimeException {

    public IncompleteException(String message){
        super(message);
    }

    public IncompleteException(String message, Throwable cause) {
        super(message, cause);
    }
}
