package github.com.stormcc.exception;

/**
 * Create By: Jimmy Song
 * Create At: 2023-02-09 20:21
 */
public class HasNoPrivilegeException extends RuntimeException {

    public HasNoPrivilegeException(String message){
        super(message);
    }

    public HasNoPrivilegeException(String message, Throwable cause) {
        super(message, cause);
    }
}
