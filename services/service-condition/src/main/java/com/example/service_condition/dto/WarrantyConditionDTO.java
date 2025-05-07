package com.example.service_condition.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarrantyConditionDTO {
    private Integer id;
    private String name;
    private String description;
    private Boolean isActive;
}