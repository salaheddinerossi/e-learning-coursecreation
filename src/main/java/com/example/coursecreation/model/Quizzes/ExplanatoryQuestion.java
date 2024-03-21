package com.example.coursecreation.model.Quizzes;

import jakarta.persistence.Entity;
import jakarta.persistence.Lob;

@Entity
public class ExplanatoryQuestion extends Question {
    @Lob
    private String correctExplanation;

}