package com.example.coursecreation.response.quizResponses;

import lombok.Data;

import java.util.List;

@Data
public class QuestionResponse {

    private Long id;

    private String question;

    private List<String> options;


}
