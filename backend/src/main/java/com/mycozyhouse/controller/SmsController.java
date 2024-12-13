package com.mycozyhouse.controller;

import com.mycozyhouse.dto.UserDto;
import com.mycozyhouse.service.SmsService;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sms")
@RequiredArgsConstructor
public class SmsController {

    private final SmsService smsService;

    // 인증코드 발급
    @PostMapping("/send")
    public ResponseEntity<String> sendSms(@RequestBody UserDto UserDTO) {
        String verificationCode = smsService.generateVerificationCode();
        SingleMessageSentResponse response = smsService.sendOne(UserDTO.getPhone(), verificationCode);

        if (response != null && response.getStatusCode().equals("2000")) {
            return ResponseEntity.ok("인증 코드가 발송되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("SMS 발송에 실패했습니다.");
        }
    }

    // 인증코드 검증
    @PostMapping("/verify")
    public ResponseEntity<String> verifyCode(@RequestBody UserDto UserDTO) {
        boolean isValid = smsService.verifyCode(UserDTO.getPhone(), UserDTO.getVerificationCode());

        if (isValid) {
            return ResponseEntity.ok("인증에 성공했습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("인증 코드가 일치하지 않습니다.");
        }
    }
}
