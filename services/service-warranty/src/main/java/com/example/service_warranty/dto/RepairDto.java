package com.example.service_warranty.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RepairDto {
    private Integer id;
    private String status;
    private Integer technicianId;
    private LocalDateTime createdAt;
    private LocalDateTime endDate;
}
