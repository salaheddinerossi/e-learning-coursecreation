package com.example.coursecreation.response;

import lombok.Data;

@Data
public class CourseResponse {

    private Long id;

    private String image;

    private String title;

    private String about;

    private String courseStatusEnum;

}
