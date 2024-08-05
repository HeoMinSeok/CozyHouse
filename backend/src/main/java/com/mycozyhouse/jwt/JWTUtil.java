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
    private final SecretKey secretKey;  // 대칭 키

    public JWTUtil(@Value("${spring.jwt.secret}") String secret) {
        // 대칭키 초기화: 주어진 비밀 문자열을 기반으로 SecretKeySpec 객체 생성
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    // JWT에서 username 클레임을 추출하여 반환하는 메서드
    public String getEmail(String token) {
        // JWT 파서를 생성하고 대칭키로 JWT를 검증하기 위해 설정
        return Jwts.parser()  // JWT 파서 생성
                .verifyWith(secretKey) //  JWT의 서명이 주어진 키와 일치하는지 확인하는 준비
                .build() // JwtParser 객체 생성
                .parseSignedClaims(token) // JWT를 파싱하여 서명을 검증하고 클레임 추출
                .getPayload().get("email", String.class); // 클레임에서 username 값을 추출하여 반환
    }

    // JWT에서 role 클레임을 추출하여 반환하는 메서드
    public UserStatus getStatus(String token) {

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload().get("status", UserStatus.class); // 클레임에서 role 값을 추출하여 반환
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
                .getPayload().getExpiration().before(new Date()); // 만료 날짜가 현재 날짜보다 이전인지 비교하여 만료 여부 반환
    }

    // JWT에서 provider 클레임을 추출하여 반환하는 메서드
    public String getProvider(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("provider", String.class); // provider 값을 추출하여 반환
    }

    /**
     * 새로운 JWT를 생성하는 메서드
     *
     * @param email  JWT에 포함될 사용자 이름. 사용자의 식별 정보를 나타냄.
     * @param userStatus      JWT에 포함될 사용자 역할. 권한 관리를 위해 사용됨.
     * @param expiredMs JWT의 만료 시간을 설정하는 밀리초. 현재 시간으로부터 얼마나 뒤에 만료되는지를 나타냄.
     * @return 생성된 JWT 문자열. 클레임이 포함된 서명된 토큰을 반환함.
     */
    public String createJwt(String category, String email, UserStatus userStatus, Long expiredMs) {

        return Jwts.builder()
                .claim("category", category)
                .claim("email", email)
                .claim("userStatus", userStatus)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }
}

