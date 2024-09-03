package com.mycozyhouse.repository;

import com.mycozyhouse.entity.MediaPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaPostRepository extends JpaRepository<MediaPostEntity, Long> {
}