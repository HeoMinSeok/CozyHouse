package com.mycozyhouse.controller;

import com.mycozyhouse.service.MediaPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class MediaPostController {

    private final MediaPostService mediaPostService;

    //글쓰기 작성
    @PostMapping("/write")
    public ResponseEntity<String> uploadImage(@RequestPart("mediaContent") String mediaContent,
                                              @RequestPart("location") String location,
                                              @RequestPart("file") List<MultipartFile> files) {

        mediaPostService.saveMediaPost(mediaContent, location,  files);

        return ResponseEntity.ok("글작성이 완료되었습니다");
    }
}
