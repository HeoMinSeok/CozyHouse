package com.mycozyhouse.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycozyhouse.entity.RefreshEntity;
import com.mycozyhouse.repository.RefreshRepository;
import com.mycozyhouse.utill.CookieUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import java.io.IOException;
import java.util.Date;
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

        try {
            Map<String, String> loginRequest = new ObjectMapper().readValue(request.getInputStream(), Map.class);
            String email = loginRequest.get("email");
            String password = loginRequest.get("password");

            if (email == null || email.isEmpty() || !isValidEmail(email)) {
                throw new BadCredentialsException("유효한 이메일 주소를 입력하세요.");
            }

            if (password == null || password.isEmpty()) {
                throw new BadCredentialsException("비밀번호는 필수입니다.");
            }

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);
            return authenticationManager.authenticate(authToken);
        } catch (IOException e) {
            throw new RuntimeException("로그인 요청 처리 중 오류 발생", e);
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return email.matches(emailRegex);
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

        String nickname = authentication.getName();

        String access = jwtUtil.createJwt("access", nickname,50000L);
        String refresh = jwtUtil.createJwt("refresh", nickname,86400000L);

        addRefreshEntity(nickname, refresh, 86400000L);

        response.setHeader("access", access);
        response.addCookie(CookieUtil.createCookie("refresh", refresh));
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
        response.setStatus(401);

    }

    //로그인 성공했을 때  새로운 토큰저장
    private void addRefreshEntity(String nickname, String refresh, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshEntity refreshEntity = new RefreshEntity();
        refreshEntity.setNickname(nickname);
        refreshEntity.setRefresh(refresh);
        refreshEntity.setExpiration(date.toString());

        refreshRepository.save(refreshEntity);
    }
}
