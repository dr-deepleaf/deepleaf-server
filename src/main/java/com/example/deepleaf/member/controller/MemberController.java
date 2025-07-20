package com.example.deepleaf.member.controller;


import com.example.deepleaf.member.dto.MemberInfoResDto;
import com.example.deepleaf.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    @GetMapping("/member")
    public ResponseEntity<MemberInfoResDto> memberInfo(@RequestParam(name = "id") Long id){
        MemberInfoResDto memberInfoResDto = memberService.getMemberInfo(id);
        return ResponseEntity.ok().body(memberInfoResDto);
    }
}
