package com.example.coursecreation.response;

import com.example.coursecreation.response.quizResponses.QuizWithAnswer;
import com.example.coursecreation.response.quizResponses.QuizzesResponse;
import lombok.Data;

import java.util.List;


@Data
public class TeacherLessonDetails {

    private Long id;

    private String title;

    private String description;

    private String material;

    private Boolean usesAI;

    private String summary;

    private List<String> advices;

    private QuizzesResponse quizzesResponse;


}
