package com.example.coursecreation.controller;


import com.example.coursecreation.dto.LessonDto;
import com.example.coursecreation.response.lessonResponse.LessonCreatedResponse;
import com.example.coursecreation.service.LessonService;
import com.example.coursecreation.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lesson")
public class LessonController {

    final
    LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @PostMapping("/")
    public ResponseEntity<ApiResponse<LessonCreatedResponse>> createLesson(@RequestBody LessonDto lessonDto){

        LessonCreatedResponse lessonCreatedResponse = lessonService.createLesson(lessonDto);
        return ResponseEntity.ok(new ApiResponse<>(true,"lesson has been created",lessonCreatedResponse));

    }


}
