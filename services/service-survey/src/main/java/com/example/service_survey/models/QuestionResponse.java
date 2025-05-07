package com.example.service_survey.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "question_responses")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponse {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "survey_response_id", nullable = false)
    private SurveyResponse surveyResponse;
    
    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private SurveyQuestion question;
    
    @Column(name = "text_response", columnDefinition = "TEXT")
    private String textResponse;
    
    @ManyToOne
    @JoinColumn(name = "option_id")
    private QuestionOption selectedOption;
    
    @Column(name = "rating_value")
    private Integer ratingValue;
    
    @Column(name = "boolean_response")
    private Boolean booleanResponse;
}