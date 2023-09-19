package net.app.savable.global.common;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice(annotations = {RestController.class})
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler
    public ResponseEntity<Object> validation(ConstraintViolationException e, WebRequest request) {
        return handleExceptionInternal(e, ErrorCode.BAD_REQUEST, request);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    public ResponseEntity<Object> general(GeneralException e, WebRequest request) {
        return handleExceptionInternal(e, e.getErrorCode(), request);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    public ResponseEntity<Object> exception(Exception e, WebRequest request) {
        return handleExceptionInternal(e, ErrorCode.INTERNAL_SERVER_ERROR, request);
    }

    protected ResponseEntity<Object> handleExceptionInternal(Exception e, Object body,
                                                             HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (body == null) {
            return handleExceptionInternal(e, ErrorCode.INTERNAL_SERVER_ERROR, headers, status,
                    request);
        }
        return super.handleExceptionInternal(e, body, headers, status, request);
    }

    private ResponseEntity<Object> handleExceptionInternal(Exception e, ErrorCode errorCode,
                                                           WebRequest request) {
        return handleExceptionInternal(e, errorCode, HttpHeaders.EMPTY, errorCode.getHttpStatus(),
                request);
    }

    private ResponseEntity<Object> handleExceptionInternal(Exception e, ErrorCode errorCode,
                                                           HttpHeaders headers, HttpStatus status, WebRequest request) {
        return super.handleExceptionInternal(
                e,
                ApiResponse.fail(errorCode, errorCode.getMessage(e)),
                headers,
                status,
                request
        );
    }
}
