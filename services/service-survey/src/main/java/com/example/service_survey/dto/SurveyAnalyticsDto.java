package com.example.service_survey.dto;

import com.example.service_survey.models.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SurveyAnalyticsDto {
    private Long surveyId;
    private String surveyTitle;
    private Long totalResponses;
    private Double completionRate;
    private Map<String, Long> responsesByDate;
    private List<QuestionAnalytics> questionAnalytics;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionAnalytics {
        private Long questionId;
        private String questionText;
        private QuestionType questionType;
        private Long totalAnswers;
        private List<OptionAnalytics> optionAnalytics;
        private Double averageRating;
        private Map<String, Long> textResponseFrequency;
        private Map<Boolean, Long> booleanResponseFrequency;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OptionAnalytics {
        private Long optionId;
        private String optionText;
        private Long count;
        private Double percentage;
    }
}