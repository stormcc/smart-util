package github.com.stormcc.exception;

/**
 * Create By: Jimmy Song
 * Create At: 2023-02-17 15:44
 */
public class ExamineAndApproveException extends RuntimeException{
    public ExamineAndApproveException(String message){
        super(message);
    }

    public ExamineAndApproveException(String message, Throwable cause) {
        super(message, cause);
    }
}
