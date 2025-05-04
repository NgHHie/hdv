package com.example.service_warranty.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarrantyClaimRequest {
    private Long productId;
    private Long customerId;
    private Long repairId;
}