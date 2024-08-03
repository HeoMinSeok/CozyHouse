package com.mycozhouse.dto;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class KakaoDTO implements OAuth2DTO {

/*
    {
        "id": 123456789,
            "properties": {
        "nickname": "사용자명"
    },
        "kakao_account": {
        "email": "user@example.com",
                "profile_nickname": "사용자명"
    }
    }
    */

    private final Map<String, Object> attributes;

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        // "id"는 카카오에서 반환하는 사용자 ID입니다.
        return attributes.get("id").toString();
    }

    @Override
    public String getEmail() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        return kakaoAccount != null ? (String) kakaoAccount.get("email") : null;
    }

    @Override
    public String getName() {
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
        return properties != null ? (String) properties.get("nickname") : null;
    }
}