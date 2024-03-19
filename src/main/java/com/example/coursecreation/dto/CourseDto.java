package com.example.coursecreation.dto;


import com.example.coursecreation.Enums.CourseLevel;
import com.example.coursecreation.Enums.CourseStatus;
import com.example.coursecreation.Enums.Language;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CourseDto {

    private String image;

    private String title;

    private String about;

    private String requirements;

    private Language language;

    private Enum<Language> languageEnum;

    private Enum<CourseLevel> courseLevelEnum;

    private LocalDate date;

    private Long skillId;

    private Long categoryId;

}
