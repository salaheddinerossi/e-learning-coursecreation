package com.example.coursecreation.response.quizResponses;


import com.example.coursecreation.response.JsonResponse.TrueFalseResponse;
import lombok.Data;

import java.util.List;

@Data
public class TrueFalseQuiz {
    private Long id;

    private List<TrueFalseResponse> trueFalseResponses;
}
