package com.example.service_repair.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDto {
    private Long totalRepairRequests;
    private Long pendingRepairRequests;
    private Long inProgressRepairRequests;
    private Long completedRepairRequests;
    private Long cancelledRepairRequests;
    private Double averageRepairTime;
    private List<TechnicianPerformanceDto> technicianPerformance;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TechnicianPerformanceDto {
        private Long technicianId;
        private String technicianName;
        private Long completedRepairs;
        private Double averageRepairTime;
    }
}