package com.example.coursecreation.repository;

import com.example.coursecreation.model.Quizzes.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz,Long> {
}
