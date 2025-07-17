package com.example.deepleaf.auth.controller;

import com.example.deepleaf.auth.dto.request.MemberCreateReqDto;
import com.example.deepleaf.auth.dto.request.MemberLoginReqDto;
import com.example.deepleaf.auth.dto.response.MemberCreateResDto;
import com.example.deepleaf.auth.dto.response.MemberLoginResDto;
import com.example.deepleaf.auth.service.AuthService;
import com.example.deepleaf.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PostMapping("/create")
    public ResponseEntity<MemberCreateResDto> createMember(@RequestBody MemberCreateReqDto memberCreateReqDto){
        MemberCreateResDto memberCreateResDto = authService.create(memberCreateReqDto);
        return ResponseEntity.status(201).body(memberCreateResDto);
    }

    @PostMapping("/login")
    public ResponseEntity<MemberLoginResDto> login(@RequestBody MemberLoginReqDto memberLoginReqDto){
        MemberLoginResDto response = authService.login(memberLoginReqDto);
        return ResponseEntity.ok().body(response);
    }
}
