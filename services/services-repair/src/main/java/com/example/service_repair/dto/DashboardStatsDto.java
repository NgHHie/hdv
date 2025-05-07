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
    private Integer totalRepairRequests;
    private Integer pendingRepairRequests;
    private Integer inProgressRepairRequests;
    private Integer completedRepairRequests;
    private Integer cancelledRepairRequests;
    private Double averageRepairTime;
    private List<TechnicianPerformanceDto> technicianPerformance;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TechnicianPerformanceDto {
        private Integer technicianId;
        private String technicianName;
        private Integer completedRepairs;
        private Double averageRepairTime;
    }
}