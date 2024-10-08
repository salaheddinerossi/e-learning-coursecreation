package com.example.coursecreation.controller;


import com.example.coursecreation.dto.LessonDto;
import com.example.coursecreation.dto.SummaryRequest;
import com.example.coursecreation.dto.UserDetailsDto;
import com.example.coursecreation.exception.ResourceNotFoundException;
import com.example.coursecreation.exception.UnauthorizedException;
import com.example.coursecreation.response.JsonResponse.SummaryResponse;
import com.example.coursecreation.response.TeacherLessonDetails;
import com.example.coursecreation.response.lessonResponse.LessonCreatedResponse;
import com.example.coursecreation.response.lessonResponse.LessonDetails;
import com.example.coursecreation.service.AuthService;
import com.example.coursecreation.service.LessonService;
import com.example.coursecreation.service.TeacherService;
import com.example.coursecreation.util.ApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/lesson")
public class LessonController {

    final
    LessonService lessonService;

    final
    AuthService authService;

    final
    TeacherService teacherService;

    @Value("${auth.url}")
    private String authUrl;


    public LessonController(LessonService lessonService, AuthService authService, TeacherService teacherService) {
        this.lessonService = lessonService;
        this.authService = authService;
        this.teacherService = teacherService;
    }

    @PostMapping("/")
    public ResponseEntity<ApiResponse<LessonCreatedResponse>> createLesson(@ModelAttribute LessonDto lessonDto , @RequestHeader("Authorization") String token) throws IOException {

        UserDetailsDto userDetailsDto = authService.getUserDetailsFromAuthService(authUrl,token);
        if (!teacherService.teacherHasChapter(lessonDto.getChapterId(), userDetailsDto.getEmail())) {
            throw new ResourceNotFoundException("you don't have the permission to create a course");
        }

        LessonCreatedResponse lessonCreatedResponse = lessonService.createLesson(lessonDto);
        return ResponseEntity.ok(new ApiResponse<>(true, "lesson has been created", lessonCreatedResponse));

    }

    @PostMapping("/summary/{id}")
    public ResponseEntity<ApiResponse<SummaryResponse>> addSummary(@PathVariable Long id, @RequestBody SummaryRequest summaryRequest, @RequestHeader("Authorization") String token) {
        String email = authService.getUserDetailsFromAuthService(authUrl, token).getEmail();
        if (!teacherService.teacherHasLesson(id, email)) {
            throw new ResourceNotFoundException("you don't have the permission to add a summary to this lesson");
        }

        SummaryResponse summaryResponse = lessonService.addSummary(summaryRequest, id);
        return ResponseEntity.ok(new ApiResponse<>(true, "summary has been added", summaryResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LessonDetails>> getLessonDetails(@PathVariable Long id,@RequestHeader("Authorization")String token) {

        UserDetailsDto userDetailsDto = authService.getUserDetailsFromAuthService(authUrl,token);
        if (authService.isStudent(userDetailsDto.getRole())){
            if (!teacherService.studentHasLesson(id, userDetailsDto.getEmail())){
                throw  new UnauthorizedException("you don't have the permission to access this lesson");
            }
        }

        if(authService.isTeacher(userDetailsDto.getRole())){
            if (!teacherService.teacherHasLesson(id, userDetailsDto.getEmail())){
                throw  new UnauthorizedException("you don't have the permission to access this lesson");
            }
        }

        LessonDetails lessonDetails = lessonService.getLessonDetails(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "lesson had been fetched successfully", lessonDetails));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<LessonCreatedResponse>> modifyLesson(@PathVariable Long id, @RequestBody LessonDto lessonDto, @RequestHeader("Authorization") String token) {
        String email = authService.getUserDetailsFromAuthService(authUrl, token).getEmail();
        if (!teacherService.teacherHasLesson(id, email)) {
            throw new ResourceNotFoundException("you don't have the permission to modify this lesson");
        }

        LessonCreatedResponse lessonCreatedResponse = lessonService.modifyResponse(id, lessonDto);
        return ResponseEntity.ok(new ApiResponse<>(true, "lesson has been modified ", lessonCreatedResponse));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<ApiResponse<?>> deleteLesson(@PathVariable Long id ,@RequestHeader("Authorization") String token){
        String email = authService.getUserDetailsFromAuthService(authUrl, token).getEmail();
        if (!teacherService.teacherHasLesson(id, email)) {
            throw new ResourceNotFoundException("you don't have the permission to delete this lesson");
        }

        lessonService.deleteLesson(id);
        return ResponseEntity.ok(new ApiResponse<>(true,"lesson has been deleted",null));
    }


    @GetMapping("teacher/{id}")
    public ResponseEntity<ApiResponse<TeacherLessonDetails>> getLessonDetailsForTeacher(@PathVariable Long id, @RequestHeader("Authorization")String token) {

        UserDetailsDto userDetailsDto = authService.getUserDetailsFromAuthService(authUrl,token);

        if (!teacherService.teacherHasLesson(id, userDetailsDto.getEmail())){
            throw  new UnauthorizedException("you don't have the permission to access this lesson");
        }

        TeacherLessonDetails teacherLessonDetails = lessonService.getTeacherLessonDetails(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "lesson had been fetched successfully", teacherLessonDetails));
    }
}
