package github.com.stormcc.exception;

/**
 * Create By: Jimmy Song
 * Create At: 2023-02-09 20:21
 */
public class UploadFileException extends RuntimeException {

    public UploadFileException(String message){
        super(message);
    }

    public UploadFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
