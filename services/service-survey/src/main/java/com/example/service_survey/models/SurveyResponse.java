package com.example.service_survey.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "survey_responses")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SurveyResponse {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;
    
    @Column(name = "customer_id", nullable = false)
    private Integer customerId;
    
    @Column(name = "related_entity_id")
    private Integer relatedEntityId;
    
    @Column(name = "related_entity_type")
    private String relatedEntityType;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @OneToMany(mappedBy = "surveyResponse", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionResponse> questionResponses;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}