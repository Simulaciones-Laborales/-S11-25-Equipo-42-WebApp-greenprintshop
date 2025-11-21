package com.ecommerce.controller.advice;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ecommerce.dto.Response;
import com.ecommerce.dto.ValidationErrorDetail;
import com.ecommerce.exception.BadRequestCustomException;
import com.ecommerce.exception.DataExistException;
import com.ecommerce.exception.NotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response<Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<ValidationErrorDetail> errorList = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> new ValidationErrorDetail(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());

        return new ResponseEntity<>(mappingError(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(),
                errorList), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Response<Object>> handleGeneralExceptions(Exception ex) {
        List<String> errorList = Collections.singletonList(ex.getMessage());
        return new ResponseEntity<>(mappingError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), errorList),
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public final ResponseEntity<Response<Object>> handleRuntimeExceptions(RuntimeException ex) {
        List<String> errorList = Collections.singletonList(ex.getMessage());
        return new ResponseEntity<>(mappingError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), errorList),
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataExistException.class)
    public final ResponseEntity<Response<Object>> dataExistException(DataExistException ex) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        return new ResponseEntity<>(mappingError(HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(), errors),
                new HttpHeaders(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Response<Object>> handleNotFoundException(NotFoundException ex) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        return new ResponseEntity<>(mappingError(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(),
                errors), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestCustomException.class)
    public final ResponseEntity<Response<Object>> handleBadRequestCustomException(BadRequestCustomException ex) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        return new ResponseEntity<>(mappingError(HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(), errors),
                new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    private Response<Object> mappingError(int responseCode, String responseMessage, List<?> errorList) {
        return Response.builder()
                .responseCode(responseCode)
                .responseMessage(responseMessage)
                .errorList(errorList)
                .build();
    }
}