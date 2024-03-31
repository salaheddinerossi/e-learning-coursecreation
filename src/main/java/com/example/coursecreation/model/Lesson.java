package com.example.coursecreation.model;


import com.example.coursecreation.model.Quizzes.Quiz;
import com.example.coursecreation.util.StringListToJsonConverter;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "lesson")
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;


    private String material;

    @Convert(converter = StringListToJsonConverter.class)
    private List<String> advices;

    private String summary;

    @Lob
    private String transcribe;

    private Boolean usesAI;

    @Column
    private Boolean isDeleted=false;


    @ManyToOne
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Quiz> quizzes = new ArrayList  <>();


}
