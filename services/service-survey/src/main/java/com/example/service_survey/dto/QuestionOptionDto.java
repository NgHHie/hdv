package com.example.service_survey.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionOptionDto {
    private Integer id;
    private Integer questionId;
    private String optionText;
    private Integer displayOrder;
}