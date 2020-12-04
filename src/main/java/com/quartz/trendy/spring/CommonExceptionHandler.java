package com.quartz.trendy.spring;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class CommonExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    public List<String> handleValidationFailures(final ConstraintViolationException exc) {
        return exc.getConstraintViolations()
                .stream()
                .map(v -> String.format("%s: %s", v.getPropertyPath().toString(), v.getMessage()))
                .collect(Collectors.toList());
    }
}
