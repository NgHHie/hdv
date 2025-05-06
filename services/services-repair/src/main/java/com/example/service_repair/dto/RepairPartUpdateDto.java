package com.example.service_repair.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepairPartUpdateDto {
    private Integer id;
    private String partName;
    private String partNumber;
    private Integer quantity;
    private BigDecimal unitPrice;
}
