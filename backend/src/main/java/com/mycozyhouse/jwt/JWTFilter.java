package com.mycozyhouse.jwt;

import com.mycozyhouse.config.CustomUserDetails;
import com.mycozyhouse.dto.UserStatus;
import com.mycozyhouse.entity.UserEntity;
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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        // 요청 헤더에서 Authorization 값을 가져옴
        String authorization = request.getHeader("Authorization");

        // Authorization 헤더가 없거나 Bearer로 시작하지 않는 경우
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            // 토큰이 없거나 잘못된 형식임을 출력
            System.out.println("token null");
            // 다음 필터로 요청을 전달
            filterChain.doFilter(request, response);
            return; // 필터 종료
        }

        //Bearer 뒤의 순수 토큰만 획득
        System.out.println("authorization now");
        String token = authorization.split(" ")[1];

        // JWT 만료 여부 확인
        if (jwtUtil.isExpired(token)) {
            // 만료된 토큰일 경우 출력
            System.out.println("token expired");
            // 다음 필터로 요청을 전달
            filterChain.doFilter(request, response);
            return; // 필터 종료
        }

        // 유효한 토큰일 경우 사용자 정보 추출
        String email = jwtUtil.getEmail(token); // 토큰에서 username 추출
        UserStatus status = jwtUtil.getStatus(token); // 토큰에서 role 추출

        // UserEntity 객체 생성 및 필드 설정
        UserEntity userEntity = new UserEntity();
        userEntity.setNickname(email); // username 설정
        userEntity.setStatus(status); // role 설정

        // CustomUserDetails 객체 생성
        CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);

        //인증 토큰 생성: 사용자 정보와 권한 설정
        //@param principal: 인증된 사용자 주체 (여기서는 CustomUserDetails 객체)
        //@param credentials: 인증 자격 증명 (여기서는 null)
        //@param authorities: 사용자 권한 목록 (여기서는 customUserDetails의 권한 목록)
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        // SecurityContext에 인증 정보 저장
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }
}
