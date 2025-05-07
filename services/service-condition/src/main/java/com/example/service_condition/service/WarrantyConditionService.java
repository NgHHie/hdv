package com.example.service_condition.service;

import com.example.service_condition.dto.WarrantyConditionDTO;
import com.example.service_condition.dto.WarrantyConditionResultDTO;
import com.example.service_condition.dto.WarrantyValidationDTO;
import com.example.service_condition.model.WarrantyCondition;
import com.example.service_condition.model.WarrantyConditionResult;
import com.example.service_condition.repository.WarrantyConditionRepository;
import com.example.service_condition.repository.WarrantyConditionResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WarrantyConditionService {
    
    private final WarrantyConditionRepository conditionRepository;
    private final WarrantyConditionResultRepository resultRepository;
    
    public List<WarrantyConditionDTO> getAllActiveConditions() {
        log.info("Getting all active warranty conditions");
        return conditionRepository.findByIsActiveTrue().stream()
                .map(this::mapToConditionDTO)
                .collect(Collectors.toList());
    }
    
    public WarrantyConditionDTO getConditionById(Integer id) {
        log.info("Getting warranty condition by id: {}", id);
        return conditionRepository.findById(id)
                .map(this::mapToConditionDTO)
                .orElseThrow(() -> new RuntimeException("Warranty condition not found with id: " + id));
    }
    
    public WarrantyConditionDTO createCondition(WarrantyConditionDTO conditionDTO) {
        log.info("Creating new warranty condition: {}", conditionDTO.getName());
        
        WarrantyCondition condition = WarrantyCondition.builder()
                .name(conditionDTO.getName())
                .description(conditionDTO.getDescription())
                .isActive(conditionDTO.getIsActive() != null ? conditionDTO.getIsActive() : true)
                .build();
        
        WarrantyCondition savedCondition = conditionRepository.save(condition);
        return mapToConditionDTO(savedCondition);
    }
    
    @Transactional
    public Boolean validateWarrantyRequest(WarrantyValidationDTO validationDTO) {
        log.info("Validating warranty request: {}", validationDTO.getWarrantyRequestId());
        
        List<WarrantyConditionResult> results = new ArrayList<>();
        Boolean validation = true;
        // Process individual condition results
        if (validationDTO.getConditionResults() != null) {
            System.out.println("hiep dep trai");
            for (WarrantyValidationDTO.ConditionResultDTO resultDTO : validationDTO.getConditionResults()) {
                WarrantyCondition condition = conditionRepository.findById(resultDTO.getConditionId())
                        .orElseThrow(() -> new RuntimeException("Warranty condition not found with id: " + resultDTO.getConditionId()));
                if(resultDTO.getPassed() == false) validation = false;
                WarrantyConditionResult result = WarrantyConditionResult.builder()
                        .warrantyRequestId(validationDTO.getWarrantyRequestId())
                        .condition(condition)
                        .passed(resultDTO.getPassed())
                        .notes(resultDTO.getNotes())
                        .evaluatedBy(validationDTO.getValidatedBy())
                        .evaluatedAt(LocalDateTime.now())
                        .build();
                
                results.add(result);
            }
        }
        
        // Save all condition results
        resultRepository.saveAll(results);
        
        // Return validation result
        return validation;
    }
    
    public List<WarrantyConditionResultDTO> getResultsByWarrantyRequestId(Integer warrantyRequestId) {
        log.info("Getting condition results for warranty request: {}", warrantyRequestId);
        
        return resultRepository.findByWarrantyRequestId(warrantyRequestId).stream()
                .map(this::mapToResultDTO)
                .collect(Collectors.toList());
    }
    
    // Mapping methods
    private WarrantyConditionDTO mapToConditionDTO(WarrantyCondition condition) {
        return WarrantyConditionDTO.builder()
                .id(condition.getId())
                .name(condition.getName())
                .description(condition.getDescription())
                .isActive(condition.getIsActive())
                .build();
    }
    
    private WarrantyConditionResultDTO mapToResultDTO(WarrantyConditionResult result) {
        return WarrantyConditionResultDTO.builder()
                .id(result.getId())
                .warrantyRequestId(result.getWarrantyRequestId())
                .conditionId(result.getCondition().getId())
                .conditionName(result.getCondition().getName())
                .passed(result.getPassed())
                .notes(result.getNotes())
                .evaluatedBy(result.getEvaluatedBy())
                .evaluatedAt(result.getEvaluatedAt())
                .build();
    }
}