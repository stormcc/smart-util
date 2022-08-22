package github.com.stormcc.exception;

/**
 * Create By: Jimmy Song
 * Create At: 2022-08-09 13:17
 */
public class ApplicationException extends RuntimeException {
    public ApplicationException(){
        super();
    }

    public ApplicationException(String message){
        super(message);
    }

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
