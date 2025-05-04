package com.example.service_repair.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.example.service_repair.constants.RepairStatus;

@Entity
@Table(name = "repair_requests")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepairRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long warrantyId;
    private Long customerId;
    private Long productId;
    
    @Column(columnDefinition = "TEXT")
    private String issueDescription;
    
    @Column(columnDefinition = "TEXT")
    private String imageUrls;
    
    @Enumerated(EnumType.STRING)
    private RepairStatus status;
    
    @Column(columnDefinition = "TEXT")
    private String repairNotes;
    
    private Long technicianId;
    
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal repairCost;
    
    private Boolean withinWarranty;
    
    @OneToMany(mappedBy = "repairRequest", cascade = CascadeType.ALL)
    private List<RepairStatusHistory> statusHistory;
    
    @OneToMany(mappedBy = "repairRequest", cascade = CascadeType.ALL)
    private List<RepairPart> parts;
    @OneToMany(mappedBy = "repairRequest", cascade = CascadeType.ALL)
    private List<RepairAction> actions;
}




