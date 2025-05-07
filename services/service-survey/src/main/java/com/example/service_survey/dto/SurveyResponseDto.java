package com.example.service_survey.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SurveyResponseDto {
    private Integer id;
    private Integer surveyId;
    private String surveyTitle;
    private Integer customerId;
    private Integer relatedEntityId;
    private String relatedEntityType;
    private LocalDateTime createdAt;
    private List<QuestionResponseDto> questionResponses;
}