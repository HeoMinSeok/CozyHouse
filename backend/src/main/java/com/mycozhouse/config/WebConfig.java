package com.mycozhouse.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // Spring의 설정 클래스임을 나타냄=
public class WebConfig implements WebMvcConfigurer { // WebMvcConfigurer 인터페이스를 구현하여 CORS 설정을 커스터마이즈

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        // 모든 경로에 대한 CORS 매핑 추가
        corsRegistry.addMapping("/**") // 모든 요청 경로에 대해 CORS 설정 적용
                .allowedOrigins("http://localhost:5173") // http://localhost:3000 출처에서 오는 요청을 허용
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 허용할 HTTP 메소드
                .allowedHeaders("*") // 허용할 헤더
                .exposedHeaders("Set-Cookie", "Authorization") // 클라이언트에 노출할 헤더
                .allowCredentials(true); // 자격 증명 허용
    }
}
