package com.example.coursecreation.controller;

import com.example.coursecreation.dto.*;
import com.example.coursecreation.exception.ResourceNotFoundException;
import com.example.coursecreation.response.JsonResponse.ExplanatoryResponse;
import com.example.coursecreation.response.JsonResponse.MultipleChoiceResponse;
import com.example.coursecreation.response.JsonResponse.TrueFalseResponse;
import com.example.coursecreation.response.quizResponses.ExplanatoryQuiz;
import com.example.coursecreation.response.quizResponses.MultipleChoiceQuiz;
import com.example.coursecreation.response.quizResponses.TrueFalseQuiz;
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
    ResponseEntity<ApiResponse<MultipleChoiceQuiz>> generateMultipleQuiz(@PathVariable Long id, @RequestHeader("Authorization") String token){
        String email = authService.getUserDetailsFromAuthService(authUrl,token).getEmail();
        if (!teacherService.teacherHasLesson(id,email)){
            throw  new ResourceNotFoundException("you don't have the permission to add this quizz");
        }

        MultipleChoiceQuiz multipleChoiceQuiz = quizService.generateMultipleChoiceQuiz(id);
        return ResponseEntity.ok(new ApiResponse<>(Boolean.TRUE,"quiz has been created",multipleChoiceQuiz));

    }

    @PostMapping("/TrueFalse/{id}")
    ResponseEntity<ApiResponse<TrueFalseQuiz>> generateMultipleResponseQuiz(@PathVariable Long id, @RequestHeader("Authorization") String token){
        String email = authService.getUserDetailsFromAuthService(authUrl,token).getEmail();
        if (!teacherService.teacherHasLesson(id,email)){
            throw  new ResourceNotFoundException("you don't have the permission to add this quizz");
        }

        TrueFalseQuiz trueFalseQuiz = quizService.generateTrueFalseQuiz(id);
        return ResponseEntity.ok(new ApiResponse<>(Boolean.TRUE,"quiz has been created",trueFalseQuiz));

    }

    @PostMapping("/explanatory/{id}")
    ResponseEntity<ApiResponse<ExplanatoryQuiz>> generateExplanatoryQuiz(@PathVariable Long id,@RequestHeader("Authorization") String token){
        String email = authService.getUserDetailsFromAuthService(authUrl,token).getEmail();
        if (!teacherService.teacherHasLesson(id,email)){
            throw  new ResourceNotFoundException("you don't have the permission to add this quizz");
        }

        ExplanatoryQuiz explanatoryQuiz = quizService.generateExplanatoryQuiz(id);
        return ResponseEntity.ok(new ApiResponse<>(Boolean.TRUE,"quiz has been created",explanatoryQuiz));

    }


    @PostMapping("/multipleChoice/manual/{id}")
    ResponseEntity<ApiResponse<MultipleChoiceQuiz>> createManualMultipleChoiceQuiz(@RequestBody MultipleChoiceQuizDto multipleChoiceQuizDto, @PathVariable Long id, @RequestHeader("Authorization") String token){
        String email = authService.getUserDetailsFromAuthService(authUrl,token).getEmail();
        if (!teacherService.teacherHasLesson(id,email)){
            throw  new ResourceNotFoundException("you don't have the permission to add this quizz");
        }

        MultipleChoiceQuiz multipleChoiceQuiz = quizService.createMultipleChoiceQuizManually(id,multipleChoiceQuizDto.getQuestions());
        return ResponseEntity.ok(new ApiResponse<>(Boolean.TRUE,"quiz has been created",multipleChoiceQuiz));

    }

    @PostMapping("/TrueFalse/manual/{id}")
    ResponseEntity<ApiResponse<TrueFalseQuiz>> createManualTrueFalseQuiz(@RequestBody TrueFalseQuizDto trueFalseQuizDto, @PathVariable Long id, @RequestHeader("Authorization") String token){
        String email = authService.getUserDetailsFromAuthService(authUrl,token).getEmail();
        if (!teacherService.teacherHasLesson(id,email)){
            throw  new ResourceNotFoundException("you don't have the permission to add this quizz");
        }

        TrueFalseQuiz trueFalseQuiz = quizService.createTrueFalseQuizManually(id,trueFalseQuizDto.getQuestions());
        return ResponseEntity.ok(new ApiResponse<>(Boolean.TRUE,"quiz has been created",trueFalseQuiz));

    }

    @PostMapping("/explanatory/manual/{id}")
    ResponseEntity<ApiResponse<ExplanatoryQuiz>> createManualExplanatoryQuiz(@RequestBody ExplanatoryQuizDto explanatoryQuizDto, @PathVariable Long id, @RequestHeader("Authorization") String token){
        String email = authService.getUserDetailsFromAuthService(authUrl,token).getEmail();
        if (!teacherService.teacherHasLesson(id,email)){
            throw  new ResourceNotFoundException("you don't have the permission to add this quizz");
        }

        ExplanatoryQuiz explanatoryQuiz = quizService.createExplanatoryQuizManually(id,explanatoryQuizDto.getQuestions());
        return ResponseEntity.ok(new ApiResponse<>(Boolean.TRUE,"quiz has been created",explanatoryQuiz));

    }

    @PutMapping("/TrueFalse/modify/{id}")
    ResponseEntity<ApiResponse<TrueFalseQuiz>> modifyTrueFalseQuiz(@PathVariable Long id,@RequestBody QuizInstructions quizInstructions,@RequestHeader("Authorization") String token){
        String email = authService.getUserDetailsFromAuthService(authUrl,token).getEmail();
        if (!teacherService.teacherHasQuiz(id,email)){
            throw  new ResourceNotFoundException("you don't have the permission to modify this quizz");
        }

        TrueFalseQuiz trueFalseQuiz = quizService.modifyTrueFalseQuiz(id,quizInstructions);
        return ResponseEntity.ok(new ApiResponse<>(Boolean.TRUE,"quiz has been updated",trueFalseQuiz));

    }

    @PutMapping("/explanatory/modify/{id}")
    ResponseEntity<ApiResponse<ExplanatoryQuiz>> modifyExplanatoryQuiz(@PathVariable Long id,@RequestBody QuizInstructions quizInstructions,@RequestHeader("Authorization") String token){
        String email = authService.getUserDetailsFromAuthService(authUrl,token).getEmail();
        if (!teacherService.teacherHasQuiz(id,email)){
            throw  new ResourceNotFoundException("you don't have the permission to modify this quizz");
        }

        ExplanatoryQuiz explanatoryQuiz = quizService.modifyExplanatoryQuiz(id,quizInstructions);
        return ResponseEntity.ok(new ApiResponse<>(Boolean.TRUE,"quiz has been updated",explanatoryQuiz));

    }


    @PutMapping("/multipleChoice/modify/{id}")
    ResponseEntity<ApiResponse<MultipleChoiceQuiz>> modifyMultipleQuiz(@PathVariable Long id,@RequestBody QuizInstructions quizInstructions,@RequestHeader("Authorization") String token){
        String email = authService.getUserDetailsFromAuthService(authUrl,token).getEmail();
        if (!teacherService.teacherHasQuiz(id,email)){
            throw  new ResourceNotFoundException("you don't have the permission to modify this quizz");
        }

        MultipleChoiceQuiz multipleChoiceQuiz = quizService.modifyMultipleChoiceQuiz(id,quizInstructions);
        return ResponseEntity.ok(new ApiResponse<>(Boolean.TRUE,"quiz has been updated",multipleChoiceQuiz));

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
