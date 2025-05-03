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
public class RepairActionDto {
    private Long id;
    private String actionType;
    private String description;
    private Long technicianId;
    private String technicianName;
    private LocalDateTime performedAt;
}