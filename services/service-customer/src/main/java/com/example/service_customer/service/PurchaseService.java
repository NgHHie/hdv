package com.example.service_customer.service;



import com.example.service_customer.dto.PurchaseItemDTO;
import com.example.service_customer.model.PurchaseItem;
import com.example.service_customer.repository.PurchaseItemRepository;
import com.example.service_customer.repository.PurchaseRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final PurchaseItemRepository purchaseItemRepository;

    /**
     * Lấy tất cả các sản phẩm đã mua có bảo hành (warranty_expiration_date không null)
     */
    public List<PurchaseItemDTO> getAllPurchaseItemsWithWarranty() {
        log.info("Request to get all purchase items with warranty");
        return purchaseItemRepository.findAllWithNotNullWarrantyExpirationDate()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lấy các sản phẩm có bảo hành theo purchase ID
     */
    public List<PurchaseItemDTO> getPurchaseItemsWithWarrantyByPurchaseId(Long purchaseId) {
        log.info("Request to get purchase items with warranty by purchase ID: {}", purchaseId);
        return purchaseItemRepository.findByPurchaseIdAndWarrantyExpirationDateNotNull(purchaseId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lấy các sản phẩm có bảo hành theo customer ID
     */
    public List<PurchaseItemDTO> getPurchaseItemsWithWarrantyByCustomerId(Integer customerId) {
        log.info("Request to get purchase items with warranty by customer ID: {}", customerId);
        return purchaseItemRepository.findByPurchaseCustomerIdAndWarrantyExpirationDateNotNull(customerId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Chuyển đổi PurchaseItem thành PurchaseItemDTO
     */
    private PurchaseItemDTO convertToDTO(PurchaseItem item) {
        return PurchaseItemDTO.builder()
                .id(item.getId())
                .purchaseId(item.getPurchase().getId())
                .customerId(item.getPurchase().getCustomer().getId())
                .productId(item.getProductId())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .warrantyExpirationDate(item.getWarrantyExpirationDate())
                .customerName(item.getPurchase().getCustomer().getFirstName() + " " + item.getPurchase().getCustomer().getLastName())
                .build();
    }
}