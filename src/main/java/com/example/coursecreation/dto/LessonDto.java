package com.example.coursecreation.dto;


import lombok.Data;

@Data
public class LessonDto {

    private Long id;

    private String title;

    private String description;

    private String material;

    private Boolean usesAI;

    private Long chapterId;

    private Long courseId;

}
