package com.example.service_warranty.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarrantyRequestCreateDto {
    private Integer customerId;
    private Integer productId;
    private String serialNumber;
    private String issueDescription;
    private List<String> imageUrls;
}