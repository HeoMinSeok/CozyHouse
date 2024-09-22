package com.mycozyhouse.jwt;

import com.mycozyhouse.config.CustomUserDetails;
import com.mycozyhouse.dto.UserStatus;
import com.mycozyhouse.entity.UserEntity;
import com.mycozyhouse.utill.CookieUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//jwt를 검증하는 필터
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    /**
     * 요청에 대한 필터링 및 인증 처리를 수행하는 메서드.
     *
     * @param request 클라이언트의 요청 정보를 포함하는 HttpServletRequest 객체
     * @param response 서버의 응답 정보를 설정하는 HttpServletResponse 객체
     * @param filterChain 다음 필터를 호출하는 FilterChain 객체
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String cookieAccessToken = CookieUtil.getCookieValue(request, "access");

        if (cookieAccessToken != null) {
            response.setHeader("access", cookieAccessToken);
            filterChain.doFilter(request, response);
            return;
        }

        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.split(" ")[1];

        if (jwtUtil.isExpired(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Refresh token has expired.");
            filterChain.doFilter(request, response);
            return;
        }

        String nickname = jwtUtil.getNickname(token);

        UserEntity userEntity = new UserEntity();
        userEntity.setNickname(nickname);

        CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null,
                customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
