package com.example.coursecreation.repository;

import com.example.coursecreation.model.StudentLesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentLessonRepository extends JpaRepository<StudentLesson,Long> {
    Optional<StudentLesson> findByLessonIdAndCourseEnrollmentStudentEmail(Long lessonId, String email);
}
