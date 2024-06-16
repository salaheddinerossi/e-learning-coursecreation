package com.example.coursecreation.response.quizResponses;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class QuizzesResponse {

    List<MultipleChoiceQuiz> multipleChoiceQuizzes = new ArrayList<>();

    List<ExplanatoryQuiz> explanatoryQuizzes=new ArrayList<>();

    List<TrueFalseQuiz> trueFalseQuizzes=new ArrayList<>();

}
