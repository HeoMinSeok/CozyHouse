//package com.mycozyhouse.config;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
//@Component
//public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
//
//    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);
//
//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
//        // 인증 오류 로그 출력
//        logger.error("Authentication failed: {}", authException.getMessage());
//
//        // 클라이언트에게 오류 상태 코드와 메시지 전송
//        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication Failed");
//    }
//}