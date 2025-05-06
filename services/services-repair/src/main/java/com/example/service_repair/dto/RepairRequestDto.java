
package com.example.service_repair.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepairRequestDto {
    private Integer warrantyId;
    private Integer customerId;
    private Integer productId;
    private String issueDescription;
    private String imageUrls;
}


