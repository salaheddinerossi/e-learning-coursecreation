package com.example.coursecreation.service;

import com.example.coursecreation.dto.CourseDto;
import com.example.coursecreation.response.CourseCreatedResponse;
import com.example.coursecreation.response.CourseDetailsResponse;
import com.example.coursecreation.response.CourseResponse;

import java.util.List;

public interface CourseService {
    CourseCreatedResponse createCourse(CourseDto courseDto, String email);

    CourseCreatedResponse modifyCourse(Long id,CourseDto courseDto);

    CourseDetailsResponse getCourseDetails(Long id);

    void publishCourse(Long id);

    List<CourseResponse> getCoursesByCategory(Long categoryId);

    List<CourseResponse> getPublishedCourses();

    CourseDetailsResponse getPublishedCourseDetails(Long id);

    CourseCreatedResponse getCourseFromTeacher(Long courseId);


}
