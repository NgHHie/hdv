
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
    private Long warrantyId;
    private Long customerId;
    private Long productId;
    private String issueDescription;
    private String imageUrls;
}


