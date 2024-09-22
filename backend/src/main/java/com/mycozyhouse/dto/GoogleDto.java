package com.mycozyhouse.dto;

import lombok.RequiredArgsConstructor;
import java.util.Map;

@RequiredArgsConstructor
public class GoogleDto implements OAuth2Dto {

    private final Map<String, Object> attribute;

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