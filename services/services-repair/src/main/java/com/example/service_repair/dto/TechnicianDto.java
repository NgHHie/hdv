package com.example.service_repair.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TechnicianDto {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String specialization;
    private Boolean isActive;
}