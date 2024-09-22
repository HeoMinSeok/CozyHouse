package com.mycozyhouse.entity;

import com.mycozyhouse.dto.ProviderType;
import com.mycozyhouse.dto.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    private String email;
    private String password;
    private String phone;
    private String nickname;
    @Enumerated(EnumType.STRING)
    private UserStatus status;
    @Enumerated(EnumType.STRING)
    private ProviderType provider;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL ,orphanRemoval = true)
    private List<MediaPostEntity> mediaPosts = new ArrayList<>();

    public void addMediaPosts(MediaPostEntity mediaPostEntity){
        mediaPosts.add(mediaPostEntity);
        mediaPostEntity.setUser(this);
    }
}
