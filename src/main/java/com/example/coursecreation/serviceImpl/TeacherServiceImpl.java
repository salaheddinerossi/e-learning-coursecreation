package com.example.coursecreation.serviceImpl;

import com.example.coursecreation.exception.ResourceNotFoundException;
import com.example.coursecreation.model.Chapter;
import com.example.coursecreation.model.Course;
import com.example.coursecreation.model.Lesson;
import com.example.coursecreation.model.Quizzes.Quiz;
import com.example.coursecreation.repository.*;
import com.example.coursecreation.service.TeacherService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
public class TeacherServiceImpl implements TeacherService {

    final
    TeacherRepository teacherRepository;

    final
    CourseRepository courseRepository;

    final
    LessonRepository lessonRepository;

    final
    ChapterRepository chapterRepository;

    final
    QuizRepository quizRepository;

    final
    StudentLessonRepository studentLessonRepository;

    public TeacherServiceImpl(TeacherRepository teacherRepository, CourseRepository courseRepository, LessonRepository lessonRepository, ChapterRepository chapterRepository, QuizRepository quizRepository, StudentLessonRepository studentLessonRepository) {
        this.teacherRepository = teacherRepository;
        this.courseRepository = courseRepository;
        this.lessonRepository = lessonRepository;
        this.chapterRepository = chapterRepository;
        this.quizRepository = quizRepository;
        this.studentLessonRepository = studentLessonRepository;
    }


    @Override
    @Transactional
    public Boolean teacherHasCourse(Long courseID, String email) {
        Course course = findCourseById(courseID);

        return Objects.equals(course.getTeacher().getEmail(), email);
    }

    @Override
    public Boolean teacherHasLesson(Long lessonId, String email) {
        return Objects.equals(findLesson(lessonId).getChapter().getCourse().getTeacher().getEmail(), email);
    }

    @Override
    public Boolean teacherHasChapter(Long chapterId, String email) {
        return Objects.equals(findChapter(chapterId).getCourse().getTeacher().getEmail(), email);
    }

    @Override
    public Boolean teacherHasQuiz(Long quizId, String email) {

        return Objects.equals(findQuiz(quizId).getLesson().getChapter().getCourse().getTeacher().getEmail(), email);
    }

    @Override
    public Boolean studentHasLesson(Long lessonId, String email) {
        return studentLessonRepository.findByLessonIdAndCourseEnrollmentStudentEmail(lessonId,email).isPresent();
    }

    private Course findCourseById(Long id){
        return courseRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("course not found with this id" + id)
        );
    }

    private Lesson findLesson(Long id){
        return lessonRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Lesson not found with this exception")
        );
    }

    private Chapter findChapter(Long id){
        return chapterRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("chapter not found with this exception")
        );
    }

    private Quiz findQuiz(Long id){
        return quizRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("quiz not found with this exception")
        );
    }

}
