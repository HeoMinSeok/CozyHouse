package com.mycozyhouse.controller;

import com.mycozyhouse.config.CustomUserDetails;
import com.mycozyhouse.dto.UserDto;
import com.mycozyhouse.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    //회원가입
    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@RequestBody @Valid UserDto userDTO) {
        UserDto dto = userService.signup(userDTO);
        return ResponseEntity.ok(dto);
    }

   //토큰으로 사용자 정보 조회
    @GetMapping
    public ResponseEntity<UserDto> localUserInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {

        UserDto userInfo = userService.getUserInfo(userDetails.getUsername());
        return ResponseEntity.ok(userInfo);
    }
}
