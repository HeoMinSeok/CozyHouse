package com.mycozyhouse.controller;

import com.mycozyhouse.dto.UserStatus;
import com.mycozyhouse.entity.RefreshEntity;
import com.mycozyhouse.jwt.JWTUtil;
import com.mycozyhouse.repository.RefreshRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

//나중에 컨트롤과 서비스 계층 나누기
//리프레쉬 만료된 토큰 삭제하는 스케쥴 추가하기
@Controller
@ResponseBody
@RequiredArgsConstructor
public class ReissueController {

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        //쿠키에서 리프레쉬 토큰 추출
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("refresh")) {

                refresh = cookie.getValue();
            }
        }

        System.out.println("refresh = " + refresh);
        // 리프레시 토큰이 없을 경우 처리
        if (refresh == null) {
            // 응답 상태 코드를 BAD_REQUEST로 설정
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        // 리프레시 토큰의 만료 여부를 확인
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            // 만료된 경우 응답 상태 코드를 BAD_REQUEST로 설정
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }

        // 토큰이 리프레시 토큰인지 확인 (발급 시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);

        // 카테고리가 "refresh"가 아닐 경우 처리
        if (!category.equals("refresh")) {
            // 응답 상태 코드를 BAD_REQUEST로 설정
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        // DB에 저장되어 있는지 확인
        Boolean isExist = refreshRepository.existsByRefresh(refresh);
        if (!isExist) {
            // response body
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        // 리프레시 토큰에서 사용자 이름과 역할을 추출
        String email = jwtUtil.getEmail(refresh);
        UserStatus status = jwtUtil.getStatus(refresh);

        // 새로운 액세스 토큰 생성
        String newAccess = jwtUtil.createJwt("access", email, status, 600000L);
        String newRefresh = jwtUtil.createJwt("refresh", email, status, 86400000L);

        // Refresh 토큰 저장 DB에 기존의 Refresh 토큰 삭제 후 새 Refresh 토큰 저장
        refreshRepository.deleteByRefresh(refresh);
        addRefreshEntity(email, newRefresh, 86400000L);

        // 응답 헤더에 새로운 액세스 토큰을 설정
        response.addCookie(createCookie("access", newAccess));
        // 리프레쉬는 쿠키로 받기 때문에 쿠키로 셋팅 > 추후 소셜로그인 일반로그인 시 응답헤더로 바꿔야함
        response.addCookie(createCookie("refresh", newRefresh));

        // 성공적으로 토큰을 재발급한 경우 OK 상태 코드 반환
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        //cookie.setSecure(true);
        //cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

    private void addRefreshEntity(String email, String refresh, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshEntity refreshEntity = new RefreshEntity();
        refreshEntity.setEmail(email);
        refreshEntity.setRefresh(refresh);
        refreshEntity.setExpiration(date.toString());

        refreshRepository.save(refreshEntity);
    }
    //accessToken 쿠키를 응답헤더로 반환
    @GetMapping("/change")
    @ResponseBody
    public ResponseEntity<String> change(HttpServletRequest request) {

        System.out.println("컨트롤실행");
        // 요청에서 쿠키 읽기
        Cookie[] cookies = request.getCookies();
        String accessToken = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("access")) {
                    accessToken = cookie.getValue(); // 쿠키에서 access 토큰 값 읽기
                    break; // access 토큰을 찾았으면 반복 종료
                }
            }
        }

        // 응답 헤더에 access 토큰 추가
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", accessToken); // 없을 경우 대체 값 설정

        // 응답에 헤더 추가
        return ResponseEntity.ok()
                .headers(headers)
                .body("main route");
    }
}

