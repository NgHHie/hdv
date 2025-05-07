package com.example.service_survey.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "question_options")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionOption {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private SurveyQuestion question;
    
    @Column(name = "option_text", nullable = false)
    private String optionText;
    
    @Column(name = "display_order")
    private Integer displayOrder;
}