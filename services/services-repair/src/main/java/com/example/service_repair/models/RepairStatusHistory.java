package com.example.service_repair.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import com.example.service_repair.constants.RepairStatus;

@Entity
@Table(name = "repair_status_history")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepairStatusHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "repair_id")
    private RepairRequest repairRequest;
    
    @Enumerated(EnumType.STRING)
    private RepairStatus status;
    
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    private String createdBy;
    
    private LocalDateTime createdAt;
}

