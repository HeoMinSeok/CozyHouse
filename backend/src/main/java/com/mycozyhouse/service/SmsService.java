package com.mycozyhouse.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class SmsService {
    @Value("${coolsms.api.key}")
    private String apiKey;

    @Value("${coolsms.api.secret}")
    private String apiSecretKey;

    @Value("${coolsms.from}")
    private String from;

    private DefaultMessageService messageService;

    private final RedisTemplate<String, String> redisTemplate;

    @PostConstruct
    private void init(){
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecretKey, "https://api.coolsms.co.kr");
    }

    // 인증 코드 생성
    public String generateVerificationCode() {
        SecureRandom random = new SecureRandom();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }

    // 단일 메시지 발송
    public SingleMessageSentResponse sendOne(String to, String verificationCode) {
        Message message = new Message();
        message.setFrom(from);
        message.setTo(to);
        message.setText("[Cozy House] 아래의 인증번호를 입력해주세요\n" + verificationCode);

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));

        ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
        valueOps.set(to, verificationCode, 5, TimeUnit.MINUTES);

        return response;
    }

    // 인증코드 검증
    public boolean verifyCode(String phoneNumber, String code) {
        ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
        String storedCode = valueOps.get(phoneNumber);

        return code.equals(storedCode);
    }
}
