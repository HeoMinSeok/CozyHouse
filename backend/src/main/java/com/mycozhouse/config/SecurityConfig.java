package com.mycozhouse.config;


import com.mycozhouse.jwt.*;
import com.mycozhouse.repository.RefreshRepository;
import com.mycozhouse.service.CustomOAuth2UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final SuccessHandler customSuccessHandler;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;
//    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        // AuthenticationConfiguration 객체를 통해 AuthenticationManager를 생성하여 반환
        // AuthenticationManager는 인증 처리를 위한 핵심 컴포넌트로, Spring Security의 인증 로직을 담당
        return configuration.getAuthenticationManager();
    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        // BCryptPasswordEncoder 객체를 생성하여 Spring 컨텍스트에 빈으로 등록합니다.
        // 이 객체는 비밀번호를 해시하고 검증하는 데 사용됩니다.
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // CORS 설정: Cross-Origin Resource Sharing(CORS) 정책을 정의하여
        // 다른 출처의 클라이언트가 서버의 리소스에 접근할 수 있도록 허용하는 설정
        // 주로 REST API와 프론트엔드 애플리케이션 간의 통신을 원활하게 하기 위해 필요
        http
                .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        // CORS 설정 객체 생성
                        CorsConfiguration configuration = new CorsConfiguration();

                        // 허용할 출처 설정: http://localhost:3000
                        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:5173"));
                        // 모든 HTTP 메서드 허용
                        configuration.setAllowedMethods(Collections.singletonList("*"));
                        // 자격 증명(쿠키, Authorization 헤더 등)을 포함할 수 있도록 허용
                        configuration.setAllowCredentials(true);
                        // 모든 요청 헤더를 허용
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        // CORS 설정의 최대 유효 시간 설정(3600초)
                        configuration.setMaxAge(3600L);

                        // 클라이언트에 노출할 헤더 설정
                        configuration.setExposedHeaders(Arrays.asList("Set-Cookie", "Authorization", "access", "refresh")); // 필요한 헤더를 모두 포함

                        // 설정된 CORS 설정 반환
                        return configuration;
                    }
                }));

        // CSRF 보호 비활성화: REST API를 사용하는 경우, CSRF 보호가 필요하지 않으므로 비활성화(세션을 사용하지 않음)
        http
                .csrf((auth) -> auth.disable());

        // 폼 로그인 방식 비활성화: REST API에서 폼 로그인 방식은 사용되지 않으므로 비활성화
        http
                .formLogin((auth) -> auth.disable());

        // HTTP Basic 인증 비활성화: REST API에서 다른 인증 방식을 사용할 예정이므로 비활성화
        http
                .httpBasic((auth) -> auth.disable());

        // 경로별 인가 작업: 각 경로에 대해 접근 권한 설정
        http
                .authorizeHttpRequests((auth) -> auth
                        // 로그인, 루트, 회원가입 페이지는 모두 접근 허용
                        .requestMatchers("/login","/login/**", "/", "/users/**", "/reissue","/oauth2/**").permitAll()
                        // /admin 경로는 ADMIN 역할을 가진 사용자만 접근 허용
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers("/reissue").permitAll()
                        // 그 외 모든 요청은 인증된 사용자만 접근 허용
                        .anyRequest().authenticated());

        http
                .addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);

//        // 새로 정의한 LoginFilter를 UsernamePasswordAuthenticationFilter 필터 체인에서 지정된 위치에 추가
//        // 로그인 필터는 사용자의 로그인 요청을 처리하며, 기존의 UsernamePasswordAuthenticationFilter 대신 사용됨
//        // 필터 체인에서의 위치를 지정하여, 해당 필터가 어떤 필터와 순서상 관계를 갖는지를 정의
        http
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, refreshRepository), UsernamePasswordAuthenticationFilter.class);

        http
                .addFilterBefore(new CustomLogoutFilter(jwtUtil, refreshRepository), LogoutFilter.class);
        // OAuth2 로그인을 설정
        // Customizer.withDefaults()를 사용하여 기본적인 OAuth2 로그인 설정을 적용
        http.oauth2Login((oauth2) -> oauth2
                .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
                        .userService(customOAuth2UserService))
                .successHandler(customSuccessHandler)
        );
//                .exceptionHandling(exceptions ->
//                exceptions.authenticationEntryPoint(authenticationEntryPoint)
//        );

        // 세션 관리 설정: RESTful API는 무상태성을 유지해야함, 세션을 사용하지 않도록 Stateless로 설정 (주로 JWT 사용 시)
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 설정된 HttpSecurity 객체를 빌드하여 반환
        return http.build();
    }
}




