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

    //리프레쉬 로테이션
    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        String refresh = null;
        Cookie[] cookies = request.getCookies();

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
            }
        }

        if (refresh == null) {
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }

        String category = jwtUtil.getCategory(refresh);

        if (!category.equals("refresh")) {
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        Boolean isExist = refreshRepository.existsByRefresh(refresh);
        if (!isExist) {
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        String nickname = jwtUtil.getNickname(refresh);

        String newAccess = jwtUtil.createJwt("access", nickname, 600000L);
        String newRefresh = jwtUtil.createJwt("refresh", nickname, 86400000L);

        refreshRepository.deleteByRefresh(refresh);
        addRefreshEntity(nickname, newRefresh, 86400000L);

        response.addCookie(createCookie("access", newAccess));
        response.addCookie(createCookie("refresh", newRefresh));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setHttpOnly(true);
        return cookie;
    }

    private void addRefreshEntity(String nickname, String refresh, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshEntity refreshEntity = new RefreshEntity();
        refreshEntity.setNickname(nickname);
        refreshEntity.setRefresh(refresh);
        refreshEntity.setExpiration(date.toString());

        refreshRepository.save(refreshEntity);
    }

    //accessToken 쿠키를 응답헤더로 반환
    @GetMapping("/change")
    @ResponseBody
    public ResponseEntity<String> change(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        String accessToken = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("access")) {
                    accessToken = cookie.getValue();
                    break;
                }
            }
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("access", accessToken);

        return ResponseEntity.ok()
                .headers(headers)
                .body("main route");
    }
}

