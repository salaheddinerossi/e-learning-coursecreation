package com.example.coursecreation.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "chapter")
public class Chapter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private Boolean containsChapters;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    private Chapter parentChapter;

    @OneToMany(mappedBy = "parentChapter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chapter> childChapters = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lesson> lessons = new ArrayList<>();

}
