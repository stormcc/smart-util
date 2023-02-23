package github.com.stormcc.exception;

/**
 * Create By: Jimmy Song
 * Create At: 2023-02-17 15:44
 */
public class ContentModifyException extends RuntimeException{
    public ContentModifyException(String message){
        super(message);
    }

    public ContentModifyException(String message, Throwable cause) {
        super(message, cause);
    }
}
