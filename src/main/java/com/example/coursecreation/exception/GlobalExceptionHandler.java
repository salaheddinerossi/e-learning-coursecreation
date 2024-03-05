package com.example.coursecreation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CategoryNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handelCategoryNotFoundException(CategoryNotFoundException e) {
        return e.getMessage();
    }

    @ExceptionHandler(NoChildCategoriesException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handelNoChildCategoriesException(NoChildCategoriesException e) {
        return e.getMessage();
    }

}
