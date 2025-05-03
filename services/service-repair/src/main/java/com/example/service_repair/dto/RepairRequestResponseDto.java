package com.example.service_repair.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepairRequestResponseDto {
    private Long id;
    private Long warrantyId;
    private Long customerId;
    private Long productId;
    private String issueDescription;
    private String imageUrls;
    private String status;
    private String repairNotes;
    private Long technicianId;
    private String technicianName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BigDecimal repairCost;
    private Boolean withinWarranty;
    private List<RepairStatusHistoryDto> statusHistory;
    private List<RepairPartDto> parts;
    private List<RepairActionDto> actions;
}
