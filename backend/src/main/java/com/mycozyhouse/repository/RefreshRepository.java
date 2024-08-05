package com.mycozyhouse.repository;

import com.mycozyhouse.entity.RefreshEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshRepository extends JpaRepository<RefreshEntity, Long> {

    //토큰이 존재하는 지
    Boolean existsByRefresh(String refresh);

    //데이터베이스의 토큰 삭제
    @Transactional
    void deleteByRefresh(String refresh);
}