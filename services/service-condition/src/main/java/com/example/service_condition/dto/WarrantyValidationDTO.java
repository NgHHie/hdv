package com.example.service_condition.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarrantyValidationDTO {
    private Integer warrantyRequestId;
    private Boolean isValid;
    private String validationReason;
    private String validatedBy;
    private List<ConditionResultDTO> conditionResults;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConditionResultDTO {
        private Integer conditionId;
        private Boolean passed;
        private String notes;
    }
}