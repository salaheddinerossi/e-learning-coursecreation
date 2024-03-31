package com.example.coursecreation.service;

import com.example.coursecreation.model.Teacher;

public interface TeacherService {

    //find teacher by id

    Boolean teacherHasCourse(Long courseId,String email);

    Boolean teacherHasLesson(Long lessonId,String email);

    Boolean teacherHasChapter(Long chapterId,String email);
    Boolean teacherHasQuiz(Long quizId,String email);

}
