package com.example.service_survey.services;

import com.example.service_survey.dto.SurveyAnalyticsDto;
import com.example.service_survey.models.*;
import com.example.service_survey.repositories.SurveyQuestionRepository;
import com.example.service_survey.repositories.SurveyRepository;
import com.example.service_survey.repositories.SurveyResponseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SurveyAnalyticsService {
    
    private final SurveyRepository surveyRepository;
    private final SurveyResponseRepository surveyResponseRepository;
    private final SurveyQuestionRepository questionRepository;
    
    /**
     * Get survey analytics data
     */
    public SurveyAnalyticsDto getSurveyAnalytics(Long surveyId) {
        log.info("Getting survey analytics for survey: {}", surveyId);
        
        // Check if survey exists
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new RuntimeException("Survey not found with id: " + surveyId));
        
        // Get all responses for this survey
        List<SurveyResponse> responses = surveyResponseRepository.findBySurveyId(surveyId);
        
        // If there are no responses, return empty analytics
        if (responses.isEmpty()) {
            return SurveyAnalyticsDto.builder()
                    .surveyId(surveyId)
                    .surveyTitle(survey.getTitle())
                    .totalResponses(0L)
                    .questionAnalytics(new ArrayList<>())
                    .build();
        }
        
        // Get all questions for the survey
        List<SurveyQuestion> questions = questionRepository.findBySurveyIdOrderByDisplayOrderAsc(surveyId);
        
        // Calculate question analytics
        List<SurveyAnalyticsDto.QuestionAnalytics> questionAnalyticsList = new ArrayList<>();
        
        for (SurveyQuestion question : questions) {
            SurveyAnalyticsDto.QuestionAnalytics questionAnalytics = calculateQuestionAnalytics(question, responses);
            questionAnalyticsList.add(questionAnalytics);
        }
        
        return SurveyAnalyticsDto.builder()
                .surveyId(surveyId)
                .surveyTitle(survey.getTitle())
                .totalResponses((long) responses.size())
                .questionAnalytics(questionAnalyticsList)
                .completionRate(calculateCompletionRate(questions, responses))
                .responsesByDate(calculateResponsesByDate(responses))
                .build();
    }
    
    /**
     * Calculate analytics for a single question
     */
    private SurveyAnalyticsDto.QuestionAnalytics calculateQuestionAnalytics(SurveyQuestion question, List<SurveyResponse> responses) {
        List<SurveyAnalyticsDto.OptionAnalytics> optionAnalyticsList = new ArrayList<>();
        Long totalAnswers = 0L;
        Double averageRating = null;
        Map<String, Long> textResponseFrequency = null;
        Map<Boolean, Long> booleanResponseFrequency = null;
        
        // Get all question responses for this question
        List<QuestionResponse> questionResponses = new ArrayList<>();
        for (SurveyResponse response : responses) {
            if (response.getQuestionResponses() != null) {
                for (QuestionResponse questionResponse : response.getQuestionResponses()) {
                    if (questionResponse.getQuestion().getId().equals(question.getId())) {
                        questionResponses.add(questionResponse);
                        break;
                    }
                }
            }
        }
        
        totalAnswers = (long) questionResponses.size();
        
        switch (question.getQuestionType()) {
            case SINGLE_CHOICE:
            case MULTIPLE_CHOICE:
                // Calculate option frequency
                Map<Long, Long> optionFrequency = questionResponses.stream()
                        .filter(qr -> qr.getSelectedOption() != null)
                        .collect(Collectors.groupingBy(
                                qr -> qr.getSelectedOption().getId(),
                                Collectors.counting()
                        ));
                
                // Create option analytics
                if (question.getOptions() != null) {
                    for (QuestionOption option : question.getOptions()) {
                        long count = optionFrequency.getOrDefault(option.getId(), 0L);
                        double percentage = totalAnswers > 0 ? (count * 100.0) / totalAnswers : 0.0;
                        
                        SurveyAnalyticsDto.OptionAnalytics optionAnalytics = SurveyAnalyticsDto.OptionAnalytics.builder()
                                .optionId(option.getId())
                                .optionText(option.getOptionText())
                                .count(count)
                                .percentage(percentage)
                                .build();
                        
                        optionAnalyticsList.add(optionAnalytics);
                    }
                }
                break;
                
            case RATING:
                // Calculate average rating
                averageRating = questionResponses.stream()
                        .filter(qr -> qr.getRatingValue() != null)
                        .mapToDouble(qr -> qr.getRatingValue())
                        .average()
                        .orElse(0.0);
                break;
                
            case YES_NO:
                // Calculate yes/no frequency
                booleanResponseFrequency = questionResponses.stream()
                        .filter(qr -> qr.getBooleanResponse() != null)
                        .collect(Collectors.groupingBy(
                                QuestionResponse::getBooleanResponse,
                                Collectors.counting()
                        ));
                break;
                
            case TEXT:
            case TEXTAREA:
                // For text responses, calculate word frequency
                textResponseFrequency = new HashMap<>();
                for (QuestionResponse questionResponse : questionResponses) {
                    if (questionResponse.getTextResponse() != null && !questionResponse.getTextResponse().isEmpty()) {
                        String[] words = questionResponse.getTextResponse().toLowerCase().split("\\s+");
                        for (String word : words) {
                            if (word.length() > 3) { // Only count words longer than 3 characters
                                textResponseFrequency.put(word, textResponseFrequency.getOrDefault(word, 0L) + 1);
                            }
                        }
                    }
                }
                
                // Sort by frequency and take top 10
                textResponseFrequency = textResponseFrequency.entrySet().stream()
                        .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                        .limit(10)
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (e1, e2) -> e1,
                                LinkedHashMap::new
                        ));
                break;
        }
        
        return SurveyAnalyticsDto.QuestionAnalytics.builder()
                .questionId(question.getId())
                .questionText(question.getQuestionText())
                .questionType(question.getQuestionType())
                .totalAnswers(totalAnswers)
                .optionAnalytics(optionAnalyticsList)
                .averageRating(averageRating)
                .textResponseFrequency(textResponseFrequency)
                .booleanResponseFrequency(booleanResponseFrequency)
                .build();
    }
    
    /**
     * Calculate completion rate
     */
    private Double calculateCompletionRate(List<SurveyQuestion> questions, List<SurveyResponse> responses) {
        long requiredQuestionsCount = questions.stream()
                .filter(q -> Boolean.TRUE.equals(q.getRequired()))
                .count();
        
        if (requiredQuestionsCount == 0) {
            return 100.0; // No required questions
        }
        
        long totalRequiredAnswers = requiredQuestionsCount * responses.size();
        long answeredRequiredQuestions = 0;
        
        for (SurveyResponse response : responses) {
            if (response.getQuestionResponses() != null) {
                for (QuestionResponse questionResponse : response.getQuestionResponses()) {
                    if (Boolean.TRUE.equals(questionResponse.getQuestion().getRequired())) {
                        boolean isAnswered = questionResponse.getTextResponse() != null ||
                                questionResponse.getSelectedOption() != null ||
                                questionResponse.getRatingValue() != null ||
                                questionResponse.getBooleanResponse() != null;
                        
                        if (isAnswered) {
                            answeredRequiredQuestions++;
                        }
                    }
                }
            }
        }
        
        return (answeredRequiredQuestions * 100.0) / totalRequiredAnswers;
    }
    
    /**
     * Calculate responses by date
     */
    private Map<String, Long> calculateResponsesByDate(List<SurveyResponse> responses) {
        return responses.stream()
                .collect(Collectors.groupingBy(
                        response -> response.getCreatedAt().toLocalDate().toString(),
                        Collectors.counting()
                ))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }
    
    /**
     * Get response rate over time for a survey
     */
    public Map<String, Long> getResponseRateOverTime(Long surveyId, LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Getting response rate over time for survey: {}", surveyId);
        
        // Check if survey exists
        surveyRepository.findById(surveyId)
                .orElseThrow(() -> new RuntimeException("Survey not found with id: " + surveyId));
        
        // Get responses in date range
        List<SurveyResponse> responses = surveyResponseRepository.findByCreatedAtBetween(startDate, endDate)
                .stream()
                .filter(response -> response.getSurvey().getId().equals(surveyId))
                .collect(Collectors.toList());
        
        // Group by date
        return responses.stream()
                .collect(Collectors.groupingBy(
                        response -> response.getCreatedAt().toLocalDate().toString(),
                        Collectors.counting()
                ))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }
}