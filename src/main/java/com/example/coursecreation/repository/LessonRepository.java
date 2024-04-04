package com.example.coursecreation.repository;

import com.example.coursecreation.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LessonRepository extends JpaRepository<Lesson,Long> {
}
