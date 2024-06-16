package com.example.coursecreation.response.JsonResponse;


import lombok.Data;

@Data
public class ExplanatoryResponse {

    private Long id;

    private String prompt;

    private String correctExplanation;
}
