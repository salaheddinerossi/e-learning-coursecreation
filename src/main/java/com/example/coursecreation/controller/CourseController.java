package com.example.coursecreation.controller;

import com.example.coursecreation.dto.CourseCreationRequest;
import com.example.coursecreation.dto.CourseDto;
import com.example.coursecreation.response.CourseCreatedResponse;
import com.example.coursecreation.service.CourseService;
import com.example.coursecreation.util.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/course")

public class CourseController {

    final
    CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping("/")
    public ResponseEntity<ApiResponse<CourseCreatedResponse>> createCourse(@RequestBody CourseCreationRequest courseCreationRequest){

        CourseCreatedResponse courseCreatedResponse =  courseService.createCourse(courseCreationRequest.getCourseDto(), courseCreationRequest.getEmail());
        return ResponseEntity.ok(new ApiResponse<>(true, "Course has been created ",courseCreatedResponse));

    }



}
