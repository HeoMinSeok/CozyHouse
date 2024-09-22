package com.mycozyhouse.service;

import com.mycozyhouse.dto.MediaPostDto;
import com.mycozyhouse.entity.ImageEntity;
import com.mycozyhouse.entity.MediaPostEntity;
import com.mycozyhouse.entity.UserEntity;
import com.mycozyhouse.entity.VideoEntity;
import com.mycozyhouse.repository.MediaPostRepository;
import com.mycozyhouse.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    public void saveMediaPost(String mediaContent, String location, List<MultipartFile> files) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByNickname(username);

        MediaPostEntity mediaPostEntity = buildMediaPost(mediaContent, location);
        user.addMediaPosts(mediaPostEntity);

        // 파일 리스트를 순회하면서 처리
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                String storedFileName = fileUploadService.storeFile(file);
                String contentType = file.getContentType();

                if (contentType == null) {
                    throw new IllegalArgumentException("파일의 MIME 타입을 확인할 수 없습니다: " + file.getOriginalFilename());
                }
                // 파일 타입에 따라 이미지 또는 비디오로 처리
                if (isImage(contentType)) {
                    linkImageToPost(storedFileName, mediaPostEntity);
                } else if (isVideo(contentType)) {
                    linkVideoToPost(storedFileName, mediaPostEntity);
                }
            }
        }
        mediaPostRepository.save(mediaPostEntity);
    }

    // 이미지 MIME 타입 체크
    private boolean isImage(String contentType) {
        return contentType.startsWith("image/");
    }

    // 비디오 MIME 타입 체크
    private boolean isVideo(String contentType) {
        return contentType.startsWith("video/");
    }

    private MediaPostEntity buildMediaPost(String mediaContent, String location) {
        MediaPostEntity mediaPostEntity = new MediaPostEntity();
        mediaPostEntity.setMediaContent(mediaContent);
        mediaPostEntity.setLocation(location);
        return mediaPostEntity;
    }

    private void linkImageToPost(String imagePath, MediaPostEntity mediaPostEntity) {
        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setImageUrl(imagePath);
        imageEntity.setMediaPost(mediaPostEntity);
        mediaPostEntity.addImage(imageEntity);
    }

    private void linkVideoToPost(String videoPath, MediaPostEntity mediaPostEntity) {
        VideoEntity videoEntity = new VideoEntity();
        videoEntity.setVideoUrl(videoPath);
        videoEntity.setMediaPost(mediaPostEntity);
        mediaPostEntity.addVideo(videoEntity);
    }
}