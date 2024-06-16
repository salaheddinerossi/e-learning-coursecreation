package com.example.coursecreation.response.quizResponses;


import com.example.coursecreation.response.JsonResponse.MultipleChoiceResponse;
import lombok.Data;

import java.util.List;

@Data
public class MultipleChoiceQuiz {

    private Long id;

    private List<MultipleChoiceResponse> multipleChoiceResponses;
}
