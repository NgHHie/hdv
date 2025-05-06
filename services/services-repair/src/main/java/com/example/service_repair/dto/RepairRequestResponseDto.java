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
    private Integer id;
    private Integer warrantyId;
    private Integer customerId;
    private Integer productId;
    private String issueDescription;
    private String imageUrls;
    private String status;
    private String repairNotes;
    private Integer technicianId;
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
