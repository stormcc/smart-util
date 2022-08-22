package github.com.stormcc.exception;

/**
 * Create By: Jimmy Song
 * Create At: 2022-08-09 13:16
 */
public class UnknownApplicationException extends ApplicationException {
    public UnknownApplicationException(){
        super();
    }

    public UnknownApplicationException(String message){
        super(message);
    }

    public UnknownApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
