package com.example.coursecreation.dto;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class LessonDto {

    private String title;

    private String description;

    private MultipartFile material;

    private Boolean usesAI;

    private Long chapterId;

}
