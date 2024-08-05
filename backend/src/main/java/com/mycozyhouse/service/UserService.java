package com.mycozyhouse.service;

import com.mycozyhouse.dto.ProviderType;
import com.mycozyhouse.dto.UserDTO;
import com.mycozyhouse.dto.UserDTO;
import com.mycozyhouse.dto.UserStatus;
import com.mycozyhouse.entity.UserEntity;
import com.mycozyhouse.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 회원가입
    public UserDTO signup(UserDTO userDTO){
        // 이메일 중복 체크
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new DataIntegrityViolationException("이미 존재하는 이메일입니다.");
        }

        // UserEntity 객체 생성
        UserEntity user = new UserEntity();

        user.setEmail(userDTO.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
        user.setNickname(userDTO.getNickname());
        user.setPhone(userDTO.getPhone());

        // 기본 상태를 MEMBER로 설정
        user.setStatus(UserStatus.MEMBER);
        user.setProvider(ProviderType.NORMAL);

        // 데이터베이스에 저장
        UserEntity save = userRepository.save(user);

        UserDTO dto = new UserDTO();
        dto.setEmail(save.getEmail());
        dto.setPassword(save.getPassword());
        dto.setNickname(save.getNickname());
        dto.setPhone(save.getPhone());
        dto.setStatus(save.getStatus());
        dto.setProvider(save.getProvider());

        return dto;
    }

    @Transactional
    public UserDTO getUserInfo(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);

        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(userEntity.getEmail());
        userDTO.setNickname(userEntity.getNickname());
        userDTO.setPhone(userEntity.getPhone());
        userDTO.setStatus(userEntity.getStatus());
        userDTO.setProvider(userEntity.getProvider());

        return userDTO; // 조회한 사용자 정보를 반환
    }
}
