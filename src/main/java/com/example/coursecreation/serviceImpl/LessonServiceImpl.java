package com.example.coursecreation.serviceImpl;

import com.example.coursecreation.dto.LessonDto;
import com.example.coursecreation.exception.ResourceNotFoundException;
import com.example.coursecreation.model.Chapter;
import com.example.coursecreation.model.Lesson;
import com.example.coursecreation.repository.ChapterRepository;
import com.example.coursecreation.repository.CourseRepository;
import com.example.coursecreation.repository.LessonRepository;
import com.example.coursecreation.service.LessonService;
import org.springframework.stereotype.Service;


@Service
public class LessonServiceImpl implements LessonService {

    final
    LessonRepository lessonRepository;

    final
    ChapterRepository chapterRepository;

    final
    CourseRepository courseRepository;

    public LessonServiceImpl(LessonRepository lessonRepository, ChapterRepository chapterRepository, CourseRepository courseRepository) {
        this.lessonRepository = lessonRepository;
        this.chapterRepository = chapterRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public void createLesson(LessonDto lessonDto) {

        Lesson lesson = new Lesson();
        lesson.setTitle(lessonDto.getTitle());
        lesson.setMaterial(lessonDto.getMaterial());
        lesson.setDescription(lessonDto.getDescription());
        lesson.setUsesAI(lessonDto.getUsesAI());
        lesson.setChapter(findChapterById(lessonDto.getChapterId()));

        if (Boolean.TRUE.equals(lessonDto.getUsesAI())){

        }

    }

    private Chapter findChapterById(Long id){
        return chapterRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("chapter not found with the id:"+ id)
        );
    }




}
