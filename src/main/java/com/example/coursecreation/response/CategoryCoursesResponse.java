package com.example.coursecreation.response;

import lombok.Data;

import java.util.List;


@Data
public class CategoryCoursesResponse {

    private Long id;

    private String title;

    private String description;

    private List<CourseResponse> courseResponses;
}
