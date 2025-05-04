package com.example.service_customer.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseItemDTO {
    private Long id;
    private Long purchaseId;
    private Integer customerId;
    private Long productId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private LocalDate warrantyExpirationDate;
    private String productName; 
    private String customerName; 
}