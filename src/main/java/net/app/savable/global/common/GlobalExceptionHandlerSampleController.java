package net.app.savable.global.common;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.web.bind.annotation.GetMapping;

public class GlobalExceptionHandlerSampleController {
    @GetMapping("/validation-error")
    public void validationError() { // ConstraintViolationException을 발생시키는 메서드(GeneralException을 발생시키는 메서드와 동일)
        throw new ConstraintViolationException("Validation error!", null, "");
    }

    @GetMapping("/exception")
    public void exception() { // RuntimeException을 발생시키는 메서드(GeneralException을 발생시키는 메서드와 동일)
        throw new RuntimeException("An unexpected error occurred!");
    }

}
