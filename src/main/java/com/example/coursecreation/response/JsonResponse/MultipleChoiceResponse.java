package com.example.coursecreation.response.JsonResponse;

import lombok.Data;

import java.util.List;

@Data
public class MultipleChoiceResponse {

    private Long id;

    private String prompt;

    private List<String> options;

    private String correctAnswer;

}
