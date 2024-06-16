package com.example.coursecreation.response.lessonResponse;


import lombok.Data;

import java.util.List;

@Data
public class LessonCreatedResponse {

    private Long id;

    private String title;

    private String description;

    private String material;

    private Boolean usesAI;

    private Long chapterId;

    private List<String> advices;

    private String summary;


}
