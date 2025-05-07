package com.example.service_repair.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Table(name = "repair_actions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepairAction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "repair_id")
    private RepairRequest repairRequest;
    
    private String actionType;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    private Integer performedBy;
    
    private LocalDateTime performedAt;
}