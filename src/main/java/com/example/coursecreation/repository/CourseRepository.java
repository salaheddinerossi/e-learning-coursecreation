package com.example.coursecreation.repository;

import com.example.coursecreation.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course,Long> {
}
