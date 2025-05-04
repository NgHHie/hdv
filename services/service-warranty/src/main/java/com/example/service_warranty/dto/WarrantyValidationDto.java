package com.example.service_warranty.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarrantyValidationDto {
    private Long warrantyRequestId;
    private Boolean isValid;
    private String validationReason;
    private String validatedBy;
}