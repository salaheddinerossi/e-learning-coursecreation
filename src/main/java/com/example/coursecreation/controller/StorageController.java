package com.example.coursecreation.controller;

import com.example.coursecreation.response.PresignedUrlResponse;
import com.example.coursecreation.serviceImpl.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StorageController {

    @Value("${BUCKET_NAME}")
    private String BUCKET_NAME;


    private final StorageService storageService;

    @Autowired
    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

//    @GetMapping("/presigned-url")
//    public ResponseEntity<PresignedUrlResponse> getPresignedUrl(@RequestParam String fileName) {
//
//        String bucketName = BUCKET_NAME;
//        String presignedUrl = storageService.generatePresignedUrl(bucketName, fileName);
//
//        PresignedUrlResponse response = new PresignedUrlResponse(presignedUrl);
//
//        return ResponseEntity.ok(response);
//    }
}
