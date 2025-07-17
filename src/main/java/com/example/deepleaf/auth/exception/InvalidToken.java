package com.example.deepleaf.auth.exception;

import com.example.deepleaf.global.exception.GlobalException;
import org.springframework.http.HttpStatus;

public class InvalidToken extends GlobalException{
    public InvalidToken() {
        super(HttpStatus.UNAUTHORIZED, "INVALID TOKEN", "유효 하지 않은 토큰입니다.");
    }
}
