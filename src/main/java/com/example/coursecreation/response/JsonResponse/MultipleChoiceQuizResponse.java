package com.example.coursecreation.response.JsonResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MultipleChoiceQuizResponse {
    List<MultipleChoiceResponse> questions;
}
