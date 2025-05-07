package com.example.service_survey.services;

import com.example.service_survey.dto.*;
import com.example.service_survey.models.*;
import com.example.service_survey.repositories.QuestionOptionRepository;
import com.example.service_survey.repositories.SurveyQuestionRepository;
import com.example.service_survey.repositories.SurveyRepository;
import com.example.service_survey.repositories.SurveyResponseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SurveyResponseService {
    
    private final SurveyRepository surveyRepository;
    private final SurveyQuestionRepository questionRepository;
    private final QuestionOptionRepository optionRepository;
    private final SurveyResponseRepository responseRepository;
    
    /**
     * Submit survey response
     */
    @Transactional
    public SurveyResponseDto submitSurveyResponse(SubmitSurveyResponseRequest request) {
        log.info("Submitting survey response for survey: {}, customer: {}", request.getSurveyId(), request.getCustomerId());
        
        // Validate survey exists
        Survey survey = surveyRepository.findById(request.getSurveyId())
                .orElseThrow(() -> new RuntimeException("Survey not found with id: " + request.getSurveyId()));
        
        if (!survey.getIsActive()) {
            throw new RuntimeException("Survey is not active");
        }
        
        // Create survey response
        SurveyResponse surveyResponse = SurveyResponse.builder()
                .survey(survey)
                .customerId(request.getCustomerId())
                .relatedEntityId(request.getRelatedEntityId())
                .relatedEntityType(request.getRelatedEntityType())
                .build();
        
        SurveyResponse savedResponse = responseRepository.save(surveyResponse);
        
        // Get all questions for validation
        List<SurveyQuestion> questions = questionRepository.findBySurveyId(survey.getId());
        Map<Integer, SurveyQuestion> questionMap = questions.stream()
                .collect(Collectors.toMap(SurveyQuestion::getId, Function.identity()));
        
        // Get all options for validation
        List<QuestionOption> allOptions = optionRepository.findAll();
        Map<Integer, QuestionOption> optionMap = allOptions.stream()
                .collect(Collectors.toMap(QuestionOption::getId, Function.identity()));
        
        // Process question responses
        List<QuestionResponse> questionResponses = new ArrayList<>();
        
        for (SubmitSurveyResponseRequest.QuestionAnswerRequest answerRequest : request.getQuestionResponses()) {
            // Validate question exists and beIntegers to the survey
            SurveyQuestion question = questionMap.get(answerRequest.getQuestionId());
            if (question == null) {
                throw new RuntimeException("Question not found with id: " + answerRequest.getQuestionId());
            }
            
            QuestionResponse questionResponse = QuestionResponse.builder()
                    .surveyResponse(savedResponse)
                    .question(question)
                    .build();
            
            // Set appropriate response based on question type
            switch (question.getQuestionType()) {
                case TEXT:
                case TEXTAREA:
                    questionResponse.setTextResponse(answerRequest.getTextResponse());
                    break;
                    
                case SINGLE_CHOICE:
                case MULTIPLE_CHOICE:
                    if (answerRequest.getSelectedOptionId() != null) {
                        QuestionOption option = optionMap.get(answerRequest.getSelectedOptionId());
                        if (option == null || !option.getQuestion().getId().equals(question.getId())) {
                            throw new RuntimeException("Invalid option id: " + answerRequest.getSelectedOptionId());
                        }
                        questionResponse.setSelectedOption(option);
                    }
                    break;
                    
                case RATING:
                    questionResponse.setRatingValue(answerRequest.getRatingValue());
                    break;
                    
                case YES_NO:
                    questionResponse.setBooleanResponse(answerRequest.getBooleanResponse());
                    break;
            }
            
            questionResponses.add(questionResponse);
        }
        
        // Check if required questions are answered
        for (SurveyQuestion question : questions) {
            if (Boolean.TRUE.equals(question.getRequired())) {
                boolean answered = questionResponses.stream()
                        .anyMatch(response -> response.getQuestion().getId().equals(question.getId()) &&
                                (response.getTextResponse() != null ||
                                        response.getSelectedOption() != null ||
                                        response.getRatingValue() != null ||
                                        response.getBooleanResponse() != null));
                
                if (!answered) {
                    throw new RuntimeException("Required question not answered: " + question.getQuestionText());
                }
            }
        }
        
        // Save question responses
        savedResponse.setQuestionResponses(questionResponses);
        
        return mapToSurveyResponseDto(savedResponse);
    }
    
    /**
     * Get all survey responses
     */
    public List<SurveyResponseDto> getAllSurveyResponses() {
        log.info("Getting all survey responses");
        
        List<SurveyResponse> responses = responseRepository.findAll();
        return responses.stream()
                .map(this::mapToSurveyResponseDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Get survey response by ID
     */
    public SurveyResponseDto getSurveyResponseById(Integer id) {
        log.info("Getting survey response with id: {}", id);
        
        SurveyResponse response = responseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Survey response not found with id: " + id));
        
        return mapToSurveyResponseDto(response);
    }
    
    /**
     * Get survey responses by customer ID
     */
    public List<SurveyResponseDto> getSurveyResponsesByCustomerId(Integer customerId) {
        log.info("Getting survey responses for customer: {}", customerId);
        
        List<SurveyResponse> responses = responseRepository.findByCustomerId(customerId);
        return responses.stream()
                .map(this::mapToSurveyResponseDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Get survey responses by survey ID
     */
    public List<SurveyResponseDto> getSurveyResponsesBySurveyId(Integer surveyId) {
        log.info("Getting survey responses for survey: {}", surveyId);
        
        List<SurveyResponse> responses = responseRepository.findBySurveyId(surveyId);
        return responses.stream()
                .map(this::mapToSurveyResponseDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Get survey responses by related entity
     */
    public List<SurveyResponseDto> getSurveyResponsesByRelatedEntity(Integer relatedEntityId, String relatedEntityType) {
        log.info("Getting survey responses for related entity: {}, type: {}", relatedEntityId, relatedEntityType);
        
        List<SurveyResponse> responses = responseRepository.findByRelatedEntityIdAndRelatedEntityType(relatedEntityId, relatedEntityType);
        return responses.stream()
                .map(this::mapToSurveyResponseDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Map entity to DTO
     */
    private SurveyResponseDto mapToSurveyResponseDto(SurveyResponse response) {
        List<QuestionResponseDto> questionResponseDtos = new ArrayList<>();
        
        if (response.getQuestionResponses() != null) {
            for (QuestionResponse questionResponse : response.getQuestionResponses()) {
                QuestionResponseDto responseDto = QuestionResponseDto.builder()
                        .id(questionResponse.getId())
                        .surveyResponseId(questionResponse.getSurveyResponse().getId())
                        .questionId(questionResponse.getQuestion().getId())
                        .questionText(questionResponse.getQuestion().getQuestionText())
                        .textResponse(questionResponse.getTextResponse())
                        .ratingValue(questionResponse.getRatingValue())
                        .booleanResponse(questionResponse.getBooleanResponse())
                        .build();
                
                if (questionResponse.getSelectedOption() != null) {
                    responseDto.setSelectedOptionId(questionResponse.getSelectedOption().getId());
                    responseDto.setSelectedOptionText(questionResponse.getSelectedOption().getOptionText());
                }
                
                questionResponseDtos.add(responseDto);
            }
        }
        
        return SurveyResponseDto.builder()
                .id(response.getId())
                .surveyId(response.getSurvey().getId())
                .surveyTitle(response.getSurvey().getTitle())
                .customerId(response.getCustomerId())
                .relatedEntityId(response.getRelatedEntityId())
                .relatedEntityType(response.getRelatedEntityType())
                .createdAt(response.getCreatedAt())
                .questionResponses(questionResponseDtos)
                .build();
    }
}