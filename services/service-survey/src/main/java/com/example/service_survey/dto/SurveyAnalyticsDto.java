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
    private Integer surveyId;
    private String surveyTitle;
    private Integer totalResponses;
    private Double completionRate;
    private Map<String, Integer> responsesByDate;
    private List<QuestionAnalytics> questionAnalytics;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionAnalytics {
        private Integer questionId;
        private String questionText;
        private QuestionType questionType;
        private Integer totalAnswers;
        private List<OptionAnalytics> optionAnalytics;
        private Double averageRating;
        private Map<String, Integer> textResponseFrequency;
        private Map<Boolean, Integer> booleanResponseFrequency;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OptionAnalytics {
        private Integer optionId;
        private String optionText;
        private Integer count;
        private Double percentage;
    }
}