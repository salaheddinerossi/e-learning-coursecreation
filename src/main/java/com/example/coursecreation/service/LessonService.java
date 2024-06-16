package com.example.coursecreation.service;

import com.example.coursecreation.dto.LessonDto;
import com.example.coursecreation.dto.SummaryRequest;
import com.example.coursecreation.response.JsonResponse.SummaryResponse;
import com.example.coursecreation.response.TeacherLessonDetails;
import com.example.coursecreation.response.lessonResponse.LessonCreatedResponse;
import com.example.coursecreation.response.lessonResponse.LessonDetails;
import com.example.coursecreation.response.quizResponses.QuizWithAnswer;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface LessonService {

    LessonCreatedResponse createLesson( LessonDto lessonDto) throws IOException;

    SummaryResponse addSummary(SummaryRequest summaryRequest, Long lessonId);

    LessonCreatedResponse modifyResponse(Long id,LessonDto lessonDto);

    LessonDetails getLessonDetails(Long id);

    void deleteLesson(Long id);

    TeacherLessonDetails getTeacherLessonDetails(Long id);





}
