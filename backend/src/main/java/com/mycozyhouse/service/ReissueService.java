package com.mycozyhouse.service;

import com.mycozyhouse.entity.RefreshEntity;
import com.mycozyhouse.jwt.JWTUtil;
import com.mycozyhouse.repository.RefreshRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReissueService {

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    @Transactional
    public Map<String, String> reissueToken(String refreshToken) {

        String nickname = jwtUtil.getNickname(refreshToken);

        String access = jwtUtil.createJwt("access", nickname, 50000L);
        String refresh = jwtUtil.createJwt("refresh", nickname, 86400000L);

        refreshRepository.deleteByRefresh(refreshToken);
        addRefreshEntity(nickname, refresh, 86400000L);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access", access);
        tokens.put("refresh", refresh);

        return tokens;
    }

    public void addRefreshEntity(String nickname, String refresh, Long expiredMs) {
        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshEntity refreshEntity = new RefreshEntity();
        refreshEntity.setNickname(nickname);
        refreshEntity.setRefresh(refresh);
        refreshEntity.setExpiration(date.toString());

        refreshRepository.save(refreshEntity);
    }
}