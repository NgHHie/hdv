package com.example.service_warranty.services;

import com.example.service_warranty.client.ProductServiceClient;
import com.example.service_warranty.dto.*;
import com.example.service_warranty.exception.WarrantyClaimNotFoundException;
import com.example.service_warranty.exception.WarrantyNotFoundException;
import com.example.service_warranty.models.Warranty;
import com.example.service_warranty.models.WarrantyClaim;
import com.example.service_warranty.repositories.WarrantyClaimRepository;
import com.example.service_warranty.repositories.WarrantyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WarrantyService {
    
    private final WarrantyRepository warrantyRepository;
    private final WarrantyClaimRepository warrantyClaimRepository;
    private final ProductServiceClient productServiceClient;
    
    /**
     * Create a new warranty
     */
    @Transactional
    public WarrantyDto createWarranty(WarrantyDto warrantyDto) {
        log.info("Creating new warranty for product: {}, customer: {}", 
                warrantyDto.getProductId(), warrantyDto.getCustomerId());
        
        Warranty warranty = Warranty.builder()
                .productId(warrantyDto.getProductId())
                .customerId(warrantyDto.getCustomerId())
                .purchaseDate(warrantyDto.getPurchaseDate())
                .expirationDate(warrantyDto.getExpirationDate())
                .isActive(warrantyDto.getIsActive() != null ? warrantyDto.getIsActive() : true)
                .build();
        
        Warranty savedWarranty = warrantyRepository.save(warranty);
        return mapToWarrantyDto(savedWarranty);


        
    }
    
    /**
     * Get warranty by ID
     */
    public WarrantyDto getWarrantyById(Integer id) {
        log.info("Getting warranty with id: {}", id);
        
        Warranty warranty = warrantyRepository.findById(id)
                .orElseThrow(() -> new WarrantyNotFoundException("Warranty not found with id: " + id));
        
        return mapToWarrantyDto(warranty);
    }
    
    /**
     * Get warranties by customer ID
     */
    public List<WarrantyDto> getWarrantiesByCustomerId(Integer customerId) {
        log.info("Getting warranties for customer: {}", customerId);
        
        List<Warranty> warranties = warrantyRepository.findByCustomerId(customerId);
        return warranties.stream()
                .map(this::mapToWarrantyDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Get warranties by product ID
     */
    public List<WarrantyDto> getWarrantiesByProductId(Integer productId) {
        log.info("Getting warranties for product: {}", productId);
        
        List<Warranty> warranties = warrantyRepository.findByProductId(productId);
        return warranties.stream()
                .map(this::mapToWarrantyDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Check warranty status for a product and customer
     */
    public WarrantyStatusResponse checkWarrantyStatus(Integer productId, Integer customerId) {
        log.info("Checking warranty status for product: {}, customer: {}", productId, customerId);
        
        Optional<Warranty> warrantyOpt = warrantyRepository.findByProductIdAndCustomerId(productId, customerId);
        
        if (warrantyOpt.isEmpty()) {
            log.info("No warranty found for product: {}, customer: {}", productId, customerId);
            return new WarrantyStatusResponse(false, null);
        }
        
        Warranty warranty = warrantyOpt.get();
        
        boolean isValid = warranty.getIsActive() && 
                warranty.getExpirationDate() != null && 
                !LocalDate.now().isAfter(warranty.getExpirationDate());
        
        String expirationDate = warranty.getExpirationDate() != null ? 
                warranty.getExpirationDate().toString() : null;
        
        return new WarrantyStatusResponse(isValid, expirationDate);
    }
    
    /**
     * Register a warranty claim
     */
    @Transactional
    public WarrantyClaimResponse registerWarrantyClaim(WarrantyClaimRequest request) {
        log.info("Registering warranty claim for product: {}, customer: {}, repair: {}", 
                request.getProductId(), request.getCustomerId(), request.getRepairId());
        
        // Check if warranty exists and is valid
        Optional<Warranty> warrantyOpt = warrantyRepository.findByProductIdAndCustomerId(
                request.getProductId(), request.getCustomerId());
        
        if (warrantyOpt.isEmpty()) {
            log.warn("No warranty found for product: {}, customer: {}", 
                    request.getProductId(), request.getCustomerId());
            return new WarrantyClaimResponse(false, null);
        }
        
        Warranty warranty = warrantyOpt.get();
        
        boolean isValid = warranty.getIsActive() && 
                warranty.getExpirationDate() != null && 
                !LocalDate.now().isAfter(warranty.getExpirationDate());
        
        if (!isValid) {
            log.warn("Warranty is not valid for product: {}, customer: {}", 
                    request.getProductId(), request.getCustomerId());
            return new WarrantyClaimResponse(false, null);
        }
        
        // Create warranty claim
        WarrantyClaim claim = WarrantyClaim.builder()
                .warrantyId(warranty.getId())
                .repairId(request.getRepairId())
                .claimDate(LocalDateTime.now())
                .status("REGISTERED")
                .notes("Warranty claim registered")
                .build();
        
        WarrantyClaim savedClaim = warrantyClaimRepository.save(claim);
        
        String claimId = "WC-" + UUID.randomUUID().toString().substring(0, 8);
        
        return new WarrantyClaimResponse(true, claimId);
    }
    
    /**
     * Get warranty claims by warranty ID
     */
    public List<WarrantyClaimDto> getWarrantyClaimsByWarrantyId(Integer warrantyId) {
        log.info("Getting warranty claims for warranty: {}", warrantyId);
        
        List<WarrantyClaim> claims = warrantyClaimRepository.findByWarrantyId(warrantyId);
        return claims.stream()
                .map(this::mapToWarrantyClaimDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Get warranty claims by repair ID
     */
    public List<WarrantyClaimDto> getWarrantyClaimsByRepairId(Integer repairId) {
        log.info("Getting warranty claims for repair: {}", repairId);
        
        List<WarrantyClaim> claims = warrantyClaimRepository.findByRepairId(repairId);
        return claims.stream()
                .map(this::mapToWarrantyClaimDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Update warranty claim status
     */
    @Transactional
    public WarrantyClaimDto updateWarrantyClaimStatus(Integer id, String status, String notes) {
        log.info("Updating warranty claim status to {} for claim: {}", status, id);
        
        WarrantyClaim claim = warrantyClaimRepository.findById(id)
                .orElseThrow(() -> new WarrantyClaimNotFoundException("Warranty claim not found with id: " + id));
        
        claim.setStatus(status);
        if (notes != null && !notes.isEmpty()) {
            claim.setNotes(notes);
        }
        
        WarrantyClaim updatedClaim = warrantyClaimRepository.save(claim);
        return mapToWarrantyClaimDto(updatedClaim);
    }
    
    /**
     * Map entity to DTO
     */
    private WarrantyDto mapToWarrantyDto(Warranty warranty) {
        return WarrantyDto.builder()
                .id(warranty.getId())
                .productId(warranty.getProductId())
                .customerId(warranty.getCustomerId())
                .purchaseDate(warranty.getPurchaseDate())
                .expirationDate(warranty.getExpirationDate())
                .isActive(warranty.getIsActive())
                .build();
    }
    
    /**
     * Map entity to DTO
     */
    private WarrantyClaimDto mapToWarrantyClaimDto(WarrantyClaim claim) {
        return WarrantyClaimDto.builder()
                .id(claim.getId())
                .warrantyId(claim.getWarrantyId())
                .repairId(claim.getRepairId())
                .claimDate(claim.getClaimDate())
                .status(claim.getStatus())
                .notes(claim.getNotes())
                .build();
    }
}