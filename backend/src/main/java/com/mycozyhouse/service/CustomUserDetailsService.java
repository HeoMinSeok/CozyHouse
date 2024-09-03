package com.mycozyhouse.service;


import com.mycozyhouse.config.CustomUserDetails;
import com.mycozyhouse.dto.ProviderType;
import com.mycozyhouse.entity.UserEntity;
import com.mycozyhouse.repository.UserRepository;
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

        UserEntity userData = userRepository.findByEmailAndProvider(email, ProviderType.NORMAL);

        if (userData != null) {
            return new CustomUserDetails(userData);
        }

        throw new UsernameNotFoundException("User not found: " + email);
    }
}
