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
    private Integer id;
    private Integer warrantyId;
    private Integer repairId;
    private LocalDateTime claimDate;
    private String status;
    private String notes;
}