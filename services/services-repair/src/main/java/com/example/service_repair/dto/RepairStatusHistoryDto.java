package com.example.service_repair.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepairStatusHistoryDto {
    private Integer id;
    private String status;
    private String notes;
    private String createdBy;
    private LocalDateTime createdAt;
}