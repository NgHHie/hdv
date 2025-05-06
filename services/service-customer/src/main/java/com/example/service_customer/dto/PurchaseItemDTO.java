package com.example.service_customer.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseItemDTO {
    private Integer id;
    private Integer purchaseId;
    private String productName;
    private Integer productId;
    private BigDecimal unitPrice;
}