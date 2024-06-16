package com.example.coursecreation.serviceImpl;

import com.example.coursecreation.Enums.CourseStatus;
import com.example.coursecreation.dto.CourseDto;
import com.example.coursecreation.exception.*;
import com.example.coursecreation.mapper.ChapterMapper;
import com.example.coursecreation.mapper.CourseMapper;
import com.example.coursecreation.model.*;
import com.example.coursecreation.repository.*;
import com.example.coursecreation.response.CategoryCoursesResponse;
import com.example.coursecreation.response.CourseCreatedResponse;
import com.example.coursecreation.response.CourseDetailsResponse;
import com.example.coursecreation.response.CourseResponse;
import com.example.coursecreation.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class CourseServiceImpl implements CourseService {

    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private final SkillRepository skillRepository;
    private final CourseMapper courseMapper;

    final
    StorageService storageService;

    final
    ChapterRepository chapterRepository;

    final
    ChapterMapper chapterMapper;

    @Autowired
    public CourseServiceImpl(TeacherRepository teacherRepository, CourseRepository courseRepository,
                             CategoryRepository categoryRepository, SkillRepository skillRepository,
                             CourseMapper courseMapper, ChapterMapper chapterMapper, ChapterRepository chapterRepository, StorageService storageService) {
        this.teacherRepository = teacherRepository;
        this.courseRepository = courseRepository;
        this.categoryRepository = categoryRepository;
        this.skillRepository = skillRepository;
        this.courseMapper = courseMapper;
        this.chapterMapper = chapterMapper;
        this.chapterRepository = chapterRepository;
        this.storageService = storageService;
    }

    @Override
    public CourseCreatedResponse createCourse(CourseDto courseDto, String email) throws IOException {
        Course course = courseMapper.toCourse(courseDto);

        String fileName = generateUniqueFileName(Objects.requireNonNull(courseDto.getImage().getOriginalFilename()));
        String presignedUrl = storageService.generatePresignedUrl(fileName);
        storageService.uploadFileToS3(courseDto.getImage(), presignedUrl);

        course.setImage(storageService.getFileUrl(fileName).toString());


        Teacher teacher = findTeacherByEmail(email);

        if (!teacher.getIsActive()){
            throw new UnauthorizedException("your account is not active yet");
        }

        course.setTeacher(teacher);
        course.setCategory(findCategoryById(courseDto.getCategoryId()));
        if(courseDto.getSkillId()!=null){
            course.setSkill(findSkillById(courseDto.getSkillId()));
        }
        course.setCourseStatusEnum(CourseStatus.DRAFT);
        course.setDate(LocalDate.now());



        return courseMapper.toCourseCreatedResponse(courseRepository.save(course));
    }

    @Override
    public CourseCreatedResponse modifyCourse(Long id, CourseDto courseDto) throws IOException {

        Course course = findCourseById(id);
        course.setAbout(courseDto.getAbout());
        course.setCourseLevelEnum(courseDto.getCourseLevelEnum());

        String fileName = generateUniqueFileName(Objects.requireNonNull(courseDto.getImage().getOriginalFilename()));
        String presignedUrl = storageService.generatePresignedUrl(fileName);
        storageService.uploadFileToS3(courseDto.getImage(), presignedUrl);

        course.setImage(storageService.getFileUrl(fileName).toString());

        Category category = findCategoryById(courseDto.getCategoryId());

        categoryCanContainsCourse(category);
        course.setCategory(category);

        course.setTitle(courseDto.getTitle());
        course.setRequirements(courseDto.getRequirements());

        return courseMapper.toCourseCreatedResponse(courseRepository.save(course));

    }

    @Override
    public CourseDetailsResponse getCourseDetails(Long id,Boolean isTeacher){

        Course course = findCourseById(id);


        if (course.getCourseStatusEnum()!=CourseStatus.APPROVED && !isTeacher){
            throw new ResourceNotFoundException("this course is not approved yet ");
        }

        List<Chapter> filteredChapters = filterOutDeletedChapters(course.getChapters());

        course.getChapters().clear();
        course.getChapters().addAll(filteredChapters);


        return courseMapper.toCourseDetailsResponse(course);
    }

    @Override
    public CategoryCoursesResponse getCoursesByCategory(Long categoryId) {

        Category category = findCategoryById(categoryId);

        List<Course> courses = courseRepository.findCourseByCategory(category);
        List<CourseResponse> courseResponses =  courses.stream()
                .filter(course -> course.getCourseStatusEnum() == CourseStatus.APPROVED)
                .map(courseMapper::toCourseResponse)
                .collect(Collectors.toList());

        CategoryCoursesResponse categoryCoursesResponse = new CategoryCoursesResponse();
        categoryCoursesResponse.setCourseResponses(courseResponses);
        categoryCoursesResponse.setId(categoryId);
        categoryCoursesResponse.setDescription(category.getDescription());
        categoryCoursesResponse.setTitle(category.getTitle());
        return categoryCoursesResponse;

    }

    @Override
    public List<CourseResponse> getPublishedCourses() {
        List<Course> courses = courseRepository.findAll();
        return courses.stream()
                .filter(course -> course.getCourseStatusEnum() == CourseStatus.PUBLISHED)
                .map(courseMapper::toCourseResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseResponse> getTeacherCourses(String email) {

        List<Course> courses = courseRepository.findByTeacherEmail(email);

        return courseMapper.toCourseResponse(courses);
    }

    @Override
    public CourseDetailsResponse getPublishedCourseDetails(Long id) {
        Course course = findCourseById(id);
        if (course.getCourseStatusEnum()==CourseStatus.DRAFT){
            throw new ResourceNotFoundException("this course is not published yet ");
        }

        List<Chapter> filteredChapters = filterOutDeletedChapters(course.getChapters());


        course.getChapters().clear();
        course.getChapters().addAll(filteredChapters);


        return courseMapper.toCourseDetailsResponse(course);

    }

    @Override
    public CourseCreatedResponse getCourseFromTeacher(Long courseId) {
        return courseMapper.toCourseCreatedResponse(findCourseById(courseId));
    }

    @Override
    public Long getCourseIdFromChapterId(Long chapterId) {


        return findChapterById(chapterId).getCourse().getId();
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

    private Chapter findChapterById(Long id){
        return chapterRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("chapter not found with the id:"+ id)
        );
    }


    private Course findCourseById(Long id){
        return courseRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("course not found with the id:"+ id)
        );
    }

    private void categoryCanContainsCourse(Category category){
        if (category.getContainsCategories()){
            throw new BadRequestException("this category cannot contains any course");
        }
    }

    private List<Chapter> filterOutDeletedChapters(List<Chapter> originalChapters) {
        originalChapters.stream()
                .filter(chapter -> !Boolean.TRUE.equals(chapter.getIsDeleted()))
                .forEach(chapter -> {
                    // Filter out lessons marked as deleted
                    List<Lesson> filteredLessons = filterOutDeletedLessons(chapter.getLessons());
                    chapter.getLessons().clear();
                    chapter.getLessons().addAll(filteredLessons);

                    // If the chapter can contain sub-chapters, recursively handle sub-chapters
                    if (Boolean.TRUE.equals(chapter.getContainsChapters())) {
                        List<Chapter> filteredSubChapters = filterOutDeletedSubChapters(chapter.getChildChapters());
                        chapter.getChildChapters().clear();
                        chapter.getChildChapters().addAll(filteredSubChapters);
                    }
                });

        // Return only top-level chapters (with no parent) that are not marked as deleted
        return originalChapters.stream()
                .filter(chapter -> !Boolean.TRUE.equals(chapter.getIsDeleted()) && chapter.getParentChapter() == null)
                .collect(Collectors.toList());
    }

    private List<Chapter> filterOutDeletedSubChapters(List<Chapter> originalSubChapters) {
        return originalSubChapters.stream()
                .filter(subChapter -> !Boolean.TRUE.equals(subChapter.getIsDeleted()))
                .map(subChapter -> {
                    // Filter out lessons in sub-chapters marked as deleted
                    subChapter.setLessons(filterOutDeletedLessons(subChapter.getLessons()));

                    // Recursively filter sub-sub-chapters
                    if (Boolean.TRUE.equals(subChapter.getContainsChapters())) {
                        subChapter.setChildChapters(filterOutDeletedSubChapters(subChapter.getChildChapters()));
                    }

                    return subChapter;
                })
                .collect(Collectors.toList());
    }

    private List<Lesson> filterOutDeletedLessons(List<Lesson> originalLessons) {
        return originalLessons.stream()
                .filter(lesson -> !Boolean.TRUE.equals(lesson.getIsDeleted()))
                .collect(Collectors.toList());
    }

    private String generateUniqueFileName(String originalFileName) {
        String extension = "";
        int i = originalFileName.lastIndexOf('.');
        if (i > 0) {
            extension = originalFileName.substring(i);
        }
        return UUID.randomUUID().toString() + extension;
    }



}
