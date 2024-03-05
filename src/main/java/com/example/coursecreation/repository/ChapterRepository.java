package com.example.coursecreation.repository;

import com.example.coursecreation.model.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChapterRepository extends JpaRepository<Chapter,Long> {
}
