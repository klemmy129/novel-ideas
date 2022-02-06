package com.klemmy.novelideas.controller.error;

import com.klemmy.novelideas.api.ValidationErrorResponse;
import com.klemmy.novelideas.api.Violation;
import com.klemmy.novelideas.error.FindDataException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ErrorController {

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  ValidationErrorResponse onConstraintValidationException(ConstraintViolationException e) {
    ValidationErrorResponse error = new ValidationErrorResponse();
    for (ConstraintViolation violation : e.getConstraintViolations()) {
      error.getViolations().add(new Violation(violation.getPropertyPath().toString(), violation.getMessage()));
    }
    return error;
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  ValidationErrorResponse onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    ValidationErrorResponse error = new ValidationErrorResponse();
    BindingResult result = e.getBindingResult();
    List<Violation> errorBuild = result.getFieldErrors().stream()
        .map(f -> new Violation( f.getField(), f.getCode()))
        .collect(Collectors.toList());
    error.getViolations().addAll(errorBuild);
    return error;
  }

  @ExceptionHandler(IllegalStateException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  ValidationErrorResponse onIllegalStateException(IllegalStateException e) {
    ValidationErrorResponse error = new ValidationErrorResponse();
    error.getViolations().add(new Violation("", e.getMessage()));
    return error;
  }

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ValidationErrorResponse assertionException(final IllegalArgumentException e) {
    ValidationErrorResponse error = new ValidationErrorResponse();
    error.getViolations().add(new Violation("", e.getMessage()));
    return error;
  }

  @ExceptionHandler(FindDataException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  ValidationErrorResponse onMethodArgumentNotValidException(FindDataException e) {
    ValidationErrorResponse error = new ValidationErrorResponse();
    error.getViolations().add(new Violation("id", e.getMessage()));
    return error;
  }

}
