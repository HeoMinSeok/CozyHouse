package com.mycozhouse.service;


import com.mycozhouse.config.CustomUserDetails;
import com.mycozhouse.entity.UserEntity;
import com.mycozhouse.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("Attempting to load user: " + email);
        // 사용자 이름(username)을 통해 데이터베이스에서 사용자 정보를 조회
        UserEntity userData = userRepository.findByEmail(email);

        // 조회된 사용자 정보가 존재하면 CustomUserDetails 객체로 변환하여 반환
        if (userData != null) {
            return new CustomUserDetails(userData);
        }

        // 조회된 사용자 정보가 없으면 null을 반환 (Spring Security는 이 경우 인증 실패로 처리)
        throw new UsernameNotFoundException("User not found: " + email);    }
}
