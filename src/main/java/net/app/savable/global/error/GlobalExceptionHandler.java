package net.app.savable.global.error;

import jakarta.persistence.EntityNotFoundException;
import net.app.savable.global.error.exception.ErrorCode;
import net.app.savable.global.error.exception.GeneralException;
import net.app.savable.global.error.exception.InvalidSocialIdException;
import net.app.savable.global.error.exception.SessionMemberNotFoundException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice(annotations = {RestController.class})
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<Object> validation(ConstraintViolationException e, WebRequest request) {
        return handleExceptionInternal(e, ErrorCode.BAD_REQUEST, request);
    }

    @ExceptionHandler
    public ResponseEntity<Object> general(GeneralException e, WebRequest request) {
        return handleExceptionInternal(e, e.getErrorCode(), request);
    }

    @ExceptionHandler
    public ResponseEntity<Object> exception(Exception e, WebRequest request) {
        return handleExceptionInternal(e, ErrorCode.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(SessionMemberNotFoundException.class)
    public ResponseEntity<Object> handleSessionMemberEmptyException(SessionMemberNotFoundException e, WebRequest request) {
        return handleExceptionInternal(e, ErrorCode.SESSION_MEMBER_NOT_FOUND, request);
    }

    @ExceptionHandler({InvalidSocialIdException.class, IllegalArgumentException.class})
    public ResponseEntity<Object> handleIllegalArgumentException(InvalidSocialIdException e, WebRequest request) {
        return handleExceptionInternal(e, ErrorCode.INVALID_INPUT_VALUE, request);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException e, WebRequest request) {
        return handleExceptionInternal(e, ErrorCode.NOT_FOUND, request);
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
