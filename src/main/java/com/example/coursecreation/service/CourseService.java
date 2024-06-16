package com.example.coursecreation.service;

import com.example.coursecreation.dto.CourseDto;
import com.example.coursecreation.response.CategoryCoursesResponse;
import com.example.coursecreation.response.CourseCreatedResponse;
import com.example.coursecreation.response.CourseDetailsResponse;
import com.example.coursecreation.response.CourseResponse;

import java.io.IOException;
import java.util.List;

public interface CourseService {
    CourseCreatedResponse createCourse(CourseDto courseDto, String email) throws IOException;

    CourseCreatedResponse modifyCourse(Long id,CourseDto courseDto) throws IOException;

    CourseDetailsResponse getCourseDetails(Long id,Boolean isTeacher);

    void publishCourse(Long id);

    CategoryCoursesResponse getCoursesByCategory(Long categoryId);

    List<CourseResponse> getPublishedCourses();

    List<CourseResponse> getTeacherCourses(String email);

    CourseDetailsResponse getPublishedCourseDetails(Long id);

    CourseCreatedResponse getCourseFromTeacher(Long courseId);


    Long getCourseIdFromChapterId(Long chapterId);
}
