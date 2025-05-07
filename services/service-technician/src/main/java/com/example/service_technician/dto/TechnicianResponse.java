package com.example.service_technician.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TechnicianResponse {
    private Integer id;
    private String name;
    private String email;
    private String phone;
    private String specialization;
    private Integer yearsOfExperience;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}