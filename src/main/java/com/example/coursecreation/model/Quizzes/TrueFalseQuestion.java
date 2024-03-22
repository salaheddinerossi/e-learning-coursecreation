package com.example.coursecreation.model.Quizzes;


import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class TrueFalseQuestion extends Question{
    private boolean correctAnswer;

}
