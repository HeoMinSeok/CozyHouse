package com.mycozyhouse.entity;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "image")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "media_post_id")
    private MediaPostEntity mediaPost;
}
