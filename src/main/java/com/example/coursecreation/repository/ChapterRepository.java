package com.example.coursecreation.repository;

import com.example.coursecreation.model.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChapterRepository extends JpaRepository<Chapter,Long> {
    List<Chapter> findByParentChapter(Chapter chapter);
}
