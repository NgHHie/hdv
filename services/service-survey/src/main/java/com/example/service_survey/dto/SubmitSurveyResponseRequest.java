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
    
    @NotNull(message = "ID khảo sát là bắt buộc")
    private Long surveyId;
    
    @NotNull(message = "ID khách hàng là bắt buộc")
    private Long customerId;
    
    @NotNull(message = "Cần có câu trả lời cho khảo sát")
    private List<QuestionAnswerRequest> answers;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionAnswerRequest {
        
        @NotNull(message = "ID câu hỏi là bắt buộc")
        private Long questionId;
        
        private String answerText;
    }
}
