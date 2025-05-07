package com.example.service_warranty.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "warranty_requests")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarrantyRequest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "customer_id", nullable = false)
    private Integer customerId;

    @Column(name = "product_id", nullable = false)
    private Integer productId;
    
    @Column(name = "serial_number")
    private String serialNumber;
    
    @Column(name = "issue_description", columnDefinition = "TEXT")
    private String issueDescription;
    
    @Column(name = "image_urls", columnDefinition = "TEXT")
    private String imageUrls;
    
    @Column(name = "status", nullable = false)
    private String status;
    
    @Column(name = "submission_date")
    private LocalDateTime submissionDate;
    
    @Column(name = "expiration_date")
    private LocalDate expirationDate;
    
    @Column(name = "validation_notes", columnDefinition = "TEXT")
    private String validationNotes;
    
    @Column(name = "repair_id")
    private Integer repairId;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (submissionDate == null) {
            submissionDate = LocalDateTime.now();
        }
        if (status == null) {
            status = "PENDING";
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}