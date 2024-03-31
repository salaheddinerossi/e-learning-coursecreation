package com.example.coursecreation.response.quizResponses;


import lombok.Data;

import java.util.List;

@Data
public class QuizNoAnswerResponse {
    List<QuestionResponse> questionResponses;

    private String type;

}
