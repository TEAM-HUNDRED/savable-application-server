package net.app.savable.global.error.exception;

import lombok.Getter;

@Getter
public class GeneralException extends RuntimeException {

    private final ErrorCode errorCode;

    public GeneralException() {
        super(ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
        this.errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
    }

    public GeneralException(String message) {
        super(ErrorCode.INTERNAL_SERVER_ERROR.getMessage(message));
        this.errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
    }

    public GeneralException(String message, Throwable cause) {
        super(ErrorCode.INTERNAL_SERVER_ERROR.getMessage(message), cause);
        this.errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
    }

    public GeneralException(Throwable cause) {
        super(ErrorCode.INTERNAL_SERVER_ERROR.getMessage(cause));
        this.errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
    }

    public GeneralException(ErrorCode errorErrorcode) {
        super(errorErrorcode.getMessage());
        this.errorCode = errorErrorcode;
    }

    public GeneralException(ErrorCode errorErrorcode, String message) {
        super(errorErrorcode.getMessage(message));
        this.errorCode = errorErrorcode;
    }

    public GeneralException(ErrorCode errorErrorcode, String message, Throwable cause) {
        super(errorErrorcode.getMessage(message), cause);
        this.errorCode = errorErrorcode;
    }

    public GeneralException(ErrorCode errorErrorcode, Throwable cause) {
        super(errorErrorcode.getMessage(cause), cause);
        this.errorCode = errorErrorcode;
    }
}
