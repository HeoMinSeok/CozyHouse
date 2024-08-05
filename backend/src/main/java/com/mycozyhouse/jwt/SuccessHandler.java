package com.mycozyhouse.jwt;

import com.mycozyhouse.dto.UserStatus;
import com.mycozyhouse.entity.RefreshEntity;
import com.mycozyhouse.repository.RefreshRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

@Component
public class SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    public SuccessHandler(JWTUtil jwtUtil, RefreshRepository refreshRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
    }

    // 인증 성공 시 호출되는 메서드
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        // 리프레시 토큰을 초기화
//        String refresh = null;

        // 인증된 사용자 정보를 CustomOAuth2User로 캐스팅
        OAuth2User customUserDetails = (OAuth2User) authentication.getPrincipal();

        String nickname = customUserDetails.getName(); // 사용자 이름 가져오기

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities(); // 사용자의 권한 목록 가져오기
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator(); // 권한 목록의 Iterator 생성
        GrantedAuthority auth = iterator.next(); // 첫 번째 권한 가져오기
        UserStatus status = UserStatus.valueOf(auth.getAuthority()); // 권한 문자열 가져오기

        // JWT 토큰 생성
        // access 토큰 생성: 만료 시간 600초(10분)
        String access = jwtUtil.createJwt("access", nickname, status, 600000L);
        // refresh 토큰 생성: 만료 시간 86400초(1일)
        String refresh = jwtUtil.createJwt("refresh", nickname, status, 86400000L);

        //Refresh 토큰 저장
        addRefreshEntity(nickname, refresh, 86400000L);

        // 응답 설정
        response.addCookie(createCookie("access", access));
        // 생성된 refresh 토큰을 쿠키에 추가합니다.
        response.addCookie(createCookie("refresh", refresh));
        // HTTP 응답 상태를 200 OK로 설정합니다.
        response.setStatus(HttpStatus.OK.value());
        System.out.println("Access Token: " + access);
        System.out.println("Refresh Token: " + refresh);

        // 성공적으로 인증된 후 사용자를 특정 URI로 리다이렉트
        response.sendRedirect("http://localhost:5173/?loginMethod=social");
    }
    // 쿠키를 생성하는 메서드
    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value); // 쿠키 객체 생성
        cookie.setMaxAge(60 * 60 * 60); // 쿠키 만료 시간 설정 (초 단위)
        //cookie.setSecure(true); // HTTPS에서만 전송하도록 설정 (주석 처리됨)
        cookie.setPath("/"); // 쿠키의 유효 경로 설정
        cookie.setHttpOnly(true); // JavaScript에서 쿠키 접근 불가 설정 (보안성 향상)

        return cookie; // 생성한 쿠키 반환
    }

    //로그인 성공했을 때  새로운 토큰저장
    private void addRefreshEntity(String username, String refresh, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshEntity refreshEntity = new RefreshEntity();
        refreshEntity.setEmail(username);
        refreshEntity.setRefresh(refresh);
        refreshEntity.setExpiration(date.toString());

        refreshRepository.save(refreshEntity);
    }
}