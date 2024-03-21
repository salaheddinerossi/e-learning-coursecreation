package com.example.coursecreation.response.JsonResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailedTranscriptionResponse {
    private List<String> advice;
    private String summary;
    private String transcription;

}
