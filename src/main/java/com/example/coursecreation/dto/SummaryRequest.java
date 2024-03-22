package com.example.coursecreation.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SummaryRequest {

    private String transcribtion;

    private String summary_type;

    private String additional_instructions;
}
