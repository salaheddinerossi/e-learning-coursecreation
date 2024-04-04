package com.example.coursecreation.response.lessonResponse;

import com.example.coursecreation.response.quizResponses.QuizNoAnswerResponse;
import lombok.Data;

import java.util.List;

@Data
public class LessonDetails {

    private Long id;

    private String title;

    private String description;

    private String material;

    private Boolean usesAI;

    private String summary;

    private List<QuizNoAnswerResponse> quizNoAnswerResponses;

}
