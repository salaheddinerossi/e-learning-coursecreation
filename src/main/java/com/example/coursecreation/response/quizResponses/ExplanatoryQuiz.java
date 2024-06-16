package com.example.coursecreation.response.quizResponses;


import com.example.coursecreation.response.JsonResponse.ExplanatoryResponse;
import lombok.Data;

import java.util.List;

@Data
public class ExplanatoryQuiz {

    private Long id;

    private List<ExplanatoryResponse> ExplanatoryResponses;

}
