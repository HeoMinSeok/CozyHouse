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

        OAuth2User customUserDetails = (OAuth2User) authentication.getPrincipal();

        String nickname = customUserDetails.getName();

        String access = jwtUtil.createJwt("access", nickname, 600000L);
        String refresh = jwtUtil.createJwt("refresh", nickname,86400000L);

        addRefreshEntity(nickname, refresh, 86400000L);

        response.addCookie(createCookie("access", access));
        response.addCookie(createCookie("refresh", refresh));
        response.setStatus(HttpStatus.OK.value());
        System.out.println("Access Token: " + access);
        System.out.println("Refresh Token: " + refresh);

        response.sendRedirect("http://localhost:5173/?loginMethod=social");
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60 * 60 * 60);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

    //로그인 성공했을 때  새로운 토큰저장
    private void addRefreshEntity(String nickname, String refresh, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshEntity refreshEntity = new RefreshEntity();
        refreshEntity.setNickname(nickname);
        refreshEntity.setRefresh(refresh);
        refreshEntity.setExpiration(date.toString());

        refreshRepository.save(refreshEntity);
    }
}