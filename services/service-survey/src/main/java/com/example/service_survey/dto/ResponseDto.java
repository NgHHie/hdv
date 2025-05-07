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
public class ResponseDto {
    private Long id;
    private Long surveyId;
    private String surveyTitle;
    private Long customerId;
    private LocalDateTime createdAt;
    private List<ResponseAnswerDto> answers;
}