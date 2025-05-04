package com.example.service_customer.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    private Integer productId;
    private Integer quantity;
    private BigDecimal unitPrice;



}