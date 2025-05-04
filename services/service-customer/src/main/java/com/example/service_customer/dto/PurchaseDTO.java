package com.example.service_customer.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseDTO {
    private Long id;
    private String purchaseDate;
    private String invoiceNumber;
    private Double totalAmount;
    private String paymentMethod;
    
    
    private Integer customerId;
    private String customerName;
    
    
    private List<PurchaseItemDTO> items;
}
