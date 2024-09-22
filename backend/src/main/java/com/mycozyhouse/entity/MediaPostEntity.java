package com.mycozyhouse.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "MediaPost")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MediaPostEntity extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "media_post_id")
    private Long id;
    private String mediaContent;
    private String location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToOne(mappedBy = "mediaPost", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private VideoEntity video;

    @OneToMany(mappedBy = "mediaPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImageEntity> images = new ArrayList<>();

    public void addImage(ImageEntity imageEntity){
        images.add(imageEntity);
        imageEntity.setMediaPost(this);
    }

    public void addVideo(VideoEntity videoEntity) {
        this.video = videoEntity;
        videoEntity.setMediaPost(this);
    }
}
