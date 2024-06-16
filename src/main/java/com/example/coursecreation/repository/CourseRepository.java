package com.example.coursecreation.repository;

import com.example.coursecreation.model.Category;
import com.example.coursecreation.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course,Long> {
    List<Course> findCoursesByCategoryId(Long categoryId);

    List<Course> findCourseByCategory(Category category);


    List<Course> findByTeacherEmail(String email);
}
