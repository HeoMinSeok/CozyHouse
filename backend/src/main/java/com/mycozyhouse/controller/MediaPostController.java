package com.mycozyhouse.controller;

import com.mycozyhouse.dto.MediaPostDto;
import com.mycozyhouse.service.MediaPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MediaPostController {

    private final MediaPostService mediaPostService;

    //글쓰기 작성
    @PostMapping("/uploadImages")
    public ResponseEntity<String> uploadImage(@ModelAttribute MediaPostDto MediaPostDto){

        mediaPostService.saveMediaPost(MediaPostDto);

        return ResponseEntity.ok("글작성이 완료되었습니다");
    }
}
