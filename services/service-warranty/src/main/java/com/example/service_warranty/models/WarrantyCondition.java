// New model for warranty conditions
package com.example.service_warranty.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "warranty_conditions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarrantyCondition {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column
    private Boolean isActive;
}

