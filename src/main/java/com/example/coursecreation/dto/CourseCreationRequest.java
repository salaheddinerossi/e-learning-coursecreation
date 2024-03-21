package com.example.coursecreation.dto;


import lombok.Data;

@Data
public class CourseCreationRequest {

    private CourseDto courseDto;
    private String email;

}
