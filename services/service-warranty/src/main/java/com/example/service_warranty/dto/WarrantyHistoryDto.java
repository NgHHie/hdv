package com.example.service_warranty.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarrantyHistoryDto {
    private Integer id;
    private Integer warrantyRequestId;
    private String status;
    private String notes;
    private String performedBy;
    private LocalDateTime performedAt;
}