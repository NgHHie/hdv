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
public class WarrantyDetailDto {
    // Warranty information
    private Integer id;
    private String status;
    private LocalDateTime submissionDate;
    private LocalDate expirationDate;
    private String issueDescription;
    private List<String> imageUrls;
    private String validationNotes;
    
    // Customer information
    private Integer customerId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    
    // Product information
    private Integer productId;
    private String productName;
    private String serialNumber;
    private Float warrantyDuration;
    
    // Repair information
    private Integer repairId;
    private String repairStatus;
    private LocalDateTime repairCreatedAt;
    private LocalDateTime repairEndDate;

    // Technician information
    private Integer technicianId;
    private String technicianName;


}

