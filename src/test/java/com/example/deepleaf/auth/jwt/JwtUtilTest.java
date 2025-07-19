package com.example.deepleaf.auth.jwt;

import com.example.deepleaf.auth.fixture.AuthTestFixture;
import com.example.deepleaf.member.domain.Member;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(MockitoExtension.class)
public class JwtUtilTest {

    private JWTUtil jwtUtil;

    private String secret = "secretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecret";
    @BeforeEach
    void setUp() {
        String secret = this.secret;
        jwtUtil = new JWTUtil(secret);
    }

    @Test
    void 액세스토큰을_생성하면_정상적으로_디코딩되어야_한다() {
        //given
        Member mockMember = AuthTestFixture.createMockMember();

        //when
        String accessToken = jwtUtil.createAccessToken(mockMember.getId(), mockMember.getEmail(), mockMember.getRole());

        //then
        Assertions.assertThat(jwtUtil.getId(accessToken)).isEqualTo(mockMember.getId());
        Assertions.assertThat(jwtUtil.getEmail(accessToken)).isEqualTo(mockMember.getEmail());
        Assertions.assertThat(jwtUtil.getRole(accessToken)).isEqualTo(mockMember.getRole());
    }

    @Test
    void 만료된_토큰이면_isExpired에서_오류가_발생한다() throws InterruptedException {
        // given
        String shortLivedToken = Jwts.builder()
                .claim("member_id", 1L)
                .expiration(new Date(System.currentTimeMillis() + 100))
                .signWith(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm()))
                .compact();

        Thread.sleep(200); // 만료되게끔 약간의 시간 대기

        // when
        assertThrows(ExpiredJwtException.class, () -> {
            jwtUtil.isExpired(shortLivedToken);
        });

    }
}
