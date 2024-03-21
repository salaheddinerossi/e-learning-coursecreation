package com.example.coursecreation.model.Quizzes;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String prompt;

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

}
