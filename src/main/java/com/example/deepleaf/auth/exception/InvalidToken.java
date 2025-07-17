package com.example.deepleaf.auth.exception;

import com.example.deepleaf.global.exception.GlobalException;
import org.springframework.http.HttpStatus;

public class InvalidToken extends GlobalException {
    public InvalidToken() {
        super(HttpStatus.UNAUTHORIZED, "TOKEN_INVALID", "유효하지 않은 토큰");
    }
}
