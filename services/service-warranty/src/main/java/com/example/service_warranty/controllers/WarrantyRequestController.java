package com.example.service_warranty.controllers;

import com.example.service_warranty.dto.WarrantyRequestCreateDto;
import com.example.service_warranty.dto.WarrantyRequestDto;
import com.example.service_warranty.dto.WarrantyValidationDto;
import com.example.service_warranty.exception.WarrantyRequestNotFoundException;
import com.example.service_warranty.services.WarrantyRequestService;
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
           @RequestBody WarrantyRequestCreateDto requestDto) {
       log.info("REST request to create Warranty Request : {}", requestDto);
       WarrantyRequestDto createdRequest = warrantyRequestService.createWarrantyRequest(requestDto);
       return new ResponseEntity<>(createdRequest, HttpStatus.CREATED);
   }
   
   @GetMapping("/{id}")
   public ResponseEntity<WarrantyRequestDto> getWarrantyRequestById(@PathVariable Long id) {
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
           @PathVariable Long customerId) {
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
           @PathVariable Long id,
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
   
   @PostMapping("/{id}/reject")
   public ResponseEntity<WarrantyRequestDto> rejectWarrantyRequest(
           @PathVariable Long id,
           @RequestBody Map<String, String> requestBody,
           Principal principal) {
       log.info("REST request to reject Warranty Request : {}", id);
       
       String reason = requestBody.get("reason");
       String username = principal != null ? principal.getName() : "SYSTEM";
       
       try {
           WarrantyRequestDto updatedRequest = warrantyRequestService.rejectWarrantyRequest(id, reason, username);
           return ResponseEntity.ok(updatedRequest);
       } catch (WarrantyRequestNotFoundException e) {
           log.error("Warranty Request not found: {}", e.getMessage());
           return ResponseEntity.notFound().build();
       } catch (IllegalStateException e) {
           log.error("Cannot reject warranty request: {}", e.getMessage());
           return ResponseEntity.badRequest().build();
       }
   }
   
   @PostMapping("/{id}/approve")
   public ResponseEntity<WarrantyRequestDto> approveWarrantyRequest(
           @PathVariable Long id,
           @RequestBody Map<String, String> requestBody,
           Principal principal) {
       log.info("REST request to approve Warranty Request : {}", id);
       
       String notes = requestBody.get("notes");
       String username = principal != null ? principal.getName() : "SYSTEM";
       
       try {
           WarrantyRequestDto updatedRequest = warrantyRequestService.approveWarrantyRequest(id, notes, username);
           return ResponseEntity.ok(updatedRequest);
       } catch (WarrantyRequestNotFoundException e) {
           log.error("Warranty Request not found: {}", e.getMessage());
           return ResponseEntity.notFound().build();
       } catch (IllegalStateException e) {
           log.error("Cannot approve warranty request: {}", e.getMessage());
           return ResponseEntity.badRequest().build();
       }
   }
   
   @PostMapping("/{id}/receive")
   public ResponseEntity<WarrantyRequestDto> receiveWarrantyRequest(
           @PathVariable Long id,
           @RequestBody Map<String, String> requestBody,
           Principal principal) {
       log.info("REST request to mark Warranty Request as received : {}", id);
       
       String notes = requestBody.get("notes");
       String username = principal != null ? principal.getName() : "SYSTEM";
       
       try {
           WarrantyRequestDto updatedRequest = warrantyRequestService.receiveWarrantyRequest(id, notes, username);
           return ResponseEntity.ok(updatedRequest);
       } catch (WarrantyRequestNotFoundException e) {
           log.error("Warranty Request not found: {}", e.getMessage());
           return ResponseEntity.notFound().build();
       } catch (IllegalStateException e) {
           log.error("Cannot mark warranty request as received: {}", e.getMessage());
           return ResponseEntity.badRequest().build();
       }
   }
   
   @PostMapping("/{id}/forward-to-repair")
   public ResponseEntity<WarrantyRequestDto> forwardToRepair(
           @PathVariable Long id,
           @RequestBody Map<String, String> requestBody,
           Principal principal) {
       log.info("REST request to forward Warranty Request to repair : {}", id);
       
       String notes = requestBody.get("notes");
       String username = principal != null ? principal.getName() : "SYSTEM";
       
       try {
           WarrantyRequestDto updatedRequest = warrantyRequestService.forwardToRepair(id, notes, username);
           return ResponseEntity.ok(updatedRequest);
       } catch (WarrantyRequestNotFoundException e) {
           log.error("Warranty Request not found: {}", e.getMessage());
           return ResponseEntity.notFound().build();
       } catch (IllegalStateException e) {
           log.error("Cannot forward warranty request to repair: {}", e.getMessage());
           return ResponseEntity.badRequest().build();
       }
   }
}