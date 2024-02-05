package ru.practicum.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HandlerException {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) //ошибка 400
    public ErrorResponse handleBindException(final BindException e) {
        return new ErrorResponse(e.getMessage());
    }
}
