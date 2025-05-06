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
    private Integer id;
    private Integer surveyResponseId;
    private Integer questionId;
    private String questionText;
    private String textResponse;
    private Integer selectedOptionId;
    private String selectedOptionText;
    private Integer ratingValue;
    private Boolean booleanResponse;
}