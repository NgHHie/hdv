package com.example.service_warranty.controllers;

import com.example.service_warranty.dto.*;
import com.example.service_warranty.exception.WarrantyClaimNotFoundException;
import com.example.service_warranty.exception.WarrantyNotFoundException;
import com.example.service_warranty.services.WarrantyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/warranty")
@RequiredArgsConstructor
@Slf4j
public class WarrantyController {
    
    private final WarrantyService warrantyService;
    
    @PostMapping
    public ResponseEntity<WarrantyDto> createWarranty(@RequestBody WarrantyDto warrantyDto) {
        log.info("REST request to create Warranty : {}", warrantyDto);
        WarrantyDto createdWarranty = warrantyService.createWarranty(warrantyDto);
        return new ResponseEntity<>(createdWarranty, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<WarrantyDto> getWarrantyById(@PathVariable Integer id) {
        log.info("REST request to get Warranty : {}", id);
        try {
            WarrantyDto warranty = warrantyService.getWarrantyById(id);
            return ResponseEntity.ok(warranty);
        } catch (WarrantyNotFoundException e) {
            log.error("Warranty not found: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<WarrantyDto>> getWarrantiesByCustomerId(@PathVariable Integer customerId) {
        log.info("REST request to get Warranties for customer : {}", customerId);
        List<WarrantyDto> warranties = warrantyService.getWarrantiesByCustomerId(customerId);
        return ResponseEntity.ok(warranties);
    }
    
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<WarrantyDto>> getWarrantiesByProductId(@PathVariable Integer productId) {
        log.info("REST request to get Warranties for product : {}", productId);
        List<WarrantyDto> warranties = warrantyService.getWarrantiesByProductId(productId);
        return ResponseEntity.ok(warranties);
    }
    
    @GetMapping("/check")
    public ResponseEntity<WarrantyStatusResponse> checkWarrantyStatus(
            @RequestParam Integer productId, 
            @RequestParam Integer customerId) {
        log.info("REST request to check warranty status for product: {}, customer: {}", 
                productId, customerId);
        WarrantyStatusResponse response = warrantyService.checkWarrantyStatus(productId, customerId);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/claim")
    public ResponseEntity<WarrantyClaimResponse> registerWarrantyClaim(
            @RequestBody WarrantyClaimRequest request) {
        log.info("REST request to register warranty claim : {}", request);
        WarrantyClaimResponse response = warrantyService.registerWarrantyClaim(request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/claims/warranty/{warrantyId}")
    public ResponseEntity<List<WarrantyClaimDto>> getWarrantyClaimsByWarrantyId(
            @PathVariable Integer warrantyId) {
        log.info("REST request to get Warranty Claims for warranty : {}", warrantyId);
        List<WarrantyClaimDto> claims = warrantyService.getWarrantyClaimsByWarrantyId(warrantyId);
        return ResponseEntity.ok(claims);
    }
    
    @GetMapping("/claims/repair/{repairId}")
    public ResponseEntity<List<WarrantyClaimDto>> getWarrantyClaimsByRepairId(
            @PathVariable Integer repairId) {
        log.info("REST request to get Warranty Claims for repair : {}", repairId);
        List<WarrantyClaimDto> claims = warrantyService.getWarrantyClaimsByRepairId(repairId);
        return ResponseEntity.ok(claims);
    }
    
    @PatchMapping("/claims/{id}/status")
    public ResponseEntity<WarrantyClaimDto> updateWarrantyClaimStatus(
            @PathVariable Integer id,
            @RequestBody Map<String, String> request) {
        log.info("REST request to update Warranty Claim status : {}", id);
        try {
            String status = request.get("status");
            String notes = request.get("notes");
            WarrantyClaimDto updatedClaim = warrantyService.updateWarrantyClaimStatus(id, status, notes);
            return ResponseEntity.ok(updatedClaim);
        } catch (WarrantyClaimNotFoundException e) {
            log.error("Warranty Claim not found: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}