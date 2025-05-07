package com.example.service_customer.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "warranty_history")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarrantyHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "warranty_request_id", nullable = false)
    private WarrantyRequest warrantyRequest;
    
    @Column(name = "status", nullable = false)
    private String status;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "performed_by")
    private String performedBy;
    
    @Column(name = "performed_at")
    private LocalDateTime performedAt;
    
    @PrePersist
    protected void onCreate() {
        if (performedAt == null) {
            performedAt = LocalDateTime.now();
        }
    }
}