package com.example.coursecreation.model.Quizzes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class ExplanatoryQuestion extends Question {
    @Lob
    @Column(name="correctExplanation", length=100000)
    private String correctExplanation;

}