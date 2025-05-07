package com.example.service_survey.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseAnswerDto {
    private Long id;
    private Long responseId;
    private Long questionId;
    private String questionText;
    private String answerText;
}
