package com.mycozyhouse.dto;

import com.mycozyhouse.entity.ImageEntity;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class MediaPostDto {

    private String comment;
    private String location;
    private List<MultipartFile> images;
}
