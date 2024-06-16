package com.example.coursecreation.dto;


import com.example.coursecreation.response.JsonResponse.ExplanatoryResponse;
import lombok.Data;

import java.util.List;

@Data
public class ExplanatoryQuizDto {

    private List<ExplanatoryResponse> questions;
}
