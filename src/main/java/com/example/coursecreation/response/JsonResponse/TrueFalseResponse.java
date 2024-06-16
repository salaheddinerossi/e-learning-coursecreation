package com.example.coursecreation.response.JsonResponse;


import lombok.Data;

@Data
public class TrueFalseResponse {


    private Long id;

    private String prompt;

    private Boolean correctAnswer;

}
