package com.example.coursecreation.serviceImpl;

import com.example.coursecreation.dto.LessonDto;
import com.example.coursecreation.dto.SummaryRequest;
import com.example.coursecreation.exception.ResourceNotFoundException;
import com.example.coursecreation.mapper.LessonMapper;
import com.example.coursecreation.model.Chapter;
import com.example.coursecreation.model.Lesson;
import com.example.coursecreation.repository.ChapterRepository;
import com.example.coursecreation.repository.CourseRepository;
import com.example.coursecreation.repository.LessonRepository;
import com.example.coursecreation.response.JsonResponse.DetailedTranscriptionResponse;
import com.example.coursecreation.response.JsonResponse.SummaryResponse;
import com.example.coursecreation.response.lessonResponse.LessonCreatedResponse;
import com.example.coursecreation.service.AiService;
import com.example.coursecreation.service.LessonService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class LessonServiceImpl implements LessonService {

    final
    LessonRepository lessonRepository;

    final
    ChapterRepository chapterRepository;

    final
    CourseRepository courseRepository;

    final
    LessonMapper lessonMapper;

    final
    AiService aiService;

    public LessonServiceImpl(LessonRepository lessonRepository, ChapterRepository chapterRepository, CourseRepository courseRepository, LessonMapper lessonMapper, AiService aiService) {
        this.lessonRepository = lessonRepository;
        this.chapterRepository = chapterRepository;
        this.courseRepository = courseRepository;
        this.lessonMapper = lessonMapper;
        this.aiService = aiService;
    }



    @Override
    @Transactional
    public LessonCreatedResponse createLesson(LessonDto lessonDto) {
        Lesson lesson = lessonMapper.lessonDtoToLesson(lessonDto);

        lesson.setChapter(findChapterById(lessonDto.getChapterId()));

        if(lessonDto.getUsesAI()){
            DetailedTranscriptionResponse transcriptionResponse = aiService.getTranscribe(lessonDto.getMaterial()).block();
            if(transcriptionResponse != null) {
                lesson.setTranscribe(transcriptionResponse.getTranscription());
                lesson.setSummary(transcriptionResponse.getSummary());
                lesson.setAdvices(transcriptionResponse.getAdvice());

            }
        }



        return lessonMapper.lessonToLessonCreatedResponse(lessonRepository.save(lesson));
    }

    @Override
    public SummaryResponse addSummary(SummaryRequest summaryRequest, Long lessonId) {
        Lesson lesson = findLessonById(lessonId);

        summaryRequest.setTranscribtion(lesson.getTranscribe());

        if (summaryRequest.getSummary_type()==null){
            summaryRequest.setSummary_type(" ");
        }

        if (summaryRequest.getSummary_type()==null){
            summaryRequest.setSummary_type(" ");
        }


        SummaryResponse summaryResponse = aiService.generateSummary(summaryRequest).block();
        assert summaryResponse != null;
        lesson.setSummary(summaryResponse.getSummary());

        lessonRepository.save(lesson);

        return summaryResponse;
    }


    private Chapter findChapterById(Long id){
        return chapterRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("chapter not found with the id:"+ id)
        );
    }

    Lesson findLessonById(Long id){
        return lessonRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("lesson not found with the id:"+ id)
        );
    }




}
