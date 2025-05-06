package com.example.service_warranty.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarrantyRequestDto {
    private Integer id;
    private Integer customerId;
    private String customerName;
    private Integer productId;
    private String productName;
    private String serialNumber;
    private String issueDescription;
    private List<String> imageUrls;
    private String status;
    private LocalDateTime submissionDate;
    private LocalDate expirationDate;
    private String validationNotes;
    private Integer repairId;
}