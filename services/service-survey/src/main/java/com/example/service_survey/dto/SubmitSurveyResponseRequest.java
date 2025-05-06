package com.example.service_survey.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmitSurveyResponseRequest {
    
    @NotNull(message = "Survey ID is required")
    private Integer surveyId;
    
    @NotNull(message = "Customer ID is required")
    private Integer customerId;
    
    private Integer relatedEntityId;
    
    private String relatedEntityType;
    
    @NotNull(message = "Question responses are required")
    private List<QuestionAnswerRequest> questionResponses;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionAnswerRequest {
        
        @NotNull(message = "Question ID is required")
        private Integer questionId;
        
        private String textResponse;
        
        private Integer selectedOptionId;
        
        private Integer ratingValue;
        
        private Boolean booleanResponse;
    }
}