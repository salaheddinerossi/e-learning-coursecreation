package com.example.coursecreation.serviceImpl;

import com.example.coursecreation.dto.LessonDto;
import com.example.coursecreation.dto.SummaryRequest;
import com.example.coursecreation.exception.ResourceNotFoundException;
import com.example.coursecreation.mapper.LessonMapper;
import com.example.coursecreation.model.Chapter;
import com.example.coursecreation.model.Lesson;
import com.example.coursecreation.model.Quizzes.*;
import com.example.coursecreation.repository.ChapterRepository;
import com.example.coursecreation.repository.CourseRepository;
import com.example.coursecreation.repository.LessonRepository;
import com.example.coursecreation.response.JsonResponse.DetailedTranscriptionResponse;
import com.example.coursecreation.response.JsonResponse.SummaryResponse;
import com.example.coursecreation.response.lessonResponse.LessonCreatedResponse;
import com.example.coursecreation.response.lessonResponse.LessonDetails;
import com.example.coursecreation.response.quizResponses.QuestionResponse;
import com.example.coursecreation.response.quizResponses.QuizNoAnswerResponse;
import com.example.coursecreation.service.AiService;
import com.example.coursecreation.service.LessonService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


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

        if (lesson.getIsDeleted()){
            throw  new ResourceNotFoundException("this lesson has been deleted by the owner");
        }


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

    @Override
    public LessonCreatedResponse modifyResponse(Long id, LessonDto lessonDto) {

        Lesson lesson = findLessonById(id);

        if (lesson.getIsDeleted()){
            throw  new ResourceNotFoundException("this lesson has been deleted by the owner");
        }

        lesson.setTitle(lessonDto.getTitle());
        lesson.setDescription(lessonDto.getDescription());
        lessonRepository.save(lesson);
        return lessonMapper.lessonToLessonCreatedResponse(lessonRepository.save(lesson));
    }

    @Override
    @Transactional
    public LessonDetails getLessonDetails(Long id) {
        Lesson lesson = findLessonById(id);
        LessonDetails lessonDetails = lessonMapper.lessonToLessonDetails(lesson);


        List<QuizNoAnswerResponse> quizNoAnswerResponses = new ArrayList<>();

        for (Quiz quiz:lesson.getQuizzes()){

            if (quiz.getIsDeleted()){
                continue;
            }

            QuizNoAnswerResponse quizNoAnswerResponse = new QuizNoAnswerResponse();

            if (!quiz.getQuestions().isEmpty()) {
                Object firstQuestion = quiz.getQuestions().get(0); // Get the first question

                if (firstQuestion instanceof MultipleChoiceQuestion) {
                    quizNoAnswerResponse.setType("MultipleChoiceQuestion");
                } else if (firstQuestion instanceof TrueFalseQuestion) {
                    quizNoAnswerResponse.setType("TrueFalseQuestion");
                } else if (firstQuestion instanceof ExplanatoryQuestion) {
                    quizNoAnswerResponse.setType("ExplanatoryQuestion");
                }
            }

            List<QuestionResponse> questionResponses = new ArrayList<>();
            for (Question question : quiz.getQuestions()){
                QuestionResponse questionResponse = new QuestionResponse();
                questionResponse.setQuestion(question.getPrompt());
                questionResponse.setId(question.getId());
                if (question instanceof MultipleChoiceQuestion){
                    questionResponse.setOptions(((MultipleChoiceQuestion) question).getOptions());
                }else{
                    questionResponse.setOptions(null);
                }
                questionResponses.add(questionResponse);
            }
            quizNoAnswerResponse.setQuestionResponses(questionResponses);
            quizNoAnswerResponses.add(quizNoAnswerResponse);
        }
        lessonDetails.setQuizNoAnswerResponses(quizNoAnswerResponses);

        return lessonDetails;
    }

    @Override
    public void deleteLesson(Long id) {
        Lesson lesson = findLessonById(id);
        if (lesson.getIsDeleted()){
            throw new ResourceNotFoundException("lesson already deleted");
        }
        lesson.setIsDeleted(true);
        List<Quiz> quizzes = lesson.getQuizzes();
        for (Quiz quiz:quizzes){
            quiz.setIsDeleted(true);
        }
        lessonRepository.save(lesson);
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
