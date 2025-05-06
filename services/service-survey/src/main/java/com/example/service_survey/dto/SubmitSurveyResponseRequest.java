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
    private Long surveyId;
    
    @NotNull(message = "Customer ID is required")
    private Long customerId;
    
    private Long relatedEntityId;
    
    private String relatedEntityType;
    
    @NotNull(message = "Question responses are required")
    private List<QuestionAnswerRequest> questionResponses;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionAnswerRequest {
        
        @NotNull(message = "Question ID is required")
        private Long questionId;
        
        private String textResponse;
        
        private Long selectedOptionId;
        
        private Integer ratingValue;
        
        private Boolean booleanResponse;
    }
}