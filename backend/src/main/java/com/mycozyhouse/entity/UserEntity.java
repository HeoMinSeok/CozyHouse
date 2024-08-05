package com.mycozyhouse.entity;

import com.mycozyhouse.dto.ProviderType;
import com.mycozyhouse.dto.UserStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    private String email;
    private String password;
    private String phone;
    private String nickname;

    //Enumerated - 열거형(enum) 타입의 필드를 데이터베이스에 매핑할 때 사용
    @Enumerated(EnumType.STRING) //순서를 변경하거나 상수를 추가해도 기존 데이터와의 호환성을 유지 거의 STRING
    private UserStatus status; //회원상태 [MEMBER, NON_MEMBER]

    @Enumerated(EnumType.STRING)
    private ProviderType provider; // 소셜 로그인 제공자

}
