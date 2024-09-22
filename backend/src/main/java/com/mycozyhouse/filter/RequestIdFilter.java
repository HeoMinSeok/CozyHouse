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

// HTTP 요청에 고유한 요청 ID를 생성하여 요청 속성에 저장하는 필터
@Component
public class RequestIdFilter extends OncePerRequestFilter {

    public static final String REQUEST_ID_HEADER = "X-Request-ID";
    private final SecureRandom random = new SecureRandom();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestId =  UUID.randomUUID().toString().substring(0, 8);
        request.setAttribute(REQUEST_ID_HEADER, requestId);

        filterChain.doFilter(request, response);
    }
}