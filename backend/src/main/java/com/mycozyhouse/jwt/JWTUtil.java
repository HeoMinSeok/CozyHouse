package com.mycozyhouse.jwt;

import com.mycozyhouse.dto.UserStatus;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

//JWT를 발급 및 검증 등
@Component
public class JWTUtil {
    private final SecretKey secretKey;

    public JWTUtil(@Value("${spring.jwt.secret}") String secret) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    // JWT에서 username 클레임을 추출하여 반환하는 메서드
    public String getNickname(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload().get("nickname", String.class);
    }

    //JWT 토큰을 파싱하여 서명된 클레임을 검증하고 추출
    public String getCategory(String token) {

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("category", String.class);
    }

    // JWT의 만료 여부를 확인하는 메서드
    public Boolean isExpired(String token) {

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload().getExpiration().before(new Date());
    }

    public String getProvider(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("provider", String.class);
    }

    /**
     * 새로운 JWT를 생성하는 메서드
     * @param category   JWT에 포함될 토큰의 종류
     * @param nickname   JWT에 포함될 사용자 이름. 사의 식별 정보를 나타냄.
     * @param expiredMs  JWT의 만료 시간을 설정하는 밀리초. 현재 시간으로부터 얼마나 뒤에 만료되는지를 나타냄.
     * @return 생성된 JWT 문자열. 클레임이 포함된 서명된 토큰을 반환함.
     */
    public String createJwt(String category, String nickname, Long expiredMs) {

        return Jwts.builder()
                .claim("category", category)
                .claim("nickname", nickname)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }
}

