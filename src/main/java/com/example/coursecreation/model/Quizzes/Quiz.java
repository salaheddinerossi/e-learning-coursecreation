package com.example.coursecreation.model.Quizzes;

import com.example.coursecreation.model.Lesson;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;


@Data
@Entity
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions;

    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;
}
