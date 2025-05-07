package com.example.service_customer.controllers;

import com.example.service_customer.dto.WarrantyHistoryDTO;
import com.example.service_customer.dto.WarrantyRequestDTO;
import com.example.service_customer.service.WarrantyRequestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/customers/warranty")
@RequiredArgsConstructor
@Slf4j
public class WarrantyController {
    
    private final WarrantyRequestService warrantyRequestService;
    
    @PostMapping("/requests")
    public ResponseEntity<WarrantyRequestDTO> createWarrantyRequest(
            @RequestBody WarrantyRequestDTO requestDto) throws JsonProcessingException {
        log.info("REST request to create Warranty Request : {}", requestDto);
        WarrantyRequestDTO createdRequest = warrantyRequestService.createWarrantyRequest(requestDto);
        return new ResponseEntity<>(createdRequest, HttpStatus.CREATED);
    }
    
    @GetMapping("/requests/{id}")
    public ResponseEntity<WarrantyRequestDTO> getWarrantyRequestById(@PathVariable Integer id) {
        log.info("REST request to get Warranty Request : {}", id);
        try {
            WarrantyRequestDTO request = warrantyRequestService.getWarrantyRequestById(id);
            return ResponseEntity.ok(request);
        } catch (Exception e) {
            log.error("Warranty Request not found: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/requests/customer/{customerId}")
    public ResponseEntity<List<WarrantyRequestDTO>> getWarrantyRequestsByCustomerId(
            @PathVariable Integer customerId) {
        log.info("REST request to get Warranty Requests for customer : {}", customerId);
        List<WarrantyRequestDTO> requests = warrantyRequestService.getWarrantyRequestsByCustomerId(customerId);
        return ResponseEntity.ok(requests);
    }
    
    @GetMapping("/requests/status/{status}")
    public ResponseEntity<List<WarrantyRequestDTO>> getWarrantyRequestsByStatus(
            @PathVariable String status) {
        log.info("REST request to get Warranty Requests with status : {}", status);
        List<WarrantyRequestDTO> requests = warrantyRequestService.getWarrantyRequestsByStatus(status);
        return ResponseEntity.ok(requests);
    }
    
    @PutMapping("/requests/{id}/status")
    public ResponseEntity<WarrantyRequestDTO> updateWarrantyRequestStatus(
            @PathVariable Integer id,
            @RequestBody Map<String, String> requestBody) {
        log.info("REST request to update Warranty Request status : {}", id);
        
        String status = requestBody.get("status");
        String notes = requestBody.get("notes");
        String performedBy = requestBody.get("performedBy");
        
        if (status == null) {
            return ResponseEntity.badRequest().build();
        }
        
        try {
            WarrantyRequestDTO updatedRequest = warrantyRequestService.updateWarrantyRequestStatus(
                    id, status, notes, performedBy);
            return ResponseEntity.ok(updatedRequest);
        } catch (Exception e) {
            log.error("Error updating warranty request: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/requests/{id}/repair")
    public ResponseEntity<WarrantyRequestDTO> updateRepairId(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> requestBody) {
        log.info("REST request to update Repair ID for warranty request : {}", id);
        
        Integer repairId = (Integer) requestBody.get("repairId");
        String performedBy = (String) requestBody.get("performedBy");
        
        if (repairId == null) {
            return ResponseEntity.badRequest().build();
        }
        
        try {
            WarrantyRequestDTO request = warrantyRequestService.getWarrantyRequestById(id);
            request.setRepairId(repairId);
            WarrantyRequestDTO updatedRequest = warrantyRequestService.updateWarrantyRequestStatus(
                    id, request.getStatus(), "Repair ID updated to: " + repairId, performedBy);
            return ResponseEntity.ok(updatedRequest);
        } catch (Exception e) {
            log.error("Error updating repair ID: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/requests/{id}/history")
    public ResponseEntity<List<WarrantyHistoryDTO>> getWarrantyHistory(@PathVariable Integer id) {
        log.info("REST request to get history for Warranty Request : {}", id);
        List<WarrantyHistoryDTO> history = warrantyRequestService.getWarrantyHistoryByRequestId(id);
        return ResponseEntity.ok(history);
    }
}