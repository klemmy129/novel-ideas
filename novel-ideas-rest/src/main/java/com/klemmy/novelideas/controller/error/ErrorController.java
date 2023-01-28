package com.klemmy.novelideas.controller.error;

import com.klemmy.novelideas.error.FindDataException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import java.util.Objects;

@RestControllerAdvice
public class ErrorController {

    @ExceptionHandler(ConstraintViolationException.class)
    ProblemDetail onConstraintValidationException(ConstraintViolationException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
            problemDetail.setProperty(violation.getPropertyPath().toString(), violation.getMessage());
        }
        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        BindingResult result = e.getBindingResult();
        for (FieldError f : result.getFieldErrors()) {
            problemDetail.setProperty(f.getField(), Objects.requireNonNull(f.getCode()));
        }
        return problemDetail;
    }

    @ExceptionHandler(IllegalStateException.class)
    ProblemDetail onIllegalStateException(IllegalStateException e) {

        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail assertionException(final IllegalArgumentException e) {

        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(FindDataException.class)
    ProblemDetail onMethodArgumentNotValidException(FindDataException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problemDetail.setProperty("id", e.getMessage());

        return problemDetail;
    }

}
