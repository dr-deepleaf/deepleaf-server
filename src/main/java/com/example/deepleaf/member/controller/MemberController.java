package com.example.deepleaf.member.controller;


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

    @GetMapping("/member")
    public ResponseEntity<?> getMember(@RequestParam(name = "id") Long id){
        return ResponseEntity.ok().body(id);
    }
}
