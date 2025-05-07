package com.example.service_survey.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
    
    @NotBlank(message = "Tiêu đề khảo sát là bắt buộc")
    private String title;
    
    private String description;
    
    @NotEmpty(message = "Cần ít nhất một câu hỏi cho khảo sát")
    private List<CreateQuestionRequest> questions;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateQuestionRequest {
        @NotBlank(message = "Nội dung câu hỏi là bắt buộc")
        private String questionText;
        
        private Boolean required;
        
        private Integer questionOrder;
    }
}
