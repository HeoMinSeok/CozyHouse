package com.mycozyhouse.repository;

import com.mycozyhouse.dto.ProviderType;
import com.mycozyhouse.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Boolean existsByEmail(String Email);
    UserEntity findByEmail(String email);
    UserEntity findByNickname(String nickname);
    Boolean existsByNickname(String nickname);
    UserEntity findByEmailAndProvider(String email, ProviderType provider);
}
