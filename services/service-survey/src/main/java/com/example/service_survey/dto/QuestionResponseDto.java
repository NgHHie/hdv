package com.example.service_survey.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponseDto {
    private Long id;
    private Long surveyResponseId;
    private Long questionId;
    private String questionText;
    private String textResponse;
    private Long selectedOptionId;
    private String selectedOptionText;
    private Integer ratingValue;
    private Boolean booleanResponse;
}