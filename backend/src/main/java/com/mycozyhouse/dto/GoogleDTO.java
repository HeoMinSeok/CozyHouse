package com.mycozyhouse.dto;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class GoogleDTO implements OAuth2DTO {

    private final Map<String, Object> attribute;

    /*
    구글 데이터: JSON
    {
        "sub": "1234567890",
        "name": "이병훈",
        "email": "user@example.com"
    }
    */

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getProviderId() {
        Object providerId = attribute.get("sub");

        return providerId != null ? providerId.toString() : null;
    }

    @Override
    public String getEmail() {
        Object email = attribute.get("email");

        return email != null ? email.toString() : null;
    }

    @Override
    public String getName() {
        Object name = attribute.get("name");

        return name != null ? name.toString() : null;
    }
}