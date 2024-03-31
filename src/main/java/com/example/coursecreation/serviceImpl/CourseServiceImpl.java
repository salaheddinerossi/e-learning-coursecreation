package com.example.coursecreation.serviceImpl;

import com.example.coursecreation.Enums.CourseStatus;
import com.example.coursecreation.dto.CourseDto;
import com.example.coursecreation.exception.*;
import com.example.coursecreation.mapper.ChapterMapper;
import com.example.coursecreation.mapper.CourseMapper;
import com.example.coursecreation.model.*;
import com.example.coursecreation.repository.CategoryRepository;
import com.example.coursecreation.repository.CourseRepository;
import com.example.coursecreation.repository.SkillRepository;
import com.example.coursecreation.repository.TeacherRepository;
import com.example.coursecreation.response.CourseCreatedResponse;
import com.example.coursecreation.response.CourseDetailsResponse;
import com.example.coursecreation.response.CourseResponse;
import com.example.coursecreation.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class CourseServiceImpl implements CourseService {

    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private final SkillRepository skillRepository;
    private final CourseMapper courseMapper;

    final
    ChapterMapper chapterMapper;

    @Autowired
    public CourseServiceImpl(TeacherRepository teacherRepository, CourseRepository courseRepository,
                             CategoryRepository categoryRepository, SkillRepository skillRepository,
                             CourseMapper courseMapper, ChapterMapper chapterMapper) {
        this.teacherRepository = teacherRepository;
        this.courseRepository = courseRepository;
        this.categoryRepository = categoryRepository;
        this.skillRepository = skillRepository;
        this.courseMapper = courseMapper;
        this.chapterMapper = chapterMapper;
    }

    @Override
    public CourseCreatedResponse createCourse(CourseDto courseDto, String email) {
        Course course = courseMapper.toCourse(courseDto);
        course.setTeacher(findTeacherByEmail(email));
        course.setCategory(findCategoryById(courseDto.getCategoryId()));
        if(courseDto.getSkillId()!=null){
            course.setSkill(findSkillById(courseDto.getSkillId()));
        }
        course.setCourseStatusEnum(CourseStatus.DRAFT);
        course.setDate(LocalDate.now());

        return courseMapper.toCourseCreatedResponse(courseRepository.save(course));
    }

    @Override
    public CourseCreatedResponse modifyCourse(Long id, CourseDto courseDto, String email) {

        Course course = findCourseById(id);
        course.setAbout(courseDto.getAbout());
        course.setCourseLevelEnum(courseDto.getCourseLevelEnum());

        Category category = findCategoryById(courseDto.getCategoryId());

        categoryCanContainsCourse(category);
        course.setCategory(category);

        course.setTitle(courseDto.getTitle());
        course.setImage(courseDto.getImage());
        course.setRequirements(courseDto.getRequirements());

        return courseMapper.toCourseCreatedResponse(courseRepository.save(course));

    }

    @Override
    public CourseDetailsResponse getCourseDetails(Long id) {
        Course course = findCourseById(id);

        return courseMapper.toCourseDetailsResponse(course);
    }

    @Override
    public List<CourseResponse> getCoursesByCategory(Long categoryId) {
        List<Course> courses = courseRepository.findCoursesByCategoryId(categoryId);
        return courses.stream()
                .map(courseMapper::toCourseResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void publishCourse(Long id) {
        Course course = findCourseById(id);
        course.setCourseStatusEnum(CourseStatus.PUBLISHED);

        courseRepository.save(course);
    }


    private Teacher findTeacherByEmail(String email){
        return teacherRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("Teacher not found with the email:"+ email)
        );
    }

    private Category findCategoryById(Long id){
        return categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Category not found with the id:"+ id)
        );
    }

    private Skill findSkillById(Long id){
        return skillRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("skill not found with the id:"+ id)
        );
    }


    private Course findCourseById(Long id){
        return courseRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("course not found with the id:"+ id)
        );
    }

    private void categoryCanContainsCourse(Category category){
        if (!category.getContainsCategories()){
            throw new BadRequestException("this category cannot contains any course");
        }
    }


}
