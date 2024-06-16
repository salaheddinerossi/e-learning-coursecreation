package com.example.coursecreation.response.quizResponses;


import lombok.Data;

import java.util.List;

@Data
public class QuizWithAnswer {

    private Long id;

    private String type;

    private List<QuestionAnswer> questionAnswers;

}
