package com.mycozyhouse.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycozyhouse.dto.UserStatus;
import com.mycozyhouse.entity.RefreshEntity;
import com.mycozyhouse.repository.RefreshRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

/**
 * 로그인 요청을 처리하는 커스텀 필터(클라이언트 요청을 가로챔)
 * UsernamePasswordAuthenticationFilter를 확장하여 인증 과정을 사용자 정의
 */
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    // 주입 받기 위해서는 SecurityConfig에서 filter에 필드들을 주입해줘야함
//    addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class);
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    /**
     * 사용자가 제공한 자격 증명을 기반으로 인증을 시도하는 메서드
     *
     * @param request  로그인 요청을 포함한 HttpServletRequest
     * @param response 응답을 전송하는 HttpServletResponse
     * @return 인증 객체를 반환
     * @throws AuthenticationException 인증에 실패할 경우 발생할 수 있는 예외
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("실행");
        // JSON 형태의 요청 본문을 읽어 사용자 이름과 비밀번호를 추출
        try {
            Map<String, String> loginRequest = new ObjectMapper().readValue(request.getInputStream(), Map.class);
            String email = loginRequest.get("email");
            String password = loginRequest.get("password");

            //스프링 시큐리티에서 아이디와 비밀번호를 이용하여 검증에 사용하기 위해 인증 토큰 생성
            // UsernamePasswordAuthenticationToken(principal, credentials, authorities)
            // principal: 사용자 식별 정보 (예: 이메일 또는 사용자 이름)
            // credentials: 사용자 자격 증명 (예: 비밀번호)
            // authorities: 사용자 권한 정보 (예: ROLE_USER, ROLE_ADMIN)
            //로그인 과정에서 권한이 필요한 경우 authorities이 사용될 수 있지만, 일반적으로는 인증 후 권한을 설정하는 것이 표준
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);

            //인증 관리자(authenticationManager)를 통해 인증 토큰을 인증
            //과정에서 데이터베이스에서 사용자의 정보를 조회하고, 비밀번호를 확인
            return authenticationManager.authenticate(authToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 인증이 성공했을 때 호출되는 메서드
     * 성공적인 인증 후에 어떤 작업을 수행할지 정의
     *
     * @param request        요청을 포함한 HttpServletRequest
     * @param response       응답을 전송하는 HttpServletResponse
     * @param chain          다음 필터로 요청을 전달하는 FilterChain
     * @param authentication 인증된 사용자 정보를 포함하는 Authentication 객체
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {

        //인증된 사용자의 이메일을 가져옴
        String email = authentication.getName();

        // 사용자의 상태를 가져옴(회원 , 비회원)
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        // 권한이 여러 개일 수 있으므로 Iterator를 사용하여 첫 번째 권한을 가져옵니다.
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        // 첫 번째 권한(역할)을 가져옵니다. 예: ROLE_USER, ROLE_ADMIN 등
        UserStatus status = UserStatus.valueOf(auth.getAuthority());

        // JWT 토큰 생성
        // access 토큰 생성: 만료 시간 600초(10분)
        String access = jwtUtil.createJwt("access", email, status, 600000L);
        // refresh 토큰 생성: 만료 시간 86400초(1일)
        String refresh = jwtUtil.createJwt("refresh", email, status, 86400000L);

        //Refresh 토큰 저장
        addRefreshEntity(email, refresh, 86400000L);

        // 응답 설정
        // 생성된 access 토큰을 응답 헤더에 설정합니다.
        response.setHeader("access", access);
        // 생성된 refresh 토큰을 쿠키에 추가합니다.
        response.addCookie(createCookie("refresh", refresh));
        // HTTP 응답 상태를 200 OK로 설정합니다.
        response.setStatus(HttpStatus.OK.value());
    }

    /**
     * 인증이 실패했을 때 호출되는 메서드
     * 인증 실패에 대한 처리를 여기서 정의
     *
     * @param request  요청을 포함한 HttpServletRequest
     * @param response 응답을 전송하는 HttpServletResponse
     * @param failed   인증 실패를 나타내는 AuthenticationException 객체
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        //로그인 실패시 401 응답 코드 반환
        response.setStatus(401);
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value); // 주어진 키와 값을 사용하여 새로운 쿠키 객체를 생성
        cookie.setMaxAge(24 * 60 * 60);   // 쿠키의 최대 수명을 24시간으로 설정 (초 단위)
//         cookie.setSecure(true); // 쿠키를 HTTPS 연결에서만 사용할 수 있도록 설정
         cookie.setPath("/"); // 쿠키가 웹 어플리케이션의 루트 경로에서만 접근 가능하도록 설정
        cookie.setHttpOnly(true); // 쿠키를 JavaScript에서 접근할 수 없도록 설정하여 보안을 강화

        return cookie;
    }

    //로그인 성공했을 때  새로운 토큰저장
    private void addRefreshEntity(String username, String refresh, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshEntity refreshEntity = new RefreshEntity();
        refreshEntity.setEmail(username);
        refreshEntity.setRefresh(refresh);
        refreshEntity.setExpiration(date.toString());

        refreshRepository.save(refreshEntity);
    }
}
