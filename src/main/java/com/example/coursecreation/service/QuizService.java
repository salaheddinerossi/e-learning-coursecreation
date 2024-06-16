package com.example.coursecreation.service;

import com.example.coursecreation.dto.QuizInstructions;
import com.example.coursecreation.response.JsonResponse.ExplanatoryResponse;
import com.example.coursecreation.response.JsonResponse.MultipleChoiceQuizResponse;
import com.example.coursecreation.response.JsonResponse.MultipleChoiceResponse;
import com.example.coursecreation.response.JsonResponse.TrueFalseResponse;
import com.example.coursecreation.response.quizResponses.ExplanatoryQuiz;
import com.example.coursecreation.response.quizResponses.MultipleChoiceQuiz;
import com.example.coursecreation.response.quizResponses.TrueFalseQuiz;

import java.util.List;

public interface QuizService {

    MultipleChoiceQuiz generateMultipleChoiceQuiz(Long lessonID);

    ExplanatoryQuiz generateExplanatoryQuiz(Long lessonID);

    TrueFalseQuiz generateTrueFalseQuiz(Long lessonID);

    MultipleChoiceQuiz createMultipleChoiceQuizManually(Long lessonID,List<MultipleChoiceResponse> multipleChoiceResponses);

    ExplanatoryQuiz createExplanatoryQuizManually(Long lessonID,List<ExplanatoryResponse> explanatoryResponses);

    TrueFalseQuiz createTrueFalseQuizManually(Long lessonID,List<TrueFalseResponse> trueFalseResponses);


    MultipleChoiceQuiz modifyMultipleChoiceQuiz(Long quizId, QuizInstructions quizInstructions);

    ExplanatoryQuiz modifyExplanatoryQuiz(Long quizId,QuizInstructions quizInstructions);

    TrueFalseQuiz modifyTrueFalseQuiz(Long quizId,QuizInstructions quizInstructions);



    void deleteQuiz(Long id);

}
