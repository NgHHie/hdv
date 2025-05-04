package com.example.service_warranty.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarrantyDto {
    private Long id;
    private Long productId;
    private Long customerId;
    private LocalDate purchaseDate;
    private LocalDate expirationDate;
    private Boolean isActive;
}