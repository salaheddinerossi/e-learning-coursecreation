package com.example.coursecreation.model;


import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class StudentLesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "course_enrollement_id")
    CourseEnrollment courseEnrollment;

    @ManyToOne
    @JoinColumn(name = "lesson_id")
    Lesson lesson;



}
