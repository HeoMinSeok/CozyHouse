package com.mycozhouse.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;


@RequiredArgsConstructor
public class OAuth2UserDTO implements OAuth2User {

    private final UserDTO userDTO;

    // 각 소셜 제공자마다 응답하는 Attributes 형태가 다르기 때문에 사용하지 않음
    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    // 사용자 권한을 반환하는 메서드
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>(); // 권한 컬렉션 생성

        // 사용자 역할을 권한으로 추가
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return userDTO.getStatus().toString();
            }
        });
        return collection; // 생성한 권한 컬렉션 반환
    }

    // 사용자 이름을 반환하는 메서드
    @Override
    public String getName() {
        return userDTO.getNickname();
    }

    // 사용자 이메일반환
    public String getEmail() {
        return userDTO.getEmail();
    }

//    // 카테고리를 반환하는 메서드 추가
//    public String getCategory() {
//        return userDTO.getCategory();
//    }
}