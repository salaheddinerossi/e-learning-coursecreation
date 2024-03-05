package com.example.coursecreation.service;

import com.example.coursecreation.dto.CourseDto;
import com.example.coursecreation.response.CourseDetailsResponse;
import com.example.coursecreation.response.CourseResponse;

import java.util.List;

public interface CourseService {
    //createCourse
    void createCourse(CourseDto courseDto, String email);

    //modify Course
    void modifyCourse(Long id,CourseDto courseDto, String email);

    //getCourseDetails
    CourseDetailsResponse getCourseDetails(Long id);

    //PublishCourse
    void publishCourse(Long id);

    //getCoursesByCategory
    List<CourseResponse> getCoursesByCategory(Long categoryId);




}
