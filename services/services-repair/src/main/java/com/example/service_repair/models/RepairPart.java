package com.example.service_repair.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "repair_parts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepairPart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "repair_id")
    private RepairRequest repairRequest;
    
    private String partName;
    private String partNumber;
    private String description;
    private Boolean isWarrantyReplacement;
}