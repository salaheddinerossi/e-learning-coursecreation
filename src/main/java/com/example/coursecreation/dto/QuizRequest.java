package com.example.coursecreation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QuizRequest {
    private String transcription;

    private String additional_instructions;
}
