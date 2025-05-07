package com.example.service_warranty.controllers;

import com.example.service_warranty.dto.WarrantyRequestCreateDto;
import com.example.service_warranty.dto.WarrantyRequestDto;
import com.example.service_warranty.dto.WarrantyValidationDto;
import com.example.service_warranty.exception.WarrantyRequestNotFoundException;
import com.example.service_warranty.services.WarrantyRequestService;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/warranty/requests")
@RequiredArgsConstructor
@Slf4j
public class WarrantyRequestController {
   
   private final WarrantyRequestService warrantyRequestService;
   
   @PostMapping
   public ResponseEntity<WarrantyRequestDto> createWarrantyRequest(
           @RequestBody WarrantyRequestCreateDto requestDto) throws JsonProcessingException {
       log.info("REST request to create Warranty Request : {}", requestDto);
       WarrantyRequestDto createdRequest = warrantyRequestService.createWarrantyRequest(requestDto);
       return new ResponseEntity<>(createdRequest, HttpStatus.CREATED);
   }
   
   @GetMapping("/{id}")
   public ResponseEntity<WarrantyRequestDto> getWarrantyRequestById(@PathVariable Integer id) {
       log.info("REST request to get Warranty Request : {}", id);
       try {
           WarrantyRequestDto request = warrantyRequestService.getWarrantyRequestById(id);
           return ResponseEntity.ok(request);
       } catch (WarrantyRequestNotFoundException e) {
           log.error("Warranty Request not found: {}", e.getMessage());
           return ResponseEntity.notFound().build();
       }
   }
   
   @GetMapping("/customer/{customerId}")
   public ResponseEntity<List<WarrantyRequestDto>> getWarrantyRequestsByCustomerId(
           @PathVariable Integer customerId) {
       log.info("REST request to get Warranty Requests for customer : {}", customerId);
       List<WarrantyRequestDto> requests = warrantyRequestService.getWarrantyRequestsByCustomerId(customerId);
       return ResponseEntity.ok(requests);
   }
   
   @GetMapping("/status/{status}")
   public ResponseEntity<List<WarrantyRequestDto>> getWarrantyRequestsByStatus(
           @PathVariable String status) {
       log.info("REST request to get Warranty Requests with status : {}", status);
       List<WarrantyRequestDto> requests = warrantyRequestService.getWarrantyRequestsByStatus(status);
       return ResponseEntity.ok(requests);
   }
   
   @PostMapping("/{id}/validate")
   public ResponseEntity<WarrantyRequestDto> validateWarrantyRequest(
           @PathVariable Integer id,
           @RequestBody WarrantyValidationDto validationDto,
           Principal principal) {
       log.info("REST request to validate Warranty Request : {}", id);
       
       String username = principal != null ? principal.getName() : "SYSTEM";
       validationDto.setValidatedBy(username);
       
       try {
           WarrantyRequestDto updatedRequest = warrantyRequestService.validateWarrantyRequest(id, validationDto);
           return ResponseEntity.ok(updatedRequest);
       } catch (WarrantyRequestNotFoundException e) {
           log.error("Warranty Request not found: {}", e.getMessage());
           return ResponseEntity.notFound().build();
       } catch (IllegalStateException e) {
           log.error("Cannot validate warranty request: {}", e.getMessage());
           return ResponseEntity.badRequest().build();
       }
   }
   
//    @PostMapping("/{id}/reject")
//    public ResponseEntity<WarrantyRequestDto> rejectWarrantyRequest(
//            @PathVariable Integer id,
//            @RequestBody Map<String, String> requestBody,
//            Principal principal) {
//        log.info("REST request to reject Warranty Request : {}", id);
       
//        String reason = requestBody.get("reason");
//        String username = principal != null ? principal.getName() : "SYSTEM";
       
//        try {
//            WarrantyRequestDto updatedRequest = warrantyRequestService.rejectWarrantyRequest(id, reason, username);
//            return ResponseEntity.ok(updatedRequest);
//        } catch (WarrantyRequestNotFoundException e) {
//            log.error("Warranty Request not found: {}", e.getMessage());
//            return ResponseEntity.notFound().build();
//        } catch (IllegalStateException e) {
//            log.error("Cannot reject warranty request: {}", e.getMessage());
//            return ResponseEntity.badRequest().build();
//        }
//    }
   
//    @PostMapping("/{id}/approve")
//    public ResponseEntity<WarrantyRequestDto> approveWarrantyRequest(
//            @PathVariable Integer id,
//            @RequestBody Map<String, String> requestBody,
//            Principal principal) {
//        log.info("REST request to approve Warranty Request : {}", id);
       
//        String notes = requestBody.get("notes");
//        String username = principal != null ? principal.getName() : "SYSTEM";
       
//        try {
//            WarrantyRequestDto updatedRequest = warrantyRequestService.approveWarrantyRequest(id, notes, username);
//            return ResponseEntity.ok(updatedRequest);
//        } catch (WarrantyRequestNotFoundException e) {
//            log.error("Warranty Request not found: {}", e.getMessage());
//            return ResponseEntity.notFound().build();
//        } catch (IllegalStateException e) {
//            log.error("Cannot approve warranty request: {}", e.getMessage());
//            return ResponseEntity.badRequest().build();
//        }
//    }
   
    @PostMapping("/{id}/receive-and-forward")
    public ResponseEntity<WarrantyRequestDto> receiveAndForwardToRepair(
            @PathVariable Integer id,
            @RequestBody Map<String, String> requestBody,
            Principal principal) {
        log.info("REST request to receive product and forward to repair service: {}", id);
        
        String notes = requestBody.get("notes");
        String username = principal != null ? principal.getName() : "SYSTEM";
        
        try {
            // First mark the warranty request as received
            warrantyRequestService.receiveWarrantyRequest(id, notes, username);
            
            // Then forward it to repair service
            WarrantyRequestDto forwardedRequest = warrantyRequestService.forwardToRepair(id, 
                    notes != null ? notes + " (Auto-forwarded to repair)" : "Auto-forwarded to repair", 
                    username);
            
            return ResponseEntity.ok(forwardedRequest);
        } catch (WarrantyRequestNotFoundException e) {
            log.error("Warranty Request not found: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            log.error("Process failed: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/update-repair-status")
    public ResponseEntity<WarrantyRequestDto> updateRepairStatus(
            @PathVariable Integer id,
            @RequestBody Map<String, String> requestBody,
            Principal principal) {
        log.info("REST request to update repair status for Warranty Request: {}", id);
        
        String status = requestBody.get("status");
        String notes = requestBody.get("notes");
        String username = principal != null ? principal.getName() : "SYSTEM";
        
        try {
            WarrantyRequestDto updatedRequest = warrantyRequestService.updateRepairStatus(id, notes, username);
            return ResponseEntity.ok(updatedRequest);
        } catch (WarrantyRequestNotFoundException e) {
            log.error("Warranty Request not found: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            log.error("Cannot update repair status: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}