package com.example.deepleaf.member.exception;

import com.example.deepleaf.global.exception.GlobalException;
import org.springframework.http.HttpStatus;

public class MemberNotFound extends GlobalException {

    public MemberNotFound() {
        super(HttpStatus.NOT_FOUND, "MEMBER_NOT_FOUND", "존재하지 않는 회원입니다");
    }
}
