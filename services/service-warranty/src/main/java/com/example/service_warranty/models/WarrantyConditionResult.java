// New model for condition evaluation
package com.example.service_warranty.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "warranty_condition_results")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarrantyConditionResult {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "warranty_request_id")
    private WarrantyRequest warrantyRequest;
    
    @ManyToOne
    @JoinColumn(name = "condition_id")
    private WarrantyCondition condition;
    
    @Column(nullable = false)
    private Boolean passed;
    
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    @Column
    private String evaluatedBy;
    
    @Column
    private LocalDateTime evaluatedAt;
}