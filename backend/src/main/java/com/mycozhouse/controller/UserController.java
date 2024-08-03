package com.mycozhouse.controller;


import com.mycozhouse.dto.UserDTO;
import com.mycozhouse.repository.UserRepository;
import com.mycozhouse.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signup(@RequestBody UserDTO userDTO) {
        UserDTO dto = userService.signup(userDTO); // 예외가 발생할 경우, GlobalExceptionHandler 가 처리
        return ResponseEntity.ok(dto);
    }
//    @GetMapping("/login") // GET 요청에 대한 매핑
//    public String loginPage() {
//        return "login"; // login.html 파일을 반환
//    }
}
