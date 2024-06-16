package com.example.coursecreation.mapper;

import com.example.coursecreation.dto.LessonDto;
import com.example.coursecreation.model.Lesson;
import com.example.coursecreation.response.TeacherLessonDetails;
import com.example.coursecreation.response.lessonResponse.LessonCreatedResponse;
import com.example.coursecreation.response.lessonResponse.LessonDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface LessonMapper {


    @Mapping(target = "material", ignore = true)
    Lesson lessonDtoToLesson(LessonDto lessonDto);

    @Mapping(source = "chapter.id", target = "chapterId")
    LessonCreatedResponse lessonToLessonCreatedResponse  (Lesson lesson);

    @Mapping(target = "quizNoAnswerResponses", ignore = true)
    LessonDetails lessonToLessonDetails(Lesson lesson);

    TeacherLessonDetails lessonToTeacherLessonDetails(Lesson lesson);

}