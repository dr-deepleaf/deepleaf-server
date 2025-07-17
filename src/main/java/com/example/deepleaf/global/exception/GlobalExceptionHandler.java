package com.example.deepleaf.global.exception;

import com.example.deepleaf.global.exception.dto.ExceptionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<ExceptionResponse> handleRuntimeException(final GlobalException e) {
        return ResponseEntity.status(e.getStatus()).body(ExceptionResponse.create(e.getName(),e.getMessage()));
    }
}
