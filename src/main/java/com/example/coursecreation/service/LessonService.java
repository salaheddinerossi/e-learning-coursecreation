package com.example.coursecreation.service;

import com.example.coursecreation.dto.LessonDto;
import com.example.coursecreation.dto.SummaryRequest;
import com.example.coursecreation.response.JsonResponse.SummaryResponse;
import com.example.coursecreation.response.lessonResponse.LessonCreatedResponse;
import com.example.coursecreation.response.lessonResponse.LessonDetails;

public interface LessonService {

    LessonCreatedResponse createLesson(LessonDto lessonDto);

    SummaryResponse addSummary(SummaryRequest summaryRequest, Long lessonId);

    LessonCreatedResponse modifyResponse(Long id,LessonDto lessonDto);

    LessonDetails getLessonDetails(Long id);

    void deleteLesson(Long id);


}
