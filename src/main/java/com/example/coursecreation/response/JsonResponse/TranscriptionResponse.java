package com.example.coursecreation.response.JsonResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TranscriptionResponse {
    @JsonProperty("advice")
    private List<String> advice;

    @JsonProperty("summary")
    private String summary;

    @JsonProperty("transcription")
    private String transcription;
}
