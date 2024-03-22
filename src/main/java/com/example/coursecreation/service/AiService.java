package com.example.coursecreation.service;

import com.example.coursecreation.dto.SummaryRequest;
import com.example.coursecreation.response.JsonResponse.*;
import reactor.core.publisher.Mono;


public interface AiService {

    Mono<DetailedTranscriptionResponse> getTranscribe(String videoUrl);

    Mono<MultipleChoiceQuizResponse> generateMultipleChoiceQuiz(String transcription,String additional_instructions);

    Mono<TrueFalseQuizResponse> generateTrueFalseQuiz(String transcription,String additional_instructions);

    Mono<ExplanatoryQuizResponse> generateExplanatoryQuiz(String transcription,String additional_instructions);

    Mono<SummaryResponse> generateSummary(SummaryRequest summaryRequest);


}
