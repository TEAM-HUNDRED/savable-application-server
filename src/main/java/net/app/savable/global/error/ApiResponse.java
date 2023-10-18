package net.app.savable.global.error;

import io.sentry.Sentry;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.app.savable.global.error.exception.ErrorCode;
import org.springframework.http.HttpStatus;

@Slf4j
@Getter
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private int code;
    private HttpStatus status;
    private String message;

    public static <T> ApiResponse<T> fail(ErrorCode errorCode) {
        log.info("ApiResponse.fail: {}", errorCode.getMessage());
        Sentry.captureMessage(errorCode.getMessage());
        ApiResponse<T> response = new ApiResponse<>();
        response.success = false;
        response.code = errorCode.getCode();
        response.status = errorCode.getHttpStatus();
        response.message = errorCode.getMessage();
        return response;
    }

    public static <T> ApiResponse<T> fail(ErrorCode errorCode, String message) {
        log.info("ApiResponse.fail: {}", message);
        Sentry.captureMessage(message);
        ApiResponse<T> response = new ApiResponse<>();
        response.success = false;
        response.code = errorCode.getCode();
        response.status = errorCode.getHttpStatus();
        response.message = message;
        return response;
    }

    public static <T> ApiResponse<T> success(T data) {
        log.info("ApiResponse.success: {}", data);
        ApiResponse<T> response = new ApiResponse<>();
        response.success = true;
        response.data = data;
        response.code = ErrorCode.OK.getCode();
        response.status = ErrorCode.OK.getHttpStatus();
        response.message = ErrorCode.OK.getMessage();
        return response;
    }
}
