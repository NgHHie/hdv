package com.example.service_survey.dto;

import com.example.service_survey.models.SurveyType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
public class CreateSurveyRequest {
    
    @NotNull(message = "Survey type is required")
    private SurveyType surveyType;
    
    @NotBlank(message = "Title is required")
    private String title;
    
    private String description;
    
    @NotEmpty(message = "At least one question is required")
    private List<CreateQuestionRequest> questions;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateQuestionRequest {
        @NotBlank(message = "Question text is required")
        private String questionText;
        
        @NotNull(message = "Question type is required")
        private String questionType;
        
        private Boolean required;
        
        private Integer displayOrder;
        
        private List<CreateOptionRequest> options;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateOptionRequest {
        @NotBlank(message = "Option text is required")
        private String optionText;
        
        private Integer displayOrder;
    }
}