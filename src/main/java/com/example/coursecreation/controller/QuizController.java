package com.example.coursecreation.controller;

import com.example.coursecreation.dto.QuizInstructions;
import com.example.coursecreation.dto.QuizRequest;
import com.example.coursecreation.response.JsonResponse.ExplanatoryResponse;
import com.example.coursecreation.response.JsonResponse.MultipleChoiceResponse;
import com.example.coursecreation.response.JsonResponse.TrueFalseResponse;
import com.example.coursecreation.service.QuizService;
import com.example.coursecreation.util.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quiz")
public class QuizController {

    final
    QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping("/multipleChoice/{id}")
    ResponseEntity<ApiResponse<List<MultipleChoiceResponse>>> generateMultipleQuiz(@PathVariable Long id){

        List<MultipleChoiceResponse> multipleChoiceResponses = quizService.generateMultipleChoiceQuiz(id);
        return ResponseEntity.ok(new ApiResponse<>(Boolean.TRUE,"quiz has been created",multipleChoiceResponses));

    }

    @PostMapping("/TrueFalse/{id}")
    ResponseEntity<ApiResponse<List<TrueFalseResponse>>> generateMultipleResponseQuiz(@PathVariable Long id){

        List<TrueFalseResponse> trueFalseResponses = quizService.generateTrueFalseQuiz(id);
        return ResponseEntity.ok(new ApiResponse<>(Boolean.TRUE,"quiz has been created",trueFalseResponses));

    }

    @PostMapping("/explanatory/{id}")
    ResponseEntity<ApiResponse<List<ExplanatoryResponse>>> generateExplanatoryQuiz(@PathVariable Long id){

        List<ExplanatoryResponse> explanatoryResponses = quizService.generateExplanatoryQuiz(id);
        return ResponseEntity.ok(new ApiResponse<>(Boolean.TRUE,"quiz has been created",explanatoryResponses));

    }

    @PostMapping("/TrueFalse/modify/{id}")
    ResponseEntity<ApiResponse<List<TrueFalseResponse>>> modifyTrueFalseQuiz(@PathVariable Long id,@RequestBody QuizInstructions quizInstructions){

        List<TrueFalseResponse> trueFalseResponses = quizService.modifyTrueFalseQuiz(id,quizInstructions);
        return ResponseEntity.ok(new ApiResponse<>(Boolean.TRUE,"quiz has been updated",trueFalseResponses));

    }

    @PostMapping("/explanatory/modify/{id}")
    ResponseEntity<ApiResponse<List<ExplanatoryResponse>>> modifyexplanatoryQuiz(@PathVariable Long id,@RequestBody QuizInstructions quizInstructions){

        List<ExplanatoryResponse> explanatoryResponses = quizService.modifyExplanatoryQuiz(id,quizInstructions);
        return ResponseEntity.ok(new ApiResponse<>(Boolean.TRUE,"quiz has been updated",explanatoryResponses));

    }


    @PostMapping("/multipleChoice/modify/{id}")
    ResponseEntity<ApiResponse<List<MultipleChoiceResponse>>> modifyMultipleQuiz(@PathVariable Long id,@RequestBody QuizInstructions quizInstructions){

        List<MultipleChoiceResponse> multipleChoiceResponses = quizService.modifyMultipleChoiceQuiz(id,quizInstructions);
        return ResponseEntity.ok(new ApiResponse<>(Boolean.TRUE,"quiz has been updated",multipleChoiceResponses));

    }




}
