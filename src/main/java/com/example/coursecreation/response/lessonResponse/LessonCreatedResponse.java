package com.example.coursecreation.response.lessonResponse;


import lombok.Data;

@Data
public class LessonCreatedResponse {

    private Long id;

    private String title;

    private String description;

    private String material;

    private Boolean usesAI;

    private Long chapterId;

}
