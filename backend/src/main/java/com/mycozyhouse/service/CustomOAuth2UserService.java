package com.mycozyhouse.service;

import com.mycozyhouse.config.OAuth2UserDetails;
import com.mycozyhouse.dto.*;
import com.mycozyhouse.entity.UserEntity;
import com.mycozyhouse.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

            OAuth2User oAuth2User = super.loadUser(userRequest);

            String registrationId = userRequest.getClientRegistration().getRegistrationId();

            OAuth2Dto oAuth2Response = getOAuth2Response(oAuth2User, registrationId);
            if (oAuth2Response == null) {
                return null;
            }

            String nickname  = oAuth2Response.getName() + oAuth2Response.getProviderId();
            UserEntity userEntity = userRepository.findByNickname(nickname);

            if (userEntity == null) {
                userEntity = createUserEntity(oAuth2Response, registrationId);
            } else {
                updateUserEntity(userEntity, oAuth2Response, registrationId);
            }

            userRepository.save(userEntity);

            return new OAuth2UserDetails(createUserDTO(userEntity, oAuth2Response));

    }

    private OAuth2Dto getOAuth2Response(OAuth2User oAuth2User, String registrationId) {
        switch (registrationId) {
            case "google":
                return new GoogleDto(oAuth2User.getAttributes());
            case "kakao":
                return new KakaoDto(oAuth2User.getAttributes());
            case "github":
                return new GithubDto(oAuth2User.getAttributes());
            case "naver":
                return new NaverDto(oAuth2User.getAttributes());
            default:
                return null;
        }
    }

    private UserEntity createUserEntity(OAuth2Dto oAuth2Response, String registrationId) {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(oAuth2Response.getEmail());
        userEntity.setNickname(oAuth2Response.getName()+oAuth2Response.getProviderId());
        userEntity.setStatus(UserStatus.MEMBER);
        userEntity.setProvider(getProviderType(registrationId));
        return userEntity;
    }

    private void updateUserEntity(UserEntity userEntity, OAuth2Dto oAuth2Response, String registrationId) {
        userEntity.setEmail(oAuth2Response.getEmail());
        userEntity.setNickname(oAuth2Response.getName()+oAuth2Response.getProviderId());
        userEntity.setProvider(getProviderType(registrationId));
    }

    private UserDto createUserDTO(UserEntity userEntity, OAuth2Dto oAuth2Response) {

        UserDto userDTO = new UserDto();
        userDTO.setEmail(oAuth2Response.getEmail());
        userDTO.setNickname(oAuth2Response.getName()+oAuth2Response.getProviderId());
        userDTO.setStatus(userEntity.getStatus());
        userDTO.setProvider(userEntity.getProvider());
        return userDTO;
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
                return null;
        }
    }
}