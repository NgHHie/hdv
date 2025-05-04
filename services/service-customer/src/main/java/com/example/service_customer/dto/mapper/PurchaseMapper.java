package com.example.service_customer.dto.mapper;



import com.example.service_customer.dto.PurchaseDTO;
import com.example.service_customer.model.Purchase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PurchaseMapper {
    
    private final PurchaseItemMapper purchaseItemMapper;
    
    @Autowired
    public PurchaseMapper(PurchaseItemMapper purchaseItemMapper) {
        this.purchaseItemMapper = purchaseItemMapper;
    }
    
  
    public PurchaseDTO toDTO(Purchase purchase) {
        if (purchase == null) {
            return null;
        }

        PurchaseDTO dto = PurchaseDTO.builder()
                .id(purchase.getId())
                .purchaseDate(purchase.getPurchaseDate().toString())
                .invoiceNumber(purchase.getInvoiceNumber())
                .totalAmount(purchase.getTotalAmount().doubleValue())
                .paymentMethod(purchase.getPaymentMethod())
                .build();
        
        if (purchase.getCustomer() != null) {
            dto.setCustomerId(purchase.getCustomer().getId());
            dto.setCustomerName(purchase.getCustomer().getFirstName() + " " + 
                    purchase.getCustomer().getLastName());
        }
        
        if (purchase.getItems() != null) {
            dto.setItems(purchaseItemMapper.toDTOList(purchase.getItems()));
        }
        
        return dto;
    }

    
    public List<PurchaseDTO> toDTOList(List<Purchase> purchases) {
        if (purchases == null) {
            return null;
        }
        
        return purchases.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

  
    public Purchase toEntity(PurchaseDTO dto) {
        if (dto == null) {
            return null;
        }
        
        LocalDateTime purchaseDate = null;
        if (dto.getPurchaseDate() != null) {
            try {
                purchaseDate = LocalDateTime.parse(dto.getPurchaseDate());
            } catch (Exception e) {
                
            }
        }
        
        return Purchase.builder()
                .id(dto.getId())
                .purchaseDate(purchaseDate)
                .invoiceNumber(dto.getInvoiceNumber())
                .totalAmount(dto.getTotalAmount() != null ? 
                        java.math.BigDecimal.valueOf(dto.getTotalAmount()) : null)
                .paymentMethod(dto.getPaymentMethod())
                .createdAt(LocalDateTime.now())
                .build();
    }
}