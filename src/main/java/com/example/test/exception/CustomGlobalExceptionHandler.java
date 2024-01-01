package com.example.test.exception;

import com.example.test.errors.ErrorDto;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomGlobalExceptionHandler {

    @ExceptionHandler(RegistrationException.class)
    protected ResponseEntity<Object> handleRegistrationException(RegistrationException ex) {
        return createResponseEntity(
                new ErrorDto(LocalDateTime.now(), ex.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getAllErrors().stream()
                .map(this::getErrorMessage)
                .toList();
        ErrorDto errorDto = new ErrorDto(
                LocalDateTime.now(), errors);
        return createResponseEntity(errorDto, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Object> createResponseEntity(ErrorDto errorDto, HttpStatus status) {
        return new ResponseEntity<>(errorDto, status);
    }

    private String getErrorMessage(ObjectError objectError) {
        if (objectError instanceof FieldError) {
            String field = ((FieldError) objectError).getField();
            String message = objectError.getDefaultMessage();
            return String.join(" ", field, message);
        }
        return objectError.getDefaultMessage();
    }
}
