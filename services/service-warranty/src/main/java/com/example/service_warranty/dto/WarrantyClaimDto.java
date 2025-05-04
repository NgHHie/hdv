package com.example.service_warranty.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarrantyClaimDto {
    private Long id;
    private Long warrantyId;
    private Long repairId;
    private LocalDateTime claimDate;
    private String status;
    private String notes;
}