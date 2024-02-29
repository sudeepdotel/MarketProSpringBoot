package org.nepalimarket.nepalimarketproproject.exception;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.cache.spi.access.UnknownAccessTypeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalException {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        errors.put("error", "Validation failed");
        errors.put("details", fieldErrors.stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", ")));

        log.error("Validation failed: {}", errors);
        return errors;
    }

    @ExceptionHandler(MultipleExceptionsOccurredException.class)
    public ResponseEntity<Map<String, Object>> handleMultipleExceptions(MultipleExceptionsOccurredException ex) {
        List<String> errorMessages = ex.getExceptions().stream()
                .map(Exception::getMessage)
                .collect(Collectors.toList());

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("error", "Multiple exceptions occurred");
        responseBody.put("details", errorMessages);

        log.error("Multiple exceptions occurred: {}", errorMessages);
        return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<String> handleInternalAuthenticationServiceException(InternalAuthenticationServiceException e) {
        log.error("Internal authentication service exception: {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UnknownAccessTypeException.class)
    public ResponseEntity<String> handleUnknownAccessTypeException(UnknownAccessTypeException ex) {
        log.error("Unknown access type exception: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unknown access type");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        log.error("An unexpected exception occurred: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
    }

    @ExceptionHandler(OutOfStockException.class)
    public ResponseEntity<String> handleOutOfStockException(OutOfStockException ex) {
        log.error("Out of stock exception: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
