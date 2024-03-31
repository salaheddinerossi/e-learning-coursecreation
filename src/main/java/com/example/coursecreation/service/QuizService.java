package com.example.coursecreation.service;

import com.example.coursecreation.dto.QuizInstructions;
import com.example.coursecreation.response.JsonResponse.ExplanatoryResponse;
import com.example.coursecreation.response.JsonResponse.MultipleChoiceQuizResponse;
import com.example.coursecreation.response.JsonResponse.MultipleChoiceResponse;
import com.example.coursecreation.response.JsonResponse.TrueFalseResponse;

import java.util.List;

public interface QuizService {

    List<MultipleChoiceResponse> generateMultipleChoiceQuiz(Long lessonID);

    List<ExplanatoryResponse> generateExplanatoryQuiz(Long lessonID);

    List<TrueFalseResponse> generateTrueFalseQuiz(Long lessonID);

    List<MultipleChoiceResponse> modifyMultipleChoiceQuiz(Long quizId, QuizInstructions quizInstructions);

    List<ExplanatoryResponse> modifyExplanatoryQuiz(Long quizId,QuizInstructions quizInstructions);

    List<TrueFalseResponse> modifyTrueFalseQuiz(Long quizId,QuizInstructions quizInstructions);

    void deleteQuiz(Long id);

}
