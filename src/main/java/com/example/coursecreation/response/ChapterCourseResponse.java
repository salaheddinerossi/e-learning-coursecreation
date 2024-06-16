package com.example.coursecreation.response;


import com.example.coursecreation.response.lessonResponse.LessonCourseResponse;
import lombok.Data;

import java.util.List;

@Data
public class ChapterCourseResponse {

    private Long id;

    private String title;

    private Boolean containsChapters;

    private List<ChapterCourseResponse> childChapters;


    private List<LessonCourseResponse> lessonCourseResponses;

}
