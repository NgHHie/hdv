package com.example.service_condition.controller;

import com.example.service_condition.dto.WarrantyConditionDTO;
import com.example.service_condition.dto.WarrantyConditionResultDTO;
import com.example.service_condition.dto.WarrantyValidationDTO;
import com.example.service_condition.service.WarrantyConditionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/conditions")
@RequiredArgsConstructor
@Slf4j
public class WarrantyConditionController {
    
    private final WarrantyConditionService conditionService;
    
    @GetMapping
    public ResponseEntity<List<WarrantyConditionDTO>> getAllConditions() {
        log.info("REST request to get all warranty conditions");
        return ResponseEntity.ok(conditionService.getAllActiveConditions());
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<WarrantyConditionDTO>> getAllActiveConditions() {
        log.info("REST request to get all active warranty conditions");
        return ResponseEntity.ok(conditionService.getAllActiveConditions());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<WarrantyConditionDTO> getConditionById(@PathVariable Integer id) {
        log.info("REST request to get warranty condition by id: {}", id);
        try {
            return ResponseEntity.ok(conditionService.getConditionById(id));
        } catch (Exception e) {
            log.error("Warranty condition not found: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<WarrantyConditionDTO> createCondition(
            @RequestBody WarrantyConditionDTO conditionDTO) {
        log.info("REST request to create warranty condition: {}", conditionDTO.getName());
        return new ResponseEntity<>(conditionService.createCondition(conditionDTO), HttpStatus.CREATED);
    }
    
    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateWarrantyConditions(
            @RequestBody WarrantyValidationDTO validationDTO) {
        log.info("REST request to validate warranty conditions for request: {}", 
                validationDTO.getWarrantyRequestId());
        return ResponseEntity.ok(conditionService.validateWarrantyRequest(validationDTO));
    }
    
    @GetMapping("/results/{warrantyRequestId}")
    public ResponseEntity<List<WarrantyConditionResultDTO>> getResultsByWarrantyRequest(
            @PathVariable Integer warrantyRequestId) {
        log.info("REST request to get condition results for warranty request: {}", warrantyRequestId);
        return ResponseEntity.ok(conditionService.getResultsByWarrantyRequestId(warrantyRequestId));
    }
}