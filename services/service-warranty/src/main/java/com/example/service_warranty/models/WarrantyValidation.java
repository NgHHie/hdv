package com.example.service_warranty.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "warranty_validations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarrantyValidation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "warranty_request_id", nullable = false)
    private Long warrantyRequestId;
    
    @Column(name = "is_valid", nullable = false)
    private Boolean isValid;
    
    @Column(name = "validation_reason", columnDefinition = "TEXT")
    private String validationReason;
    
    @Column(name = "validated_by")
    private String validatedBy;
    
    @Column(name = "validated_at")
    private LocalDateTime validatedAt;
    
    @PrePersist
    protected void onCreate() {
        if (validatedAt == null) {
            validatedAt = LocalDateTime.now();
        }
    }
}