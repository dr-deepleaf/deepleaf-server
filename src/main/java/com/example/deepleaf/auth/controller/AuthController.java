package com.example.deepleaf.auth.controller;

import com.example.deepleaf.auth.service.AuthService;
import com.example.deepleaf.member.domain.Member;
import com.example.deepleaf.member.dto.request.MemberCreateDto;
import com.example.deepleaf.member.dto.request.MemberLoginDto;
import com.example.deepleaf.member.dto.response.MemberLoginResponseDto;
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
    public ResponseEntity<?> createMember(@RequestBody MemberCreateDto memberCreateDto){
        Member member = authService.create(memberCreateDto);
        return new ResponseEntity<>(member.getId(), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<MemberLoginResponseDto> login(@RequestBody MemberLoginDto memberLoginDto){
        MemberLoginResponseDto response = authService.login(memberLoginDto);
        return ResponseEntity.ok().body(response);
    }
}
