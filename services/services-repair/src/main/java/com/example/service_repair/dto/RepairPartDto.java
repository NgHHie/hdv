package com.example.service_repair.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepairPartDto {
    private Integer id;
    private String partName;
    private String partNumber;
    private String description;
    private Boolean isWarrantyReplacement;
}