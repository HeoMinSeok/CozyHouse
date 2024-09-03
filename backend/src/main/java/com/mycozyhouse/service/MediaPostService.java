package com.mycozyhouse.service;

import com.mycozyhouse.dto.MediaPostDto;
import com.mycozyhouse.entity.ImageEntity;
import com.mycozyhouse.entity.MediaPostEntity;
import com.mycozyhouse.entity.UserEntity;
import com.mycozyhouse.repository.MediaPostRepository;
import com.mycozyhouse.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MediaPostService {

    private final FileUploadService fileUploadService;
    private final UserRepository userRepository;
    private final MediaPostRepository mediaPostRepository;

    @Transactional
    public void saveMediaPost(MediaPostDto mediaPostDto)  {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByNickname(username).orElseThrow(() -> new UsernameNotFoundException("User not found with nickname: " + username));

        List<String> imageList = fileUploadService.storeFiles(mediaPostDto.getImages());

        MediaPostEntity mediaPostEntity = new MediaPostEntity();
        mediaPostEntity.setComment(mediaPostDto.getComment());
        mediaPostEntity.setLocation(mediaPostDto.getLocation());

        user.addMediaPosts(mediaPostEntity);

        for (String imagePath : imageList) {
            ImageEntity imageEntity = new ImageEntity();
            imageEntity.setMediaUrl(imagePath);
            imageEntity.setMediaPost(mediaPostEntity);
            mediaPostEntity.addImage(imageEntity);
        }

        mediaPostRepository.save(mediaPostEntity);
    }
}
