package com.mycozhouse.config;


import com.mycozhouse.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final UserEntity userEntity;

    // 사용자에게 부여된 권한을 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        // 사용자의 권한 정보를 저장할 컬렉션 객체 생성
        Collection<GrantedAuthority> collection = new ArrayList<>();

        // 사용자의 역할 정보를 담은 GrantedAuthority 객체를 컬렉션에 추가
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                // 사용자의 역할을 반환
                return userEntity.getStatus().toString();
            }
        });

        // 권한 정보를 담은 컬렉션 반환
        return collection;
    }

    // 사용자의 비밀번호를 반환
    @Override
    public String getPassword() {

        return userEntity.getPassword();
    }

    // 사용자의 아이디를 반환
    @Override
    public String getUsername() {
        return userEntity.getNickname();
    }

    // 계정이 만료되지 않았는지를 반환
    // 기본 구현을 호출하여 반환하고 있어, 기본적으로 계정이 만료되지 않은 상태
    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    // 계정이 잠기지 않았는지를 반환
    // 기본 구현을 호출하여 반환하고 있어, 기본적으로 계정이 잠기지 않은 상태
    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    // 자격 증명이 만료되지 않았는지를 반환
    // 기본 구현을 호출하여 반환하고 있어, 기본적으로 자격 증명이 만료되지 않은 상태
    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    // 계정이 활성화되었는지를 반환
    // 기본 구현을 호출하여 반환하고 있어, 기본적으로 계정이 활성화된 상태
    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
