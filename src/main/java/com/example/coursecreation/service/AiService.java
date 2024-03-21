package com.example.coursecreation.service;

import com.example.coursecreation.response.JsonResponse.DetailedTranscriptionResponse;
import reactor.core.publisher.Mono;

public interface AiService {

    Mono<DetailedTranscriptionResponse> getTranscribe(String videoUrl);

}
