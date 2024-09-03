package com.mycozyhouse.repository;

import com.mycozyhouse.entity.RefreshEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshRepository extends JpaRepository<RefreshEntity, Long> {

    Boolean existsByRefresh(String refresh);

    void deleteByRefresh(String refresh);
}