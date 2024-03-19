package com.example.coursecreation.mapper;

import com.example.coursecreation.dto.CourseDto;
import com.example.coursecreation.model.Course;
import com.example.coursecreation.model.Chapter;
import com.example.coursecreation.model.Lesson;
import com.example.coursecreation.response.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    CourseMapper INSTANCE = Mappers.getMapper(CourseMapper.class);

    @Mapping(target = "teacher", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "skill", ignore = true)
    @Mapping(target = "courseStatusEnum", ignore = true)
    @Mapping(target = "date", ignore = true)
    Course toCourse(CourseDto dto);


    CourseCreatedResponse toCourseCreatedResponse(Course course);


    CourseResponse toCourseResponse(Course course);

    @Mapping(target = "chapterCourseResponses", source = "chapters")
    CourseDetailsResponse toCourseDetailsResponse(Course course);

            // Chapter and lesson mappings
    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "containsChapters", source = "containsChapters"),
            @Mapping(target = "childChapters", source = "childChapters"),
            @Mapping(target = "lessonCourseResponses", source = "lessons")
    })
    ChapterCourseResponse toChapterCourseResponse(Chapter chapter);
    LessonCourseResponse toLessonCourseResponse(Lesson lesson);

    // Collection mappings for chapters and lessons
    List<ChapterCourseResponse> toChapterCourseResponseList(List<Chapter> chapters);
    List<LessonCourseResponse> toLessonCourseResponseList(List<Lesson> lessons);



    void updateCourseFromDto(CourseDto dto, @MappingTarget Course course);
}
