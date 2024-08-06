package com.mycozyhouse.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

//토큰 저장소
@Entity
@Getter
@Setter
@Table(name = "refresh")
public class RefreshEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nickname;
    private String refresh;
    private String expiration;

}