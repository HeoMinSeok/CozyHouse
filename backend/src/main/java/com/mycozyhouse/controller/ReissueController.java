package com.mycozyhouse.controller;

import com.mycozyhouse.dto.UserStatus;
import com.mycozyhouse.entity.RefreshEntity;
import com.mycozyhouse.jwt.JWTUtil;
import com.mycozyhouse.repository.RefreshRepository;
import com.mycozyhouse.service.ReissueService;
import com.mycozyhouse.utill.CookieUtil;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

//나중에 컨트롤과 서비스 계층 나누기
//리프레쉬 만료된 토큰 삭제하는 스케쥴 추가하기
@RestController
@RequiredArgsConstructor
public class ReissueController {

    private final ReissueService reissueService;

    @PostMapping("/reissue")
    public ResponseEntity<String> reissue(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = CookieUtil.getCookieValue(request, "refresh");

        if (refreshToken == null) {
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

            String newAccess = reissueService.reissueToken(refreshToken);
            String newRefresh = reissueService.createNewRefreshToken(refreshToken);
            response.addCookie(CookieUtil.createCookie("refresh", newRefresh));
            response.addHeader("access", newAccess);

        return new ResponseEntity<>("Tokens reissued successfully", HttpStatus.OK);
    }

    @GetMapping("/change")
    public ResponseEntity<String> change(HttpServletRequest request) {
        String accessToken = CookieUtil.getCookieValue(request, "access");

        HttpHeaders headers = new HttpHeaders();
        headers.set("access", accessToken);

        return ResponseEntity.ok()
                .headers(headers)
                .body("main route");
    }
}

