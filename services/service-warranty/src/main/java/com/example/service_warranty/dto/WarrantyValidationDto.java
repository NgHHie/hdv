// Updated validation DTO
package com.example.service_warranty.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarrantyValidationDto {
    private Integer warrantyRequestId;
    private Boolean isValid;
    private String validationReason;
    private String validatedBy;
    private List<ConditionResultDto> conditionResults;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConditionResultDto {
        private Integer conditionId;
        private Boolean passed;
        private String notes;
    }
}