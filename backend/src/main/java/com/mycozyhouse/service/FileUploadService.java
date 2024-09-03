package com.mycozyhouse.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FileUploadService {

    @Value("${file.dir}")
    private String fileDir;

    // 파일 이름을 받아서 전체 경로를 반환하는 메서드
    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    @Transactional
    // 여러 MultipartFile을 받아서 저장하고, 저장된 파일 정보를 리스트로 반환하는 메서드
    public List<String> storeFiles(List<MultipartFile> multipartFiles)  {
        List<String> storeFileResult = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) { // 파일이 비어있지 않은 경우만 저장
                storeFileResult.add(storeFile(multipartFile));
            }
        }
        return storeFileResult;
    }

    // 단일 MultipartFile을 저장하고, 저장된 파일 정보를 반환하는 메서드
    public String storeFile(MultipartFile multipartFile)  {
        if (multipartFile.isEmpty()) {
            return null; // 파일이 비어있으면 null 반환
        }
        try {
            String originalFilename = multipartFile.getOriginalFilename();
            String storeFileName = createStoreFileName(originalFilename);// 저장할 파일 이름 생성
            multipartFile.transferTo(new File(getFullPath(storeFileName)));// 파일을 지정된 경로에 저장
            return storeFileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file: " + e.getMessage(), e);
        }
    }

    // 원본 파일 이름을 받아서 저장할 파일 이름을 생성하는 메서드
    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename); // 파일 확장자 추출
        String baseName = originalFilename.substring(0, originalFilename.lastIndexOf('.')); //확장자를 뺀 파일명만
        String uuid = UUID.randomUUID().toString(); // 고유한 UUID 생성
        return baseName + "_" + uuid + "." + ext; // 파일명, UUID, 확장자를 결합하여 저장할 파일 이름 생성
    }

    // 원본 파일 이름에서 확장자를 추출하는 메서드
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
