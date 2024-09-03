package com.mycozyhouse.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.UUID;

@Component
public class RequestIdFilter extends OncePerRequestFilter {

    public static final String REQUEST_ID_HEADER = "X-Request-ID";
    private final SecureRandom random = new SecureRandom();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 8자리 난수로 요청 ID 생성
        String requestId =  UUID.randomUUID().toString().substring(0, 8);

        // 요청 속성에 요청 ID 저장
        request.setAttribute(REQUEST_ID_HEADER, requestId);

        // 필터 체인을 계속해서 호출
        filterChain.doFilter(request, response);
    }
}