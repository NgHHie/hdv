package com.example.service_survey.dto;

import com.example.service_survey.models.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SurveyQuestionDto {
    private Integer id;
    private Integer surveyId;
    private String questionText;
    private QuestionType questionType;
    private Boolean required;
    private Integer displayOrder;
    private List<QuestionOptionDto> options;
}