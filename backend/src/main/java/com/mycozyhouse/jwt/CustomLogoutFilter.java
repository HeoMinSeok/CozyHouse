package com.mycozyhouse.jwt;

import com.mycozyhouse.repository.RefreshRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;


@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // ServletRequest와 ServletResponse를 HttpServletRequest와 HttpServletResponse로 변환하여 doFilter 메서드 호출
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        // 요청 경로와 메서드를 검증
        String requestUri = request.getRequestURI();
        if (!requestUri.matches("^\\/logout$")) {
            // 로그아웃 경로가 아니면 다음 필터로 진행
            filterChain.doFilter(request, response);
            return;
        }

        String requestMethod = request.getMethod();
        if (!requestMethod.equals("POST")) {
            // POST 메서드가 아니면 다음 필터로 진행
            filterChain.doFilter(request, response);
            return;
        }

        // 리프레시 토큰 가져오기
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                // 찾은 경우 리프레시 토큰 값을 저장
                refresh = cookie.getValue();
            }
        }

        // 리프레시 토큰이 null인지 확인
        if (refresh == null) {
            // 응답 상태 코드를 BAD_REQUEST로 설정
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 만료 여부 확인
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            // 만료된 경우 응답 상태 코드를 BAD_REQUEST로 설정
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 토큰이 리프레시인지 확인 (발급 시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);
        if (!category.equals("refresh")) {
            // 응답 상태 코드를 BAD_REQUEST로 설정
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // DB에 저장되어 있는지 확인
        Boolean isExist = refreshRepository.existsByRefresh(refresh);
        if (!isExist) {
            // 응답 상태 코드를 BAD_REQUEST로 설정
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 로그아웃 진행
        // Refresh 토큰 DB에서 제거
        refreshRepository.deleteByRefresh(refresh);

        // Refresh 토큰 Cookie 값 0으로 설정
        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0); // 쿠키의 최대 수명을 0으로 설정하여 즉시 삭제
        cookie.setPath("/"); // 모든 경로에서 쿠키를 사용할 수 있도록 설정

        response.addCookie(cookie); // 쿠키를 응답에 추가
        response.setStatus(HttpServletResponse.SC_OK); // 응답 상태 코드를 OK로 설정
    }
}