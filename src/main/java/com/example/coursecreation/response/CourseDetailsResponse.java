package com.example.coursecreation.response;


import com.example.coursecreation.Enums.CourseLevel;
import com.example.coursecreation.Enums.CourseStatus;
import com.example.coursecreation.Enums.Language;
import lombok.Data;

@Data
public class CourseDetailsResponse {
    private Long id;

    private String image;

    private String title;

    private String about;

    private String requirements;

    private Language language;

    private CourseLevel courseLevel;

    private CourseStatus courseStatus;



}
