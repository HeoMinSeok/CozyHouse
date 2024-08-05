package com.mycozyhouse.controller;


import com.mycozyhouse.config.CustomUserDetails;
import com.mycozyhouse.dto.UserDTO;
import com.mycozyhouse.dto.UserDTO;
import com.mycozyhouse.service.UserService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signup(@RequestBody @Valid UserDTO userDTO) {
        UserDTO dto = userService.signup(userDTO); // 예외가 발생할 경우, GlobalExceptionHandler 가 처리
        return ResponseEntity.ok(dto);
    }

    @GetMapping
//    @PreAuthorize("hasAuthority('NON_MEMBER')") // 사용자 권한 체크
    public ResponseEntity<UserDTO> getUserInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {

        UserDTO userInfo = userService.getUserInfo(userDetails.getUsername()); // 또는 다른 필드 사용 가능
        return ResponseEntity.ok(userInfo);
    }
}
