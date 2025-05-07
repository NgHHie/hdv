package com.example.service_warranty.dto;

import java.time.LocalDate;
import java.util.List;

import org.springframework.cglib.core.Local;

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
    private String status;
    private String validationNotes;
    private LocalDate expirationDate;
}