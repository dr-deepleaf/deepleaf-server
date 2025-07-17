package com.example.deepleaf.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GlobalException extends RuntimeException{

    private String message;
    private HttpStatus status;
    private String name;

    public GlobalException(final HttpStatus status, final String name, final String message) {
        super(message);
        this.status = status;
        this.message = message;
        this.name = name;
    }
}
