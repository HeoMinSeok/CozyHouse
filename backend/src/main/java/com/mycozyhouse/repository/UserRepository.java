package com.mycozyhouse.repository;

import com.mycozyhouse.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Boolean existsByEmail(String Email);

    UserEntity findByEmail(String Email);
}
