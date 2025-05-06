package com.example.service_survey.services;

import com.example.service_survey.dto.CreateSurveyRequest;
import com.example.service_survey.dto.QuestionOptionDto;
import com.example.service_survey.dto.SurveyDto;
import com.example.service_survey.dto.SurveyQuestionDto;
import com.example.service_survey.models.*;
import com.example.service_survey.repositories.QuestionOptionRepository;
import com.example.service_survey.repositories.SurveyQuestionRepository;
import com.example.service_survey.repositories.SurveyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SurveyService {
    
    private final SurveyRepository surveyRepository;
    private final SurveyQuestionRepository questionRepository;
    private final QuestionOptionRepository optionRepository;
    
    /**
     * Create a new survey with questions and options
     */
    @Transactional
    public SurveyDto createSurvey(CreateSurveyRequest request) {
        log.info("Creating new survey: {}", request.getTitle());
        
        // Create survey
        Survey survey = Survey.builder()
                .surveyType(request.getSurveyType())
                .title(request.getTitle())
                .description(request.getDescription())
                .isActive(true)
                .build();
        
        Survey savedSurvey = surveyRepository.save(survey);
        
        // Create questions and options
        List<SurveyQuestion> questions = new ArrayList<>();
        
        for (int i = 0; i < request.getQuestions().size(); i++) {
            CreateSurveyRequest.CreateQuestionRequest questionRequest = request.getQuestions().get(i);
            
            // Determine question type from string
            QuestionType questionType = QuestionType.valueOf(questionRequest.getQuestionType());
            
            SurveyQuestion question = SurveyQuestion.builder()
                    .survey(savedSurvey)
                    .questionText(questionRequest.getQuestionText())
                    .questionType(questionType)
                    .required(questionRequest.getRequired() != null ? questionRequest.getRequired() : false)
                    .displayOrder(questionRequest.getDisplayOrder() != null ? questionRequest.getDisplayOrder() : i + 1)
                    .build();
            
            SurveyQuestion savedQuestion = questionRepository.save(question);
            questions.add(savedQuestion);
            
            // Create options if needed
            if (questionRequest.getOptions() != null && !questionRequest.getOptions().isEmpty()) {
                List<QuestionOption> options = new ArrayList<>();
                
                for (int j = 0; j < questionRequest.getOptions().size(); j++) {
                    CreateSurveyRequest.CreateOptionRequest optionRequest = questionRequest.getOptions().get(j);
                    
                    QuestionOption option = QuestionOption.builder()
                            .question(savedQuestion)
                            .optionText(optionRequest.getOptionText())
                            .displayOrder(optionRequest.getDisplayOrder() != null ? optionRequest.getDisplayOrder() : j + 1)
                            .build();
                    
                    options.add(option);
                }
                
                optionRepository.saveAll(options);
            }
        }
        
        // Set questions to the survey
        savedSurvey.setQuestions(questions);
        
        return mapToSurveyDto(savedSurvey);
    }
    
    /**
     * Get all surveys
     */
    public List<SurveyDto> getAllSurveys() {
        log.info("Getting all surveys");
        
        List<Survey> surveys = surveyRepository.findAll();
        return surveys.stream()
                .map(this::mapToSurveyDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Get all active surveys
     */
    public List<SurveyDto> getActiveSurveys() {
        log.info("Getting all active surveys");
        
        List<Survey> surveys = surveyRepository.findByIsActiveTrue();
        return surveys.stream()
                .map(this::mapToSurveyDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Get survey by ID
     */
    public SurveyDto getSurveyById(Long id) {
        log.info("Getting survey with id: {}", id);
        
        Survey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Survey not found with id: " + id));
        
        return mapToSurveyDto(survey);
    }
    
    /**
     * Get surveys by type
     */
    public List<SurveyDto> getSurveysByType(SurveyType surveyType) {
        log.info("Getting surveys with type: {}", surveyType);
        
        List<Survey> surveys = surveyRepository.findBySurveyType(surveyType);
        return surveys.stream()
                .map(this::mapToSurveyDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Get active surveys by type
     */
    public List<SurveyDto> getActiveSurveysByType(SurveyType surveyType) {
        log.info("Getting active surveys with type: {}", surveyType);
        
        List<Survey> surveys = surveyRepository.findByIsActiveTrueAndSurveyType(surveyType);
        return surveys.stream()
                .map(this::mapToSurveyDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Update survey status (activate/deactivate)
     */
    @Transactional
    public SurveyDto updateSurveyStatus(Long id, Boolean isActive) {
        log.info("Updating survey status for id {}: isActive={}", id, isActive);
        
        Survey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Survey not found with id: " + id));
        
        survey.setIsActive(isActive);
        Survey updatedSurvey = surveyRepository.save(survey);
        
        return mapToSurveyDto(updatedSurvey);
    }
    
    /**
     * Delete a survey
     */
    @Transactional
    public void deleteSurvey(Long id) {
        log.info("Deleting survey with id: {}", id);
        
        if (!surveyRepository.existsById(id)) {
            throw new RuntimeException("Survey not found with id: " + id);
        }
        
        surveyRepository.deleteById(id);
    }
    
    /**
     * Map entity to DTO
     */
    private SurveyDto mapToSurveyDto(Survey survey) {
        List<SurveyQuestionDto> questionDtos = new ArrayList<>();
        
        if (survey.getQuestions() != null) {
            for (SurveyQuestion question : survey.getQuestions()) {
                List<QuestionOptionDto> optionDtos = new ArrayList<>();
                
                if (question.getOptions() != null) {
                    for (QuestionOption option : question.getOptions()) {
                        QuestionOptionDto optionDto = QuestionOptionDto.builder()
                                .id(option.getId())
                                .questionId(option.getQuestion().getId())
                                .optionText(option.getOptionText())
                                .displayOrder(option.getDisplayOrder())
                                .build();
                        
                        optionDtos.add(optionDto);
                    }
                }
                
                SurveyQuestionDto questionDto = SurveyQuestionDto.builder()
                        .id(question.getId())
                        .surveyId(question.getSurvey().getId())
                        .questionText(question.getQuestionText())
                        .questionType(question.getQuestionType())
                        .required(question.getRequired())
                        .displayOrder(question.getDisplayOrder())
                        .options(optionDtos)
                        .build();
                
                questionDtos.add(questionDto);
            }
        }
        
        return SurveyDto.builder()
                .id(survey.getId())
                .surveyType(survey.getSurveyType())
                .title(survey.getTitle())
                .description(survey.getDescription())
                .isActive(survey.getIsActive())
                .createdAt(survey.getCreatedAt())
                .updatedAt(survey.getUpdatedAt())
                .questions(questionDtos)
                .build();
    }
}