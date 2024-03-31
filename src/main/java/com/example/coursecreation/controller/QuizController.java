package com.example.coursecreation.controller;

import com.example.coursecreation.dto.QuizInstructions;
import com.example.coursecreation.dto.QuizRequest;
import com.example.coursecreation.exception.ResourceNotFoundException;
import com.example.coursecreation.response.JsonResponse.ExplanatoryResponse;
import com.example.coursecreation.response.JsonResponse.MultipleChoiceResponse;
import com.example.coursecreation.response.JsonResponse.TrueFalseResponse;
import com.example.coursecreation.service.AuthService;
import com.example.coursecreation.service.QuizService;
import com.example.coursecreation.service.TeacherService;
import com.example.coursecreation.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quiz")
public class QuizController {

    @Value("${auth.url}")
    private String authUrl;

    final
    AuthService authService;

    final
    TeacherService teacherService;

    final
    QuizService quizService;

    public QuizController(QuizService quizService, AuthService authService, TeacherService teacherService) {
        this.quizService = quizService;
        this.authService = authService;
        this.teacherService = teacherService;
    }

    @PostMapping("/multipleChoice/{id}")
    ResponseEntity<ApiResponse<List<MultipleChoiceResponse>>> generateMultipleQuiz(@PathVariable Long id,@RequestHeader("Authorization") String token){
        String email = authService.getUserDetailsFromAuthService(authUrl,token).getEmail();
        if (!teacherService.teacherHasLesson(id,email)){
            throw  new ResourceNotFoundException("you don't have the permission to add this quizz");
        }

        List<MultipleChoiceResponse> multipleChoiceResponses = quizService.generateMultipleChoiceQuiz(id);
        return ResponseEntity.ok(new ApiResponse<>(Boolean.TRUE,"quiz has been created",multipleChoiceResponses));

    }

    @PostMapping("/TrueFalse/{id}")
    ResponseEntity<ApiResponse<List<TrueFalseResponse>>> generateMultipleResponseQuiz(@PathVariable Long id,@RequestHeader("Authorization") String token){
        String email = authService.getUserDetailsFromAuthService(authUrl,token).getEmail();
        if (!teacherService.teacherHasLesson(id,email)){
            throw  new ResourceNotFoundException("you don't have the permission to add this quizz");
        }

        List<TrueFalseResponse> trueFalseResponses = quizService.generateTrueFalseQuiz(id);
        return ResponseEntity.ok(new ApiResponse<>(Boolean.TRUE,"quiz has been created",trueFalseResponses));

    }

    @PostMapping("/explanatory/{id}")
    ResponseEntity<ApiResponse<List<ExplanatoryResponse>>> generateExplanatoryQuiz(@PathVariable Long id,@RequestHeader("Authorization") String token){
        String email = authService.getUserDetailsFromAuthService(authUrl,token).getEmail();
        if (!teacherService.teacherHasLesson(id,email)){
            throw  new ResourceNotFoundException("you don't have the permission to add this quizz");
        }

        List<ExplanatoryResponse> explanatoryResponses = quizService.generateExplanatoryQuiz(id);
        return ResponseEntity.ok(new ApiResponse<>(Boolean.TRUE,"quiz has been created",explanatoryResponses));

    }

    @PutMapping("/TrueFalse/modify/{id}")
    ResponseEntity<ApiResponse<List<TrueFalseResponse>>> modifyTrueFalseQuiz(@PathVariable Long id,@RequestBody QuizInstructions quizInstructions,@RequestHeader("Authorization") String token){
        String email = authService.getUserDetailsFromAuthService(authUrl,token).getEmail();
        if (!teacherService.teacherHasQuiz(id,email)){
            throw  new ResourceNotFoundException("you don't have the permission to modify this quizz");
        }

        List<TrueFalseResponse> trueFalseResponses = quizService.modifyTrueFalseQuiz(id,quizInstructions);
        return ResponseEntity.ok(new ApiResponse<>(Boolean.TRUE,"quiz has been updated",trueFalseResponses));

    }

    @PutMapping("/explanatory/modify/{id}")
    ResponseEntity<ApiResponse<List<ExplanatoryResponse>>> modifyexplanatoryQuiz(@PathVariable Long id,@RequestBody QuizInstructions quizInstructions,@RequestHeader("Authorization") String token){
        String email = authService.getUserDetailsFromAuthService(authUrl,token).getEmail();
        if (!teacherService.teacherHasQuiz(id,email)){
            throw  new ResourceNotFoundException("you don't have the permission to modify this quizz");
        }

        List<ExplanatoryResponse> explanatoryResponses = quizService.modifyExplanatoryQuiz(id,quizInstructions);
        return ResponseEntity.ok(new ApiResponse<>(Boolean.TRUE,"quiz has been updated",explanatoryResponses));

    }


    @PutMapping("/multipleChoice/modify/{id}")
    ResponseEntity<ApiResponse<List<MultipleChoiceResponse>>> modifyMultipleQuiz(@PathVariable Long id,@RequestBody QuizInstructions quizInstructions,@RequestHeader("Authorization") String token){
        String email = authService.getUserDetailsFromAuthService(authUrl,token).getEmail();
        if (!teacherService.teacherHasQuiz(id,email)){
            throw  new ResourceNotFoundException("you don't have the permission to modify this quizz");
        }

        List<MultipleChoiceResponse> multipleChoiceResponses = quizService.modifyMultipleChoiceQuiz(id,quizInstructions);
        return ResponseEntity.ok(new ApiResponse<>(Boolean.TRUE,"quiz has been updated",multipleChoiceResponses));

    }

    @DeleteMapping("/{id}")
    ResponseEntity<ApiResponse<?>> deleteQuiz(@PathVariable Long id,@RequestHeader("Authorization") String token){

        String email = authService.getUserDetailsFromAuthService(authUrl,token).getEmail();
        if (!teacherService.teacherHasQuiz(id,email)){
            throw  new ResourceNotFoundException("you don't have the permission to modify this quizz");
        }

        quizService.deleteQuiz(id);
        return ResponseEntity.ok(new ApiResponse<>(Boolean.TRUE,"quiz has been deleted",null));
    }




}
