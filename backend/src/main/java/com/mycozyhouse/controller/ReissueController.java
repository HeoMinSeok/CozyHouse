package com.mycozyhouse.controller;

import com.mycozyhouse.service.ReissueService;
import com.mycozyhouse.utill.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ReissueController {

    private final ReissueService reissueService;

    @PostMapping("/reissue")
    public ResponseEntity<String> reissue(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = CookieUtil.getCookieValue(request, "refresh");

        Map<String, String> tokens = reissueService.reissueToken(refreshToken);
        String refresh = tokens.get("refresh");
        String access = tokens.get("access");

        response.addCookie(CookieUtil.createCookie("refresh", refresh));
        response.addHeader("access", access);

        return new ResponseEntity<>("Tokens reissued successfully", HttpStatus.OK);
    }

    @GetMapping("/change")
    public ResponseEntity<String> change(HttpServletResponse response) {

        Cookie cookie = new Cookie("access", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        return new ResponseEntity<>("Tokens access Change", HttpStatus.OK);
    }
}

