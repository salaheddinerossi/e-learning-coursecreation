package com.example.coursecreation.serviceImpl;

import com.example.coursecreation.Enums.CourseStatus;
import com.example.coursecreation.dto.QuizInstructions;
import com.example.coursecreation.exception.BadRequestException;
import com.example.coursecreation.exception.ResourceNotFoundException;
import com.example.coursecreation.model.Lesson;
import com.example.coursecreation.model.Quizzes.*;
import com.example.coursecreation.repository.LessonRepository;
import com.example.coursecreation.repository.QuizRepository;
import com.example.coursecreation.response.JsonResponse.*;
import com.example.coursecreation.response.quizResponses.ExplanatoryQuiz;
import com.example.coursecreation.response.quizResponses.MultipleChoiceQuiz;
import com.example.coursecreation.response.quizResponses.TrueFalseQuiz;
import com.example.coursecreation.service.AiService;
import com.example.coursecreation.service.QuizService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class QuizServiceImpl implements QuizService {


    final
    LessonRepository lessonRepository;

    final
    QuizRepository quizRepository;

    public QuizServiceImpl(LessonRepository lessonRepository, QuizRepository quizRepository, AiService aiService) {
        this.lessonRepository = lessonRepository;
        this.quizRepository = quizRepository;
        this.aiService = aiService;
    }

    final
    AiService aiService;

    @Override
    @Transactional
    public MultipleChoiceQuiz generateMultipleChoiceQuiz(Long lessonID) {
        List<MultipleChoiceResponse> multipleChoiceResponses = Objects.requireNonNull(aiService.generateMultipleChoiceQuiz(findLessonById(lessonID).getTranscribe()," ").block()).getQuestions();

        return getMultipleChoiceQuiz(lessonID, multipleChoiceResponses);
    }

    @Override
    @Transactional
    public ExplanatoryQuiz generateExplanatoryQuiz(Long lessonID) {
        List<ExplanatoryResponse> explanatoryResponses = Objects.requireNonNull(aiService.generateExplanatoryQuiz(findLessonById(lessonID).getTranscribe()," ").block()).getQuestions();

        return getExplanatoryQuiz(lessonID, explanatoryResponses);
    }

    @Override
    @Transactional
    public TrueFalseQuiz generateTrueFalseQuiz(Long lessonID) {
        List<TrueFalseResponse> trueFalseResponses = Objects.requireNonNull(aiService.generateTrueFalseQuiz(findLessonById(lessonID).getTranscribe(), " ").block()).getQuestions();

        return getTrueFalseQuiz(lessonID, trueFalseResponses);
    }

    @Override
    public MultipleChoiceQuiz createMultipleChoiceQuizManually(Long lessonID, List<MultipleChoiceResponse> multipleChoiceResponses) {
        return getMultipleChoiceQuiz(lessonID, multipleChoiceResponses);
    }


    @Override
    public ExplanatoryQuiz createExplanatoryQuizManually(Long lessonID, List<ExplanatoryResponse> explanatoryResponses) {
        return getExplanatoryQuiz(lessonID, explanatoryResponses);
    }


    @Override
    public TrueFalseQuiz createTrueFalseQuizManually(Long lessonID, List<TrueFalseResponse> trueFalseResponses) {
        return getTrueFalseQuiz(lessonID, trueFalseResponses);
    }

    private MultipleChoiceQuiz getMultipleChoiceQuiz(Long lessonID, List<MultipleChoiceResponse> multipleChoiceResponses) {
        if (multipleChoiceResponses.isEmpty()){
            throw new BadRequestException("error during generating the quiz");
        }

        Quiz quiz = createMultipleChoiceQuiz(lessonID,multipleChoiceResponses);

        MultipleChoiceQuiz multipleChoiceQuiz = new MultipleChoiceQuiz();
        Quiz quiz1 = quizRepository.save(quiz);
        multipleChoiceQuiz.setId(quiz1.getId());
        multipleChoiceQuiz.setMultipleChoiceResponses(multipleChoiceResponses);

        return multipleChoiceQuiz;
    }


    private ExplanatoryQuiz getExplanatoryQuiz(Long lessonID, List<ExplanatoryResponse> explanatoryResponses) {
        if (explanatoryResponses.isEmpty()){
            throw new BadRequestException("error during generating the quiz");
        }

        Quiz quiz = createExplanatoryQuiz(lessonID,explanatoryResponses);

        Quiz quiz1 = quizRepository.save(quiz);

        ExplanatoryQuiz explanatoryQuiz = new ExplanatoryQuiz();

        explanatoryQuiz.setId(quiz1.getId());
        explanatoryQuiz.setExplanatoryResponses(explanatoryResponses);

        return explanatoryQuiz;
    }


    private TrueFalseQuiz getTrueFalseQuiz(Long lessonID, List<TrueFalseResponse> trueFalseResponses) {
        if (trueFalseResponses.isEmpty()){
            throw new BadRequestException("error during generating the quiz");
        }

        Quiz quiz = createTrueFalseQuiz(lessonID,trueFalseResponses);

        Quiz quiz1 = quizRepository.save(quiz);

        TrueFalseQuiz trueFalseQuiz = new TrueFalseQuiz();

        trueFalseQuiz.setId(quiz1.getId());
        trueFalseQuiz.setTrueFalseResponses(trueFalseResponses);

        return trueFalseQuiz;
    }


    @Override
    public MultipleChoiceQuiz modifyMultipleChoiceQuiz(Long quizId, QuizInstructions quizInstructions) {

        Quiz quiz1 = findQuizById(quizId);

        if (quiz1.getLesson().getChapter().getCourse().getCourseStatusEnum()!= CourseStatus.DRAFT){
            throw new BadRequestException("course is not anymore in draft you can't modify the quiz");
        }

        Lesson lesson =quiz1.getLesson();

        List<MultipleChoiceResponse> multipleChoiceResponses = Objects.requireNonNull(aiService.generateMultipleChoiceQuiz(lesson.getTranscribe(),quizInstructions.getAdditional_instructions()).block()).getQuestions();

        if(multipleChoiceResponses.isEmpty()){
            throw  new BadRequestException("no questions have been generated");
        }

        quiz1.getQuestions().clear();

        for(MultipleChoiceResponse multipleChoiceResponse:multipleChoiceResponses){
            MultipleChoiceQuestion multipleChoiceQuestion = new MultipleChoiceQuestion();
            multipleChoiceQuestion.setPrompt(multipleChoiceResponse.getPrompt());
            multipleChoiceQuestion.setOptions(multipleChoiceResponse.getOptions());
            multipleChoiceQuestion.setCorrectAnswer(multipleChoiceResponse.getCorrectAnswer());
            multipleChoiceQuestion.setQuiz(quiz1);
            quiz1.getQuestions().add(multipleChoiceQuestion);
        }

        quizRepository.save(quiz1);


        MultipleChoiceQuiz multipleChoiceQuiz = new MultipleChoiceQuiz();

        multipleChoiceQuiz.setMultipleChoiceResponses(multipleChoiceResponses);
        multipleChoiceQuiz.setId(quizId);

        return multipleChoiceQuiz;

    }

    @Override
    public ExplanatoryQuiz modifyExplanatoryQuiz(Long quizId,QuizInstructions quizInstructions) {
        Quiz quiz1 = findQuizById(quizId);
        Lesson lesson =quiz1.getLesson();


        List<ExplanatoryResponse> explanatoryResponses = Objects.requireNonNull(aiService.generateExplanatoryQuiz(lesson.getTranscribe(),quizInstructions.getAdditional_instructions()).block()).getQuestions();

        if (explanatoryResponses.isEmpty()){
            throw new BadRequestException("no questions have been generated");
        }

        quiz1.getQuestions().clear();

        for (ExplanatoryResponse explanatoryResponse: explanatoryResponses){
            ExplanatoryQuestion explanatoryQuestion = new ExplanatoryQuestion();
            explanatoryQuestion.setQuiz(quiz1);
            explanatoryQuestion.setPrompt(explanatoryResponse.getPrompt());
            explanatoryQuestion.setCorrectExplanation(explanatoryResponse.getCorrectExplanation());

            quiz1.getQuestions().add(explanatoryQuestion);
        }

        quizRepository.save(quiz1);

        ExplanatoryQuiz explanatoryQuiz = new ExplanatoryQuiz();

        explanatoryQuiz.setExplanatoryResponses(explanatoryResponses);
        explanatoryQuiz.setId(quizId);

        return explanatoryQuiz;
    }

    @Override
    @Transactional
    public TrueFalseQuiz modifyTrueFalseQuiz(Long quizId,QuizInstructions quizInstructions) {
        Quiz quiz1 = findQuizById(quizId);
        Lesson lesson =quiz1.getLesson();


        List<TrueFalseResponse> trueFalseResponses = Objects.requireNonNull(aiService.generateTrueFalseQuiz(lesson.getTranscribe(),quizInstructions.getAdditional_instructions()).block()).getQuestions();


        if (trueFalseResponses.isEmpty()){
            throw new BadRequestException("no questions have been generated");
        }

        quiz1.getQuestions().clear();

        for (TrueFalseResponse trueFalseResponse:trueFalseResponses){
            TrueFalseQuestion trueFalseQuestion = new TrueFalseQuestion();

            trueFalseQuestion.setPrompt(trueFalseResponse.getPrompt());
            trueFalseQuestion.setCorrectAnswer(trueFalseResponse.getCorrectAnswer());
            trueFalseQuestion.setQuiz(quiz1);

            quiz1.getQuestions().add(trueFalseQuestion);
        }


        quizRepository.save(quiz1);

        TrueFalseQuiz trueFalseQuiz = new TrueFalseQuiz();

        trueFalseQuiz.setTrueFalseResponses(trueFalseResponses);
        trueFalseQuiz.setId(quizId);

        return trueFalseQuiz;
    }

    @Override
    @Transactional
    public void deleteQuiz(Long id) {
        Quiz quiz =findQuizById(id);

        quiz.setIsDeleted(true);
        quizRepository.save(quiz);
    }

    private Quiz createMultipleChoiceQuiz(Long lessonID,List<MultipleChoiceResponse> multipleChoiceResponses){

        Quiz quiz = new Quiz();

        for(MultipleChoiceResponse multipleChoiceResponse:multipleChoiceResponses){

            MultipleChoiceQuestion multipleChoiceQuestion = new MultipleChoiceQuestion();

            multipleChoiceQuestion.setPrompt(multipleChoiceResponse.getPrompt());
            multipleChoiceQuestion.setOptions(multipleChoiceResponse.getOptions());
            multipleChoiceQuestion.setCorrectAnswer(multipleChoiceResponse.getCorrectAnswer());
            multipleChoiceQuestion.setQuiz(quiz);

            quiz.getQuestions().add(multipleChoiceQuestion);

        }

        quiz.setLesson(findLessonById(lessonID));

        return quiz;

    }

    private Quiz createExplanatoryQuiz(Long lessonID,List<ExplanatoryResponse> explanatoryResponses){
        Quiz quiz = new Quiz();

        for(ExplanatoryResponse explanatoryResponse:explanatoryResponses){
            ExplanatoryQuestion explanatoryQuestion = new ExplanatoryQuestion();

            explanatoryQuestion.setPrompt(explanatoryResponse.getPrompt());
            explanatoryQuestion.setCorrectExplanation(explanatoryResponse.getCorrectExplanation());
            explanatoryQuestion.setQuiz(quiz);

            quiz.getQuestions().add(explanatoryQuestion);
        }

        quiz.setLesson(findLessonById(lessonID));

        return quiz;
    }

    private Quiz createTrueFalseQuiz(Long lessonId,List<TrueFalseResponse> trueFalseResponses){

        Quiz quiz = new Quiz();

        for(TrueFalseResponse trueFalseResponse:trueFalseResponses){

            TrueFalseQuestion trueFalseQuestion = new TrueFalseQuestion();

            trueFalseQuestion.setPrompt(trueFalseResponse.getPrompt());
            trueFalseQuestion.setCorrectAnswer(trueFalseResponse.getCorrectAnswer());
            trueFalseQuestion.setQuiz(quiz);

            quiz.getQuestions().add(trueFalseQuestion);

        }

        quiz.setLesson(findLessonById(lessonId));

        quizRepository.save(quiz);

        return quiz;
    }

    private Lesson findLessonById(Long id){
        return lessonRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("lesson not found with the id:"+ id)
        );
    }

    private Quiz findQuizById(Long id){
        return quizRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("quiz not found with the id: "+id)
        );
    }
}
