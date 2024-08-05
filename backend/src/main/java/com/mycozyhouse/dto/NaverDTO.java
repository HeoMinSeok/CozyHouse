package com.mycozyhouse.dto;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class NaverDTO implements OAuth2DTO {

    private final Map<String, Object> attribute;

    /*
    네이버 데이터: json 형식
    {
        resultcode: "00",
        message: "success",
        response: {
            id: "123123123",
            name: "이병훈",
            email: "user@example.com"
        }
    }
    */

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getProviderId() {
        return getResponseValue("id");
    }

    @Override
    public String getEmail() {
        return getResponseValue("email");
    }

    @Override
    public String getName() {
        return getResponseValue("name");
    }

    private String getResponseValue(String key) {
        // response 필드를 가져옴
        Map<String, Object> response = (Map<String, Object>) attribute.get("response");
        if (response != null && response.containsKey(key)) {
            Object value = response.get(key);
            return value != null ? value.toString() : null; // null 체크
        }
        return null; // response가 null이거나 키가 존재하지 않는 경우
    }
}