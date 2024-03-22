package com.example.coursecreation.controller;


import com.example.coursecreation.dto.LessonDto;
import com.example.coursecreation.dto.SummaryRequest;
import com.example.coursecreation.response.JsonResponse.SummaryResponse;
import com.example.coursecreation.response.lessonResponse.LessonCreatedResponse;
import com.example.coursecreation.service.LessonService;
import com.example.coursecreation.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/summary/{id}")
    public ResponseEntity<ApiResponse<SummaryResponse>> addSummary(@PathVariable Long id, @RequestBody SummaryRequest summaryRequest){

        SummaryResponse summaryResponse = lessonService.addSummary(summaryRequest,id);
        return ResponseEntity.ok(new ApiResponse<>(true,"summary has been added",summaryResponse));

    }


}
