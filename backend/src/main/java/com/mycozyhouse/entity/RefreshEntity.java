package com.mycozyhouse.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

//토큰 저장소
@Entity
@Getter
@Setter
public class RefreshEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //어떤 유저의 토큰인지
    private String email;
    //유저가 가지는 토큰 필드
    private String refresh;
    //토큰의 만료시간
    private String expiration;

}