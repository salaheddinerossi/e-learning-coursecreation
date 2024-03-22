package com.example.coursecreation.serviceImpl;

import com.example.coursecreation.dto.QuizInstructions;
import com.example.coursecreation.exception.ResourceNotFoundException;
import com.example.coursecreation.model.Lesson;
import com.example.coursecreation.model.Quizzes.*;
import com.example.coursecreation.repository.LessonRepository;
import com.example.coursecreation.repository.QuizRepository;
import com.example.coursecreation.response.JsonResponse.*;
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
    public List<MultipleChoiceResponse> generateMultipleChoiceQuiz(Long lessonID) {
        List<MultipleChoiceResponse> multipleChoiceResponses = Objects.requireNonNull(aiService.generateMultipleChoiceQuiz(findLessonById(lessonID).getTranscribe()," ").block()).getQuestions();

        Quiz quiz = createMultipleChoiceQuiz(lessonID,multipleChoiceResponses);

        quizRepository.save(quiz);

        return multipleChoiceResponses;
    }

    @Override
    @Transactional
    public List<ExplanatoryResponse> generateExplanatoryQuiz(Long lessonID) {
        List<ExplanatoryResponse> explanatoryResponses = Objects.requireNonNull(aiService.generateExplanatoryQuiz(findLessonById(lessonID).getTranscribe()," ").block()).getQuestions();

        Quiz quiz = createExplanatoryQuiz(lessonID,explanatoryResponses);

        quizRepository.save(quiz);

        return explanatoryResponses;
    }

    @Override
    @Transactional
    public List<TrueFalseResponse> generateTrueFalseQuiz(Long lessonID) {
        List<TrueFalseResponse> trueFalseResponses = Objects.requireNonNull(aiService.generateTrueFalseQuiz(findLessonById(lessonID).getTranscribe(), " ").block()).getQuestions();

        Quiz quiz = createTrueFalseQuiz(lessonID,trueFalseResponses);

        quizRepository.save(quiz);

        return trueFalseResponses;
    }

    @Override
    public List<MultipleChoiceResponse> modifyMultipleChoiceQuiz(Long quizId, QuizInstructions quizInstructions) {

        Quiz quiz1 = findQuizById(quizId);
        Lesson lesson =quiz1.getLesson();


        List<MultipleChoiceResponse> multipleChoiceResponses = Objects.requireNonNull(aiService.generateMultipleChoiceQuiz(lesson.getTranscribe(),quizInstructions.getAdditional_instructions()).block()).getQuestions();

        Quiz quiz = createMultipleChoiceQuiz(lesson.getId(), multipleChoiceResponses);

        quiz.setId(quiz1.getId());

        quizRepository.save(quiz);

        return multipleChoiceResponses;

    }

    @Override
    public List<ExplanatoryResponse> modifyExplanatoryQuiz(Long quizId,QuizInstructions quizInstructions) {
        Quiz quiz1 = findQuizById(quizId);
        Lesson lesson =quiz1.getLesson();


        List<ExplanatoryResponse> explanatoryResponses = Objects.requireNonNull(aiService.generateExplanatoryQuiz(lesson.getTranscribe(),quizInstructions.getAdditional_instructions()).block()).getQuestions();

        Quiz quiz = createExplanatoryQuiz(lesson.getId(), explanatoryResponses);

        quiz.setId(quiz1.getId());

        quizRepository.save(quiz);

        return explanatoryResponses;
    }

    @Override
    public List<TrueFalseResponse> modifyTrueFalseQuiz(Long quizId,QuizInstructions quizInstructions) {
        Quiz quiz1 = findQuizById(quizId);
        Lesson lesson =quiz1.getLesson();


        List<TrueFalseResponse> trueFalseResponses = Objects.requireNonNull(aiService.generateTrueFalseQuiz(lesson.getTranscribe(),quizInstructions.getAdditional_instructions()).block()).getQuestions();

        Quiz quiz = createTrueFalseQuiz(lesson.getId(), trueFalseResponses);

        quiz.setId(quiz1.getId());

        quizRepository.save(quiz);

        return trueFalseResponses;
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
