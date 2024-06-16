package com.example.coursecreation.response.quizResponses;


import lombok.Data;

import java.util.List;

@Data
public class QuestionAnswer {

    private Long id;

    private String question;

    private List<String> options;

    private Boolean booleanCorrectAnswer;

    private String stringCorrectAnswer;

    private String explanatoryAnswer;

}
