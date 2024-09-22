package com.mycozyhouse.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//CspFilter 클래스는 모든 HTTP 요청에 대해 콘텐츠 보안 정책(CSP)을 설정
public class CspFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 모든 요청에 대해 동일한 CSP 설정 이 후 외부 도메인허용해야함
        String cspPolicy = "default-src 'self'; " +
                "script-src 'self'; " +
                "img-src 'self' data: blob: *.kakao.com *.kakao.co.kr; " +
                "connect-src 'self' your-upload-api-domain.com;";

        response.setHeader("Content-Security-Policy", cspPolicy);
        filterChain.doFilter(request, response);
    }
}