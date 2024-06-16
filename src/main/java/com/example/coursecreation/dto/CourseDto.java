package com.example.coursecreation.dto;


import com.example.coursecreation.Enums.CourseLevel;
import com.example.coursecreation.Enums.Language;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


@Data
public class CourseDto {

    private  MultipartFile image;

    private String title;

    private String about;

    private String requirements;

    private Language languageEnum;

    private CourseLevel courseLevelEnum;

    private Long skillId;

    private Long categoryId;

}
