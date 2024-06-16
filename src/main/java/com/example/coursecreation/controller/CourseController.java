package com.example.coursecreation.controller;

import com.example.coursecreation.dto.CourseDto;
import com.example.coursecreation.dto.UserDetailsDto;
import com.example.coursecreation.exception.UnauthorizedException;
import com.example.coursecreation.response.CategoryCoursesResponse;
import com.example.coursecreation.response.CourseCreatedResponse;
import com.example.coursecreation.response.CourseDetailsResponse;
import com.example.coursecreation.response.CourseResponse;
import com.example.coursecreation.service.AuthService;
import com.example.coursecreation.service.CourseService;
import com.example.coursecreation.service.TeacherService;
import com.example.coursecreation.util.ApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/course")

public class CourseController {

    final
    CourseService courseService;

    @Value("${auth.url}")
    private String authUrl;

    final
    AuthService authService ;

    final
    TeacherService teacherService;


    public CourseController(CourseService courseService, AuthService authService, TeacherService teacherService) {
        this.courseService = courseService;
        this.authService = authService;
        this.teacherService = teacherService;
    }

    @PostMapping("/")
    public ResponseEntity<ApiResponse<CourseCreatedResponse>> createCourse(@ModelAttribute CourseDto courseDto , @RequestHeader("Authorization") String token) throws IOException {

        String email  = authService.getUserDetailsFromAuthService(authUrl,token).getEmail();

        CourseCreatedResponse courseCreatedResponse =  courseService.createCourse(courseDto, email);
        return ResponseEntity.ok(new ApiResponse<>(true, "Course has been created ",courseCreatedResponse));

    }

    @PutMapping("/{id}")
    ResponseEntity<ApiResponse<CourseCreatedResponse>> modifyCourse(@PathVariable Long id,@ModelAttribute CourseDto courseDto, @RequestHeader("Authorization") String token) throws IOException {
        String email = authService.getUserDetailsFromAuthService(authUrl,token).getEmail();
        if (!teacherService.teacherHasCourse(id,email)){
            throw new UnauthorizedException("you don't have the permission to modify this course");
        }

        CourseCreatedResponse courseCreatedResponse = courseService.modifyCourse(id,courseDto);
        return ResponseEntity.ok(new ApiResponse<>(Boolean.TRUE,"course has been modified",courseCreatedResponse));
    }

    @GetMapping("/{id}")
    ResponseEntity<ApiResponse<CourseDetailsResponse>> getCourseDetails(@PathVariable Long id){

        CourseDetailsResponse courseDetailsResponse = courseService.getCourseDetails(id,false);
        return ResponseEntity.ok(new ApiResponse<>(Boolean.TRUE,"Course fetched with success",courseDetailsResponse));
    }

    @GetMapping("/teacherCourse/{id}")
    ResponseEntity<ApiResponse<CourseDetailsResponse>> getCourseDetailsTeacher(@PathVariable Long id, @RequestHeader("Authorization") String token){

        Boolean teacherHasCourse = this.teacherService.teacherHasCourse(id, authService.getUserDetailsFromAuthService(authUrl, token).getEmail());
        if (!teacherHasCourse){
            throw new UnauthorizedException("you don't have the permission to access this course");
        }

        CourseDetailsResponse courseDetailsResponse = courseService.getCourseDetails(id,true);
        return ResponseEntity.ok(new ApiResponse<>(Boolean.TRUE,"Course fetched with success",courseDetailsResponse));
    }


    @PutMapping("/publish/{id}")
    ResponseEntity<ApiResponse<?>> publishCourse(@PathVariable Long id,@RequestHeader("Authorization") String token){
        String email = authService.getUserDetailsFromAuthService(authUrl,token).getEmail();
        if (!teacherService.teacherHasCourse(id,email)){
            throw new UnauthorizedException("you don't have the permission to modify this course");
        }

        courseService.publishCourse(id);
        return ResponseEntity.ok(new ApiResponse<>(Boolean.TRUE,"course has been published",null));
    }

    @GetMapping("/categoryCourses/{id}")
    ResponseEntity<ApiResponse<CategoryCoursesResponse>> getCoursesByCategory(@PathVariable Long id){
        CategoryCoursesResponse categoryCoursesResponse = courseService.getCoursesByCategory(id);
        return ResponseEntity.ok(new ApiResponse<>(Boolean.TRUE,"data has been fetched successfully",categoryCoursesResponse));
    }

    @GetMapping("/published")
    ResponseEntity<ApiResponse<List<CourseResponse>>> getPublishedCourses(@RequestHeader("Authorization")String token){

        UserDetailsDto userDetailsDto = authService.getUserDetailsFromAuthService(authUrl,token);

        if (!authService.isAdmin(userDetailsDto.getRole())){
            throw  new UnauthorizedException("you need to be admin to perform this action");
        }

        List<CourseResponse> courseResponses = courseService.getPublishedCourses();
        return ResponseEntity.ok(new ApiResponse<>(true,"course have been fetched successfully",courseResponses));
    }

    @GetMapping("/admin/{id}")
    ResponseEntity<ApiResponse<CourseDetailsResponse>> getCourseDetailResponseByAdmin(@PathVariable Long id,@RequestHeader("Authorization") String token){
        UserDetailsDto userDetailsDto = authService.getUserDetailsFromAuthService(authUrl,token);

        if (!authService.isAdmin(userDetailsDto.getRole())){
            throw  new UnauthorizedException("you need to be admin to perform this action");
        }

        return ResponseEntity.ok(new ApiResponse<>(true,"course has been fetched",courseService.getPublishedCourseDetails(id)));
    }

    // this route could be deleted check if it is not used later
    @GetMapping("/teacher/{courseId}")
    ResponseEntity<ApiResponse<CourseCreatedResponse>> getCourseFromTeacher(@PathVariable Long courseId,@RequestHeader("Authorization")String token){
        UserDetailsDto userDetailsDto = authService.getUserDetailsFromAuthService(authUrl,token);

        if (!teacherService.teacherHasCourse(courseId, userDetailsDto.getEmail())){
            throw new UnauthorizedException("you are not the owner of this course");
        }

        return ResponseEntity.ok(new ApiResponse<>(true,"course has been fetched",courseService.getCourseFromTeacher(courseId)));
    }

    @GetMapping("/chapter/{chapterId}")
    ResponseEntity<ApiResponse<Long>> getCourseIdByChapterId(@PathVariable Long chapterId,@RequestHeader("Authorization")String token){
        UserDetailsDto userDetailsDto = authService.getUserDetailsFromAuthService(authUrl,token);

        if (!teacherService.teacherHasChapter(chapterId, userDetailsDto.getEmail())){
            throw new UnauthorizedException("you are not the owner of this course");
        }

        return ResponseEntity.ok(new ApiResponse<>(true,"course has been fetched",courseService.getCourseIdFromChapterId(chapterId)));
    }

    @GetMapping("/teacher/courses")
    ResponseEntity<ApiResponse<List<CourseResponse>>> getTeacherCourses(@RequestHeader("Authorization") String token){
        UserDetailsDto userDetailsDto = authService.getUserDetailsFromAuthService(authUrl,token);

        if (!authService.isTeacher(userDetailsDto.getRole())){
            throw new UnauthorizedException("you are not allowed to perform this action");
        }

        return ResponseEntity.ok(new ApiResponse<>(true,"courses has been fetched",courseService.getTeacherCourses(userDetailsDto.getEmail())));

    }


}
