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
import com.example.coursecreation.response.JsonResponse.*;
import com.example.coursecreation.response.TeacherLessonDetails;
import com.example.coursecreation.response.lessonResponse.LessonCreatedResponse;
import com.example.coursecreation.response.lessonResponse.LessonDetails;
import com.example.coursecreation.response.quizResponses.*;
import com.example.coursecreation.service.AiService;
import com.example.coursecreation.service.LessonService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


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

    final
    StorageService storageService;

    public LessonServiceImpl(LessonRepository lessonRepository, ChapterRepository chapterRepository, CourseRepository courseRepository, LessonMapper lessonMapper, AiService aiService, StorageService storageService) {
        this.lessonRepository = lessonRepository;
        this.chapterRepository = chapterRepository;
        this.courseRepository = courseRepository;
        this.lessonMapper = lessonMapper;
        this.aiService = aiService;
        this.storageService = storageService;
    }



    @Override
    @Transactional
    public LessonCreatedResponse createLesson(LessonDto lessonDto) throws IOException {
        Lesson lesson = lessonMapper.lessonDtoToLesson(lessonDto);

        String fileName = generateUniqueFileName(Objects.requireNonNull(lessonDto.getMaterial().getOriginalFilename()));
        String presignedUrl = storageService.generatePresignedUrl(fileName);
        storageService.uploadFileToS3(lessonDto.getMaterial(), presignedUrl);

        lesson.setMaterial(storageService.getFileUrl(fileName).toString());

        lesson.setChapter(findChapterById(lessonDto.getChapterId()));

        if(lessonDto.getUsesAI()){
            DetailedTranscriptionResponse transcriptionResponse = aiService.getTranscribe(lesson.getMaterial()).block();
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

    @Override
    public TeacherLessonDetails getTeacherLessonDetails(Long id) {

        Lesson lesson = findLessonById(id);

        if (lesson.getIsDeleted()){
            throw  new ResourceNotFoundException("couldn't find a lesson with this id"+id);
        }

        TeacherLessonDetails teacherLessonDetails =  lessonMapper.lessonToTeacherLessonDetails(lesson);

        QuizzesResponse quizzesResponse = new QuizzesResponse();

        for (Quiz quiz:lesson.getQuizzes()){

            if (quiz.getIsDeleted()){
                continue;
            }
            if (quiz.getQuestions().get(0)  instanceof MultipleChoiceQuestion){

                MultipleChoiceQuiz multipleChoiceQuiz= new MultipleChoiceQuiz();
                multipleChoiceQuiz.setId(quiz.getId());
                List<MultipleChoiceResponse> multipleChoiceResponses = new ArrayList<>();
                for (Question question:quiz.getQuestions()){

                    MultipleChoiceResponse multipleChoiceResponse = new MultipleChoiceResponse();
                    multipleChoiceResponse.setId(question.getId());
                    multipleChoiceResponse.setPrompt(question.getPrompt());
                    multipleChoiceResponse.setCorrectAnswer(((MultipleChoiceQuestion) question).getCorrectAnswer());
                    multipleChoiceResponse.setOptions(((MultipleChoiceQuestion)question).getOptions());

                    multipleChoiceResponses.add(multipleChoiceResponse);

                }
                multipleChoiceQuiz.setMultipleChoiceResponses(multipleChoiceResponses);
                quizzesResponse.getMultipleChoiceQuizzes().add(multipleChoiceQuiz);

            }else if (quiz.getQuestions().get(0) instanceof TrueFalseQuestion){

                TrueFalseQuiz trueFalseQuiz = getTrueFalseQuiz(quiz);
                quizzesResponse.getTrueFalseQuizzes().add(trueFalseQuiz);

            }else if (quiz.getQuestions().get(0) instanceof ExplanatoryQuestion){
                ExplanatoryQuiz explanatoryQuiz = getExplanatoryQuiz(quiz);
                quizzesResponse.getExplanatoryQuizzes().add(explanatoryQuiz);

            }
        }
        teacherLessonDetails.setQuizzesResponse(quizzesResponse);
        return teacherLessonDetails;
    }

    private static ExplanatoryQuiz getExplanatoryQuiz(Quiz quiz) {
        ExplanatoryQuiz explanatoryQuiz = new ExplanatoryQuiz();
        explanatoryQuiz.setId(quiz.getId());
        List<ExplanatoryResponse> explanatoryResponses = new ArrayList<>();
        for (Question question: quiz.getQuestions()){

            ExplanatoryResponse explanatoryResponse =new ExplanatoryResponse();

            explanatoryResponse.setPrompt(question.getPrompt());
            explanatoryResponse.setId(question.getId());
            explanatoryResponse.setCorrectExplanation(((ExplanatoryQuestion)question).getCorrectExplanation());

            explanatoryResponses.add(explanatoryResponse);

        }

        explanatoryQuiz.setExplanatoryResponses(explanatoryResponses);
        return explanatoryQuiz;
    }

    private static TrueFalseQuiz getTrueFalseQuiz(Quiz quiz) {
        TrueFalseQuiz trueFalseQuiz = new TrueFalseQuiz();
        trueFalseQuiz.setId(quiz.getId());
        List<TrueFalseResponse> trueFalseResponses = new ArrayList<>();
        for (Question question: quiz.getQuestions()){

            TrueFalseResponse trueFalseResponse = new TrueFalseResponse();

            trueFalseResponse.setPrompt(question.getPrompt());
            trueFalseResponse.setId(question.getId());
            trueFalseResponse.setCorrectAnswer(((TrueFalseQuestion)question).getCorrectAnswer());
            trueFalseResponses.add(trueFalseResponse);

        }

        trueFalseQuiz.setTrueFalseResponses(trueFalseResponses);
        return trueFalseQuiz;
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

    private String generateUniqueFileName(String originalFileName) {
        String extension = "";
        int i = originalFileName.lastIndexOf('.');
        if (i > 0) {
            extension = originalFileName.substring(i);
        }
        return UUID.randomUUID().toString() + extension;
    }


}
