package com.example.coursecreation.model.Quizzes;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class MultipleChoiceQuestion extends Question {
    @ElementCollection
    private List<String> options;

    private String correctAnswer;

}
