package com.example.coursecreation.serviceImpl;


import com.example.coursecreation.dto.QuizRequest;
import com.example.coursecreation.dto.SummaryRequest;
import com.example.coursecreation.dto.VideoRequest;
import com.example.coursecreation.response.JsonResponse.*;
import com.example.coursecreation.service.AiService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Service
public class AiServiceImpl implements AiService {

    @Value("${ai.service.url}")
    private String aiServiceUrl;

    WebClient webClient;

    public AiServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }
    @PostConstruct
    private void initialize() {
        this.webClient = this.webClient.mutate().baseUrl(aiServiceUrl).build();
    }

    @Override
    public Mono<DetailedTranscriptionResponse> getTranscribe(String videoUrl) {
        return webClient.post()
                .uri("/analyze-transcription/")
                .bodyValue(new VideoRequest(videoUrl))
                .retrieve()
                .bodyToMono(TranscriptionResponse.class)
                .map(response -> new DetailedTranscriptionResponse(
                        response.getAdvice(),
                        response.getSummary(),
                        response.getTranscription()))
                .onErrorResume(WebClientResponseException.class, ex ->
                        Mono.just(new DetailedTranscriptionResponse(null, "Error: " + ex.getResponseBodyAsString(), null)))
                .onErrorResume(ex ->
                        Mono.just(new DetailedTranscriptionResponse(null, "Unexpected error: " + ex.getMessage(), null)));
    }

    @Override
    public Mono<MultipleChoiceQuizResponse> generateMultipleChoiceQuiz(String transcription,String additional_instructions) {
        return webClient.post()
                .uri("/generate-quiz/")
                .bodyValue(new QuizRequest(transcription,additional_instructions))
                .retrieve()
                // Updated to expect a Mono of MultipleChoiceQuizResponse
                .bodyToMono(MultipleChoiceQuizResponse.class)
                .doOnNext(response -> System.out.println("Received response: " + response))
                .onErrorResume(WebClientResponseException.class, ex -> {
                    System.out.println("WebClient response error: " + ex.getResponseBodyAsString());
                    // Instead of returning an empty list, return an empty MultipleChoiceQuizResponse
                    return Mono.just(new MultipleChoiceQuizResponse(Collections.emptyList()));
                })
                .onErrorResume(ex -> {
                    System.out.println("Unexpected error: " + ex.getMessage());
                    // Return an empty MultipleChoiceQuizResponse for other errors as well
                    return Mono.just(new MultipleChoiceQuizResponse(Collections.emptyList()));
                });
    }

    @Override
    public Mono<TrueFalseQuizResponse> generateTrueFalseQuiz(String transcription,String additional_instructions) {
        return webClient.post()
                .uri("/generate-true-false-quiz/")
                .bodyValue(new QuizRequest(transcription,additional_instructions))
                .retrieve()
                // Expecting a Mono of TrueFalseQuizResponse
                .bodyToMono(TrueFalseQuizResponse.class)
                .doOnNext(response -> System.out.println("Received response: " + response))
                .onErrorResume(WebClientResponseException.class, ex -> {
                    System.out.println("WebClient response error: " + ex.getResponseBodyAsString());
                    // Return an empty TrueFalseQuizResponse
                    return Mono.just(new TrueFalseQuizResponse(Collections.emptyList()));
                })
                .onErrorResume(ex -> {
                    System.out.println("Unexpected error: " + ex.getMessage());
                    // Return an empty TrueFalseQuizResponse for other errors
                    return Mono.just(new TrueFalseQuizResponse(Collections.emptyList()));
                });
    }

    @Override
    public Mono<ExplanatoryQuizResponse> generateExplanatoryQuiz(String transcription,String additional_instructions) {
        return webClient.post()
                .uri("/generate-explanatory-questions/") // Make sure this URI is correct for explanatory quizzes
                .bodyValue(new QuizRequest(transcription,additional_instructions))
                .retrieve()
                // Expecting a Mono of ExplanatoryQuizResponse
                .bodyToMono(ExplanatoryQuizResponse.class)
                .doOnNext(response -> System.out.println("Received response: " + response))
                .onErrorResume(WebClientResponseException.class, ex -> {
                    System.out.println("WebClient response error: " + ex.getResponseBodyAsString());
                    // Return an empty ExplanatoryQuizResponse
                    return Mono.just(new ExplanatoryQuizResponse(Collections.emptyList()));
                })
                .onErrorResume(ex -> {
                    System.out.println("Unexpected error: " + ex.getMessage());
                    // Return an empty ExplanatoryQuizResponse for other errors
                    return Mono.just(new ExplanatoryQuizResponse(Collections.emptyList()));
                });
    }

    @Override
    public Mono<SummaryResponse> generateSummary(SummaryRequest summaryRequest) {
        return webClient.post()
                .uri("/generate-summary/") // Adjust this URI to match your summary generation endpoint
                .bodyValue(summaryRequest) // Use the provided summaryRequest as the body
                .retrieve()
                // Expecting a Mono of SummaryResponse
                .bodyToMono(SummaryResponse.class)
                .doOnNext(response -> System.out.println("Received summary response: " + response))
                .onErrorResume(WebClientResponseException.class, ex -> {
                    System.out.println("WebClient response error in generateSummary: " + ex.getResponseBodyAsString());
                    // Return an empty SummaryResponse or a meaningful default
                    return Mono.just(new SummaryResponse("Error generating summary, please try again later."));
                })
                .onErrorResume(ex -> {
                    System.out.println("Unexpected error in generateSummary: " + ex.getMessage());
                    // Return an empty SummaryResponse or a meaningful default for other errors
                    return Mono.just(new SummaryResponse("Unexpected error, please try again later."));
                });
    }


}
