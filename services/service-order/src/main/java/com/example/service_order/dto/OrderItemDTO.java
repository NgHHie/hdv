package com.example.service_order.dto;



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
public class OrderItemDTO {
    private Long id;
    private Long orderId; 
    private Long productId;
    private String productName;  
    private String productCategory; 
    private BigDecimal price;
    private Integer quantity;
    private LocalDate warrantyExpiration; 
}