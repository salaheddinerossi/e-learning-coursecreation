package com.example.coursecreation.mapper;

import com.example.coursecreation.dto.LessonDto;
import com.example.coursecreation.model.Lesson;
import com.example.coursecreation.response.lessonResponse.LessonCreatedResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LessonMapper {
    LessonMapper INSTANCE = Mappers.getMapper(LessonMapper.class);

    Lesson lessonDtoToLesson(LessonDto lessonDto);

    @Mapping(source = "chapter.id", target = "chapterId")
    LessonCreatedResponse lessonToLessonCreatedResponse  (Lesson lesson);
}