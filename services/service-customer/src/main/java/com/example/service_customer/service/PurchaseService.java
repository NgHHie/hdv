package com.example.service_customer.service;



import com.example.service_customer.dto.PurchaseDTO;
import com.example.service_customer.dto.PurchaseItemDTO;
import com.example.service_customer.dto.mapper.PurchaseMapper;
import com.example.service_customer.model.Purchase;
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

    public List<PurchaseDTO> getPurchasesByCustomerId(Integer customerId) {
        List<Purchase> purchases = purchaseRepository.findByCustomerId(customerId);
        return PurchaseMapper.toListDTO(purchases);
    }


    public PurchaseDTO getPurchase(Integer purchaseId) {
          Purchase purchase = purchaseRepository.findById(purchaseId).orElse(null);
          return PurchaseMapper.toDTO(purchase);
    }


}