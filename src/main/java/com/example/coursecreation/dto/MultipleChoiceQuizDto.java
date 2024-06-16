package com.example.coursecreation.dto;


import com.example.coursecreation.response.JsonResponse.MultipleChoiceResponse;
import lombok.Data;

import java.util.List;

@Data
public class MultipleChoiceQuizDto {

    private List<MultipleChoiceResponse> questions;

}
