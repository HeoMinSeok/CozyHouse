package com.mycozyhouse.dto;

import lombok.RequiredArgsConstructor;
import java.util.Map;
@RequiredArgsConstructor
public class NaverDto implements OAuth2Dto {

    private final Map<String, Object> attribute;

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
        Map<String, Object> response = (Map<String, Object>) attribute.get("response");
        if (response != null && response.containsKey(key)) {
            Object value = response.get(key);
            return value != null ? value.toString() : null;
        }
        return null;
    }
}