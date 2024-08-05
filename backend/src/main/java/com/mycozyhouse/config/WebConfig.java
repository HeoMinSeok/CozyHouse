package com.mycozyhouse.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // Spring의 설정 클래스임을 나타냄=
public class WebConfig implements WebMvcConfigurer { // WebMvcConfigurer 인터페이스를 구현하여 CORS 설정을 커스터마이즈

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        // 모든 경로에 대한 CORS 매핑 추가
        corsRegistry.addMapping("/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Set-Cookie", "Authorization")
                .allowCredentials(true);
    }
}
