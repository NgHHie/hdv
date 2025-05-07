package com.example.service_condition.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarrantyConditionResultDTO {
    private Integer id;
    private Integer warrantyRequestId;
    private Integer conditionId;
    private String conditionName;
    private Boolean passed;
    private String notes;
    private String evaluatedBy;
    private LocalDateTime evaluatedAt;
}