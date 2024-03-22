package com.example.coursecreation.model.Quizzes;

import com.example.coursecreation.util.StringListToJsonConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class MultipleChoiceQuestion extends Question {
    @Convert(converter = StringListToJsonConverter.class)
    private List<String> options;

    private String correctAnswer;

}
