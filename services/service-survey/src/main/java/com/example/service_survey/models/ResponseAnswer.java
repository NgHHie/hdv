package com.example.service_survey.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "response_answers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseAnswer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "response_id", nullable = false)
    private Response response;
    
    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;
    
    @Column(name = "answer_text", columnDefinition = "TEXT")
    private String answerText;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}