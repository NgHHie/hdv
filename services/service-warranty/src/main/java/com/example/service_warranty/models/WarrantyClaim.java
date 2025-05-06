package com.example.service_warranty.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "warranty_claims")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarrantyClaim {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "warranty_id", nullable = false)
    private Long warrantyId;
    
    @Column(name = "repair_id", nullable = false)
    private Long repairId;
    
    @Column(name = "claim_date", nullable = false)
    private LocalDateTime claimDate;
    
    @Column(name = "status")
    private String status;
    
    @Column(name = "notes")
    private String notes;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (claimDate == null) {
            claimDate = LocalDateTime.now();
        }
        if (status == null) {
            status = "REGISTERED";
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}