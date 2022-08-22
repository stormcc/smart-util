package github.com.stormcc.exception;

/**
 * Create By: Jimmy Song
 * Create At: 2022-08-09 13:16
 */
public class InputParameterException extends ApplicationException {
    public InputParameterException(){
        super();
    }

    public InputParameterException(String message){
        super(message);
    }

    public InputParameterException(String message, Throwable cause) {
        super(message, cause);
    }
}
