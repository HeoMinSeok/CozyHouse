package com.mycozyhouse.service;

import com.mycozyhouse.dto.*;
import com.mycozyhouse.entity.UserEntity;
import com.mycozyhouse.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
//소셜로그인한 유저정보를 얻는 클래스
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    // 사용자 정보를 로드하는 메서드
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        try {
            // OAuth2UserRequest를 통해 기본 사용자 정보를 로드
            OAuth2User oAuth2User = super.loadUser(userRequest);
            System.out.println("oAuth2User: " + oAuth2User); // 로드한 사용자 정보를 콘솔에 출력
            System.out.println("oAuth2User Attributes: " + oAuth2User.getAttributes());

            // 클라이언트 등록 ID를 가져옴 (예: naver, google, kakao, github)
            String registrationId = userRequest.getClientRegistration().getRegistrationId();
            System.out.println("Registration ID: " + registrationId); // 추가된 로그

            // 각 프로바이더에 대한 사용자 정보 응답 처리
            OAuth2DTO oAuth2Response = getOAuth2Response(oAuth2User, registrationId);
            if (oAuth2Response == null) {
                return null; // 지원하지 않는 서비스인 경우 null 반환
            }

            // 리소스 서버에서 발급받은 정보로 사용자를 특정할 아이디값을 만듬
            UserEntity userEntity = userRepository.findByEmail(oAuth2Response.getEmail());

            // UserEntity가 존재하지 않으면 새로 생성하고, 존재하면 업데이트
            if (userEntity == null) {
                userEntity = createUserEntity(oAuth2Response, registrationId);
            } else {
                updateUserEntity(userEntity, oAuth2Response, registrationId);
            }

            // 사용자 정보를 저장
            userRepository.save(userEntity);

            // DTO 생성 및 반환
            return new OAuth2UserDTO(createUserDTO(userEntity, oAuth2Response));

        } catch (OAuth2AuthenticationException e) {
            throw e; // 예외를 다시 던져서 AuthenticationEntryPoint에서 처리할 수 있도록 함
        }
    }

    private OAuth2DTO getOAuth2Response(OAuth2User oAuth2User, String registrationId) {
        switch (registrationId) {
            case "google":
                return new GoogleDTO(oAuth2User.getAttributes());
            case "kakao":
                return new KakaoDTO(oAuth2User.getAttributes());
            case "github":
                return new GithubDTO(oAuth2User.getAttributes());
            case "naver":
                return new NaverDTO(oAuth2User.getAttributes());
            default:
                return null; // 지원하지 않는 서비스인 경우
        }
    }

    private UserEntity createUserEntity(OAuth2DTO oAuth2Response, String registrationId) {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(oAuth2Response.getEmail());
        userEntity.setNickname(oAuth2Response.getName()+oAuth2Response.getProviderId());
        userEntity.setStatus(UserStatus.MEMBER);
        userEntity.setProvider(getProviderType(registrationId));
        return userEntity;
    }

    private void updateUserEntity(UserEntity userEntity, OAuth2DTO oAuth2Response, String registrationId) {
        userEntity.setEmail(oAuth2Response.getEmail());
        userEntity.setNickname(oAuth2Response.getName());
        userEntity.setProvider(getProviderType(registrationId));
    }

    private UserDTO createUserDTO(UserEntity userEntity, OAuth2DTO oAuth2Response) {
        UserDTO UserDTO = new UserDTO();
        UserDTO.setEmail(userEntity.getEmail());
        UserDTO.setNickname(userEntity.getNickname());
        UserDTO.setStatus(userEntity.getStatus());
        UserDTO.setProvider(userEntity.getProvider());
        return UserDTO;
    }

    private ProviderType getProviderType(String registrationId) {
        switch (registrationId) {
            case "google":
                return ProviderType.GOOGLE;
            case "kakao":
                return ProviderType.KAKAO;
            case "github":
                return ProviderType.GITHUB;
            case "naver":
                return ProviderType.NAVER;
            default:
                return null; // 지원하지 않는 서비스인 경우
        }
    }
}