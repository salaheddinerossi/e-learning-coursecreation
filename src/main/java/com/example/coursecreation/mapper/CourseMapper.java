package com.example.coursecreation.mapper;

import com.example.coursecreation.dto.CourseDto;
import com.example.coursecreation.model.Course;
import com.example.coursecreation.model.Chapter;
import com.example.coursecreation.model.Lesson;
import com.example.coursecreation.response.*;
import com.example.coursecreation.response.lessonResponse.LessonCourseResponse;
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
    @Mapping(target = "image", ignore = true)
    Course toCourse(CourseDto dto);


    @Mapping(source = "course.category.id",target = "categoryId")
    @Mapping(source = "course.category.title",target = "skillName")
    CourseCreatedResponse toCourseCreatedResponse(Course course);


    CourseResponse toCourseResponse(Course course);

    List<CourseResponse> toCourseResponse(List<Course> courses);

    @Mapping(source = "course.category.id",target = "categoryId")
    @Mapping(target = "chapterCourseResponses", source = "chapters")
    @Mapping(target = "skillId",source = "course.skill.id")
    @Mapping(target = "skillName",source = "course.skill.name")
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
}
