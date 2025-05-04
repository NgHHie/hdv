package com.example.service_customer.dto.mapper;



import com.example.service_customer.dto.PurchaseItemDTO;
import com.example.service_customer.model.PurchaseItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PurchaseItemMapper {
    
   
    public PurchaseItemDTO toDTO(PurchaseItem item) {
        if (item == null) {
            return null;
        }

        PurchaseItemDTO dto = PurchaseItemDTO.builder()
                .id(item.getId())
                .productId(item.getProductId())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .warrantyExpirationDate(item.getWarrantyExpirationDate())
                .build();
        
        if (item.getPurchase() != null) {
            dto.setPurchaseId(item.getPurchase().getId());
            
            if (item.getPurchase().getCustomer() != null) {
                dto.setCustomerId(item.getPurchase().getCustomer().getId());
                dto.setCustomerName(
                        item.getPurchase().getCustomer().getFirstName() + " " + 
                        item.getPurchase().getCustomer().getLastName());
            }
        }
        
     
        
        return dto;
    }

    
    public List<PurchaseItemDTO> toDTOList(List<PurchaseItem> items) {
        if (items == null) {
            return null;
        }
        
        return items.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    
    public PurchaseItem toEntity(PurchaseItemDTO dto) {
        if (dto == null) {
            return null;
        }
        
        return PurchaseItem.builder()
                .id(dto.getId())
                .productId(dto.getProductId())
                .quantity(dto.getQuantity())
                .unitPrice(dto.getUnitPrice())
                .warrantyExpirationDate(dto.getWarrantyExpirationDate())
                .build();
    }
}