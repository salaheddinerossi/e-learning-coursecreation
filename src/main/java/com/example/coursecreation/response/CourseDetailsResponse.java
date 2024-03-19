package com.example.coursecreation.response;


import com.example.coursecreation.Enums.CourseLevel;
import com.example.coursecreation.Enums.CourseStatus;
import com.example.coursecreation.Enums.Language;
import lombok.Data;

import java.util.List;

@Data
public class CourseDetailsResponse {
    private Long id;

    private String image;

    private String title;

    private String about;

    private String requirements;

    private Enum<Language> languageEnum;

    private Enum<CourseLevel> courseLevelEnum;

    private Enum<CourseStatus> courseStatusEnum;

    private String SkillName;

    private List<ChapterCourseResponse> chapterCourseResponses;


}
