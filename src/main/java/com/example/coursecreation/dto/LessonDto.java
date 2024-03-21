package com.example.coursecreation.dto;


import lombok.Data;

@Data
public class LessonDto {

    private String title;

    private String description;

    private String material;

    private Boolean usesAI;

    private Long chapterId;


}
