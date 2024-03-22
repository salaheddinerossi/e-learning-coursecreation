package com.example.coursecreation.response.JsonResponse;


import lombok.Data;

@Data
public class TrueFalseResponse {

    private String prompt;

    private Boolean correctAnswer;

}
