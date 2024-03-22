package com.example.coursecreation.service;

import com.example.coursecreation.dto.LessonDto;
import com.example.coursecreation.dto.SummaryRequest;
import com.example.coursecreation.response.JsonResponse.SummaryResponse;
import com.example.coursecreation.response.lessonResponse.LessonCreatedResponse;

public interface LessonService {

    // create lesson
    LessonCreatedResponse createLesson(LessonDto lessonDto);

    SummaryResponse addSummary(SummaryRequest summaryRequest, Long lessonId);

    //modify lesson


    //get lesson Details

    //get lessons





}
