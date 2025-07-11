package com.example.deepleaf.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberLoginDto {
    private String email;
    private String password;
}
