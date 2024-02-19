package org.nepalimarket.nepalimarketproproject.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalException {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Map<String,String> invalidArguments( MethodArgumentNotValidException ex ){
        Map<String, String> errorMap = new HashMap<> (  );

        for(FieldError error: ex.getBindingResult().getFieldErrors()){
            errorMap.put(error.getField(), error.getDefaultMessage());
        }
        return errorMap;

    }

    @ExceptionHandler(MultipleExceptionsOccurredException.class)
    public ResponseEntity<Object> handleMultipleExceptions( MultipleExceptionsOccurredException ex){
        int count =1;
        List<String> errorMessages = new ArrayList<> ();
        for(Exception e: ex.getExceptions()){
            errorMessages.add(count + "." + e.getMessage());
            count++;
        }
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("Error: ", "Exception occurred!!");
        responseBody.put("Details: ", errorMessages.toString());
        return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<String> handleInternalAuthenticationServiceException(InternalAuthenticationServiceException e) {
        log.error ( e.getMessage () );
        return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
