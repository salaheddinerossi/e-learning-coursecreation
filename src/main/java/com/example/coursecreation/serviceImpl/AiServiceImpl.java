package com.example.coursecreation.serviceImpl;


import com.example.coursecreation.dto.VideoRequest;
import com.example.coursecreation.response.JsonResponse.DetailedTranscriptionResponse;
import com.example.coursecreation.response.JsonResponse.TranscriptionResponse;
import com.example.coursecreation.service.AiService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

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



}
