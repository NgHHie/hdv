package com.example.service_customer.dto.mapper;



import com.example.service_customer.dto.PurchaseItemDTO;
import com.example.service_customer.model.PurchaseItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class PurchaseItemMapper {
    
   
    public static PurchaseItemDTO toDTO(PurchaseItem item) {
        if (item == null) {
            return null;
        }

        PurchaseItemDTO dto = PurchaseItemDTO.builder()
                .id(item.getId())
                .productId(item.getProductId())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .build();
        
        if (item.getPurchase() != null) {
            dto.setPurchaseId(item.getPurchase().getId());

            

        }

        
     
        
        return dto;
    }

    public  static  List<PurchaseItemDTO> toDTO(List<PurchaseItem> items) {
        List<PurchaseItemDTO> purchaseItemDTOS = new ArrayList<>();
        for(PurchaseItem item : items){
            purchaseItemDTOS.add(toDTO(item));
        }
        return  purchaseItemDTOS;
    }


    
    public static  PurchaseItem toEntity(PurchaseItemDTO dto) {
        if (dto == null) {
            return null;
        }
        
        return PurchaseItem.builder()
                .id(dto.getId())
                .productId(dto.getProductId())
                .quantity(dto.getQuantity())
                .unitPrice(dto.getUnitPrice())

                .build();
    }
}