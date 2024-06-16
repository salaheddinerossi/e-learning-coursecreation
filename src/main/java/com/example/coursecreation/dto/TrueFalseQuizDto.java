package com.example.coursecreation.dto;


import com.example.coursecreation.response.JsonResponse.TrueFalseResponse;
import lombok.Data;

import java.util.List;

@Data
public class TrueFalseQuizDto {

    private List<TrueFalseResponse> questions;

}
