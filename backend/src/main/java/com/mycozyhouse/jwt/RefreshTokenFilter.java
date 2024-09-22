package com.mycozyhouse.jwt;

import com.mycozyhouse.repository.RefreshRepository;
import com.mycozyhouse.utill.CookieUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//refreshToken을 검증하는 필터
@RequiredArgsConstructor
public class RefreshTokenFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    /**
     * 요청에 대한 필터링 및 인증 처리를 수행하는 메서드.
     *
     * @param request 클라이언트의 요청 정보를 포함하는 HttpServletRequest 객체
     * @param response 서버의 응답 정보를 설정하는 HttpServletResponse 객체
     * @param filterChain 다음 필터를 호출하는 FilterChain 객체
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String refreshToken = CookieUtil.getCookieValue(request, "refresh");

        if (refreshToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if (jwtUtil.isExpired(refreshToken)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Refresh token has expired.");
            return;
        }

        String category = jwtUtil.getCategory(refreshToken);
        if (!category.equals("refresh")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid refresh token.");
            return;
        }

        if (!refreshRepository.existsByRefresh(refreshToken)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Refresh token not found.");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
