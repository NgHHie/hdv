package com.example.service_warranty.controllers;

import com.example.service_warranty.dto.WarrantyRequestCreateDto;
import com.example.service_warranty.dto.WarrantyRequestDto;
import com.example.service_warranty.dto.WarrantyValidationDto;
import com.example.service_warranty.services.WarrantyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/warranty")
@RequiredArgsConstructor
@Slf4j
public class WarrantyController {
   
   private final WarrantyService warrantyService;
   
   @PostMapping("/requests")
   public ResponseEntity<WarrantyRequestDto> createWarrantyRequest(
           @RequestBody WarrantyRequestCreateDto requestDto) throws JsonProcessingException {
       log.info("REST request to orchestrate Warranty Request creation: {}", requestDto);
       WarrantyRequestDto createdRequest = warrantyService.createWarrantyRequest(requestDto);
       return new ResponseEntity<>(createdRequest, HttpStatus.CREATED);
   }
   
   @PostMapping("/requests/{id}/validate")
   public ResponseEntity<WarrantyRequestDto> validateWarrantyRequest(
           @PathVariable Integer id,
           @RequestBody WarrantyValidationDto validationDto,
           Principal principal) {
       log.info("REST request to orchestrate warranty validation: {}", id);
       
       String username = principal != null ? principal.getName() : "SYSTEM";
       validationDto.setValidatedBy(username);
       
       try {
           WarrantyRequestDto updatedRequest = warrantyService.completeValidationAndForward(id, validationDto);
           return ResponseEntity.ok(updatedRequest);
       } catch (Exception e) {
           log.error("Validation failed: {}", e.getMessage());
           return ResponseEntity.badRequest().build();
       }
   }
   
   @PostMapping("/requests/{id}/receive-and-forward")
   public ResponseEntity<WarrantyRequestDto> receiveAndForwardToRepair(
           @PathVariable Integer id,
           @RequestBody Map<String, String> requestBody,
           Principal principal) {
       log.info("REST request to orchestrate product receipt and repair: {}", id);
       
       String notes = requestBody.get("notes");
       String username = principal != null ? principal.getName() : "SYSTEM";
       
       try {
           WarrantyRequestDto forwardedRequest = warrantyService.receiveAndForwardToRepair(id, notes, username);
           return ResponseEntity.ok(forwardedRequest);
       } catch (Exception e) {
           log.error("Process failed: {}", e.getMessage());
           return ResponseEntity.badRequest().build();
       }
   }

   @PutMapping("/requests/{id}/repair-status")
   public ResponseEntity<WarrantyRequestDto> updateRepairStatus(
           @PathVariable Integer id,
           @RequestBody Map<String, String> requestBody,
           Principal principal) {
       log.info("REST request to orchestrate repair status update: {}", id);
       
       String status = requestBody.get("status");
       String notes = requestBody.get("notes");
       String username = principal != null ? principal.getName() : "SYSTEM";
       
       try {
           WarrantyRequestDto updatedRequest = warrantyService.updateRepairStatus(id, status, notes, username);
           return ResponseEntity.ok(updatedRequest);
       } catch (Exception e) {
           log.error("Cannot update repair status: {}", e.getMessage());
           return ResponseEntity.badRequest().build();
       }
   }
}