package com.example.service_order.dto;



import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    private Long customerId;
    private String orderNumber;
    private BigDecimal totalAmount;
    private String status;
    private LocalDateTime orderDate;
    private String shippingAddress;
    private String paymentMethod;
    private List<OrderItemDTO> items = new ArrayList<>();
}