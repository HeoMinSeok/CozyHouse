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
    public void saveMediaPost(String comment, String location, List<MultipartFile> files) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByNickname(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with nickname: " + username));

        List<String> fileList = fileUploadService.storeFiles(files);

        MediaPostEntity mediaPostEntity = new MediaPostEntity();
        mediaPostEntity.setComment(comment);
        mediaPostEntity.setLocation(location);

        user.addMediaPosts(mediaPostEntity);

        // files 리스트를 순회하여 각 파일의 콘텐츠 타입 확인
        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);
            String filePath = fileList.get(i); // 저장된 파일 경로
            String contentType = file.getContentType();

            if (contentType != null) {
                if (contentType.startsWith("image/")) {
                    ImageEntity imageEntity = new ImageEntity();
                    imageEntity.setImageUrl(filePath);
                    imageEntity.setMediaPost(mediaPostEntity);
                    mediaPostEntity.addImage(imageEntity);
                } else if (contentType.startsWith("video/")) {
                    VideoEntity videoEntity = new VideoEntity();
                    videoEntity.setVideoUrl(filePath);
                    videoEntity.setMediaPost(mediaPostEntity);
                    mediaPostEntity.addVideo(videoEntity);
                }
            }
        }
        mediaPostRepository.save(mediaPostEntity);
    }
}