package com.mycozyhouse.service;

import com.mycozyhouse.entity.RefreshEntity;
import com.mycozyhouse.jwt.JWTUtil;
import com.mycozyhouse.repository.RefreshRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class ReissueService {

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    @Transactional
    public String reissueToken(String refreshToken) {

        // Refresh token 검증
        if (jwtUtil.isExpired(refreshToken)) {
            throw new IllegalArgumentException("Refresh token has expired");
        }

        // 카테고리 검증
        String category = jwtUtil.getCategory(refreshToken);
        if (!category.equals("refresh")) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        // Refresh token 존재 여부 검증
        if (!refreshRepository.existsByRefresh(refreshToken)) {
            throw new IllegalArgumentException("Refresh token not found");
        }

        // 사용자 정보 추출
        String nickname = jwtUtil.getNickname(refreshToken);

        // 새로운 토큰 생성
        String newAccess = jwtUtil.createJwt("access", nickname, 50000L);
        String newRefresh = jwtUtil.createJwt("refresh", nickname, 86400000L);

        // 기존 Refresh token 삭제 및 새로운 Refresh token 저장
        refreshRepository.deleteByRefresh(refreshToken);
        addRefreshEntity(nickname, newRefresh, 86400000L);

        return newAccess;
    }

    public void addRefreshEntity(String nickname, String refresh, Long expiredMs) {
        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshEntity refreshEntity = new RefreshEntity();
        refreshEntity.setNickname(nickname);
        refreshEntity.setRefresh(refresh);
        refreshEntity.setExpiration(date.toString());

        refreshRepository.save(refreshEntity);
    }

    @Transactional
    public String createNewRefreshToken(String refreshToken) {
        String nickname = jwtUtil.getNickname(refreshToken);
        String newRefresh = jwtUtil.createJwt("refresh", nickname, 86400000L);
        addRefreshEntity(nickname, newRefresh, 86400000L);
        return newRefresh;
    }
}