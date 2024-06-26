package net.app.savable.global.error.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

    // Common
    OK(200, HttpStatus.OK, "OK"),
    BAD_REQUEST(400, HttpStatus.BAD_REQUEST, "Bad Request"),
    INVALID_TYPE_VALUE(400, HttpStatus.BAD_REQUEST, "Invalid Type Value"),
    INVALID_INPUT_VALUE(400, HttpStatus.BAD_REQUEST, "Invalid Input Value"),
    INSUFFICIENT_BALANCE(400, HttpStatus.BAD_REQUEST, "Insufficient Balance"),
    DATA_INTEGRITY_VIOLATION(400, HttpStatus.BAD_REQUEST, "Data Integrity Violation"),

    UNAUTHORIZED(401, HttpStatus.UNAUTHORIZED, "Unauthorized"),
    SESSION_MEMBER_NOT_FOUND(401, HttpStatus.UNAUTHORIZED, "Session Member Not Found"),
    FORBIDDEN(403, HttpStatus.FORBIDDEN, "Forbidden"),
    NOT_FOUND(404, HttpStatus.NOT_FOUND, "Not Found"),
    METHOD_NOT_ALLOWED(405, HttpStatus.METHOD_NOT_ALLOWED, "Method Not Allowed"),
    ALREADY_EXIST_PHONENUMBER(409, HttpStatus.CONFLICT, "Conflict"),
    INTERNAL_SERVER_ERROR(500, HttpStatus.INTERNAL_SERVER_ERROR, "Server Error");

    private final int code;
    private final HttpStatus httpStatus;
    private final String message;

    public String getMessage(Throwable e) {
        return this.getMessage(this.getMessage() + " : " + e.getMessage());
    }

    public String getMessage(String message) {
        return Optional.ofNullable(message).orElse(this.getMessage());
    }

    public static ErrorCode valueOf(HttpStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("HttpStatus must not be null");
        }

        return Arrays.stream(values())
                .filter(code -> code.getHttpStatus().equals(status))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("No matching constant for [" + status + "]"));
    }
}
