package net.app.savable.global.error.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidSocialIdException extends RuntimeException{
    public InvalidSocialIdException(String message) {
        super(message);
    }
}
