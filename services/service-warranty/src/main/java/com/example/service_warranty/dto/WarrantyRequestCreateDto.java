package com.example.service_warranty.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarrantyRequestCreateDto {
    private Long customerId;
    private Long productId;
    private String serialNumber;
    private String issueDescription;
    private String imageUrls;
}