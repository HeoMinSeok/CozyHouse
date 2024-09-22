package com.mycozyhouse.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "video")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "video_id")
    private Long id;
    private String videoUrl;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "media_post_id")
    private MediaPostEntity mediaPost;
}
