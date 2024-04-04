package com.example.coursecreation.model;

import com.example.coursecreation.Enums.CourseLevel;
import com.example.coursecreation.Enums.CourseStatus;
import com.example.coursecreation.Enums.Language;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String image;

    private String title;

    private String about;

    private String requirements;

    private Language languageEnum;
    private CourseLevel courseLevelEnum;

    private CourseStatus courseStatusEnum;

    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chapter> chapters = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "skill_id")
    private Skill skill;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CourseEnrollment> courseEnrollments = new ArrayList<>();


}
