package com.example.coursecreation.response;


import lombok.Data;

import java.util.List;

@Data
public class ChapterCourseResponse {

    private Long id;

    private Boolean containsChapters;

    private List<ChapterCourseResponse> childChapters;


    private List<LessonCourseResponse> lessonCourseResponses;

}
