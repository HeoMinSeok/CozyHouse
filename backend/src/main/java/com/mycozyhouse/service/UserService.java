package com.mycozyhouse.service;

import com.mycozyhouse.dto.ProviderType;
import com.mycozyhouse.dto.UserDto;
import com.mycozyhouse.dto.UserStatus;
import com.mycozyhouse.entity.UserEntity;
import com.mycozyhouse.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 회원가입
    public UserDto signup(UserDto userDTO){
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new DataIntegrityViolationException("이미 존재하는 이메일입니다.");
        }

        if (userRepository.existsByNickname(userDTO.getNickname())) {
            throw new DataIntegrityViolationException("이미 존재하는 닉네임입니다.");
        }

        UserEntity user = new UserEntity();

        user.setEmail(userDTO.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
        user.setNickname(userDTO.getNickname());
        user.setPhone(userDTO.getPhone());
        user.setStatus(UserStatus.MEMBER);
        user.setProvider(ProviderType.NORMAL);

        UserEntity userEntity = userRepository.save(user);

        UserDto dto = new UserDto();
        userDTO.setId(userEntity.getId());
        dto.setEmail(userEntity.getEmail());
        dto.setPassword(userEntity.getPassword());
        dto.setNickname(userEntity.getNickname());
        dto.setPhone(userEntity.getPhone());
        dto.setStatus(userEntity.getStatus());
        dto.setProvider(userEntity.getProvider());

        return dto;
    }

    //사용자 정보
    public UserDto getUserInfo(String nickname) {
        UserEntity userEntity = userRepository.findByNickname(nickname);

        UserDto userDTO = new UserDto();
        userDTO.setId(userEntity.getId());
        userDTO.setEmail(userEntity.getEmail());
        userDTO.setNickname(userEntity.getNickname());
        userDTO.setPhone(userEntity.getPhone());
        userDTO.setStatus(userEntity.getStatus());
        userDTO.setProvider(userEntity.getProvider());

        return userDTO;
    }
}
