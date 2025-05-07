package com.example.service_repair.services;

import com.example.service_repair.client.WarrantyServiceClient;
import com.example.service_repair.constants.RepairStatus;
import com.example.service_repair.dto.*;
import com.example.service_repair.exception.RepairRequestNotFoundException;
import com.example.service_repair.exception.TechnicianNotFoundException;
import com.example.service_repair.models.*;
import com.example.service_repair.repositories.*;
import com.example.service_repair.state.RepairContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RepairService {

    private final RepairRequestRepository repairRequestRepository;
    private final RepairStatusHistoryRepository statusHistoryRepository;
    private final RepairPartRepository partRepository;
    private final TechnicianRepository technicianRepository;
    private final RepairActionRepository actionRepository;
    
    private final WarrantyServiceClient warrantyServiceClient;

    /**
     * Create a new repair request
     */
    @Transactional
    public RepairRequestResponseDto createRepairRequest(RepairRequestDto repairRequestDto) {
        log.info("Creating new repair request for customer: {}, product: {}", 
            repairRequestDto.getCustomerId(), repairRequestDto.getProductId());
            
        RepairRequest repairRequest = RepairRequest.builder()
                .warrantyId(repairRequestDto.getWarrantyId())
                .customerId(repairRequestDto.getCustomerId())
                .productId(repairRequestDto.getProductId())
                .issueDescription(repairRequestDto.getIssueDescription())
                .imageUrls(repairRequestDto.getImageUrls())
                .status(RepairStatus.SUBMITTED)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .withinWarranty(true)
                .build();
        
        RepairRequest savedRequest = repairRequestRepository.save(repairRequest);
        
        // Create initial status history entry
        RepairStatusHistory initialStatus = RepairStatusHistory.builder()
                .repairRequest(savedRequest)
                .status(RepairStatus.SUBMITTED)
                .notes("Repair request created")
                .createdBy("SYSTEM")
                .createdAt(LocalDateTime.now())
                .build();
        
        statusHistoryRepository.save(initialStatus);
        
        // Send notification to customer
        // notificationServiceClient.sendRepairRequestCreatedNotification(
        //     savedRequest.getCustomerId(), savedRequest.getId());
            
        return convertToResponseDto(savedRequest);
    }
    
    /**
     * Get repair request by ID
     */
    public RepairRequestResponseDto getRepairRequestById(Integer id) {
        RepairRequest repairRequest = repairRequestRepository.findById(id)
                .orElseThrow(() -> new RepairRequestNotFoundException("Repair request not found with id: " + id));
        
        return convertToResponseDto(repairRequest);
    }
    
    /**
     * Get all repair requests for a customer
     */
    public List<RepairRequestResponseDto> getRepairRequestsByCustomerId(Integer customerId) {
        List<RepairRequest> requests = repairRequestRepository.findByCustomerId(customerId);
        return requests.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Get all repair requests by status
     */
    public List<RepairRequestResponseDto> getRepairRequestsByStatus(RepairStatus status) {
        List<RepairRequest> requests = repairRequestRepository.findByStatus(status);
        return requests.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }
    
    
    /**
     * Update repair notes
     */
    @Transactional
    public RepairRequestResponseDto updateRepairNotes(Integer repairId, String notes, String username) {
        RepairRequest repairRequest = getRepairRequestEntityById(repairId);
        repairRequest.setRepairNotes(notes);
        repairRequest.setUpdatedAt(LocalDateTime.now());
        
        RepairRequest updatedRequest = repairRequestRepository.save(repairRequest);
        
        // Create repair action for updating notes
        createStateChangeAction(updatedRequest, username, 
                "NOTES_UPDATE", 
                "Repair notes updated");
        
        return convertToResponseDto(updatedRequest);
    }
    
    /**
     * Assign technician to repair request
     */
    @Transactional
    public RepairRequestResponseDto assignTechnician(Integer repairId, Integer technicianId, String username) {
        RepairRequest repairRequest = getRepairRequestEntityById(repairId);
        
        Technician technician = technicianRepository.findById(technicianId)
                .orElseThrow(() -> new TechnicianNotFoundException("Technician not found with id: " + technicianId));
        
        repairRequest.setTechnicianId(technicianId);
        repairRequest.setUpdatedAt(LocalDateTime.now());
        
        RepairRequest updatedRequest = repairRequestRepository.save(repairRequest);
        
        // Create repair action for technician assignment
        createStateChangeAction(updatedRequest, username, 
                "TECHNICIAN_ASSIGNMENT", 
                "Assigned to technician: " + technician.getName());
        
        return convertToResponseDto(updatedRequest);
    }
    
    /**
     * Add repair action
     */
    @Transactional
    public RepairActionDto addRepairAction(Integer repairId, RepairActionDto actionDto, String username) {
        RepairRequest repairRequest = getRepairRequestEntityById(repairId);
        
        Optional<Technician> technician = Optional.empty();
        if (actionDto.getTechnicianId() != null) {
            technician = technicianRepository.findById(actionDto.getTechnicianId());
        }
        
        RepairAction action = RepairAction.builder()
                .repairRequest(repairRequest)
                .actionType(actionDto.getActionType())
                .description(actionDto.getDescription())
                .performedBy(technician.orElse(null))
                .performedAt(LocalDateTime.now())
                .build();
        
        RepairAction savedAction = actionRepository.save(action);
        
        return convertToActionDto(savedAction);
    }
    
    
    /**
     * Process product receipt - mark as RECEIVED
     */
    @Transactional
    public RepairRequestResponseDto processProductReceipt(Integer repairId, TechnicianDto technician, String username) {
        RepairRequest repairRequest = getRepairRequestEntityById(repairId);
        
        if (repairRequest.getStatus() != RepairStatus.SUBMITTED) {
            throw new IllegalStateException("Repair request is not in SUBMITTED state");
        }
        
        repairRequest.setStatus(RepairStatus.RECEIVED);
        repairRequest.setUpdatedAt(LocalDateTime.now());
        repairRequest.setTechnicianId(technician.getId());
        
        RepairRequest updatedRequest = repairRequestRepository.save(repairRequest);
        
        // Create status history entry
        RepairStatusHistory statusHistory = RepairStatusHistory.builder()
                .repairRequest(updatedRequest)
                .status(RepairStatus.RECEIVED)
                .notes("Product received for repair")
                .createdBy(String.valueOf(technician.getId()))
                .createdAt(LocalDateTime.now())
                .build();
        
        statusHistoryRepository.save(statusHistory);
        
        // Create repair action for product receipt
        createStateChangeAction(updatedRequest, username, 
                "PRODUCT_RECEIPT", 
                "Product received for repair");

        warrantyServiceClient.updateWarrantyStatus(repairRequest.getWarrantyId(), "REPAIR_IN_PROGRESS", username);
          
        return convertToResponseDto(updatedRequest);
    }
    
    
    /**
     * Create state change action
     */
    private void createStateChangeAction(RepairRequest repairRequest, String username, String actionType, String description) {
        // Find technician by username if available
        Optional<Technician> technician = technicianRepository.findByEmail(username);
        
        RepairAction action = RepairAction.builder()
                .repairRequest(repairRequest)
                .actionType(actionType)
                .description(description)
                .performedBy(technician.orElse(null))
                .performedAt(LocalDateTime.now())
                .build();
        
        actionRepository.save(action);
    }
    
    /**
     * Helper method to get entity by ID
     */
    private RepairRequest getRepairRequestEntityById(Integer id) {
        return repairRequestRepository.findById(id)
                .orElseThrow(() -> new RepairRequestNotFoundException("Repair request not found with id: " + id));
    }
    
    /**
     * Convert entity to DTO
     */
    private RepairRequestResponseDto convertToResponseDto(RepairRequest repairRequest) {
        RepairRequestResponseDto dto = RepairRequestResponseDto.builder()
                .id(repairRequest.getId())
                .warrantyId(repairRequest.getWarrantyId())
                .customerId(repairRequest.getCustomerId())
                .productId(repairRequest.getProductId())
                .issueDescription(repairRequest.getIssueDescription())
                .imageUrls(repairRequest.getImageUrls())
                .status(repairRequest.getStatus().name())
                .repairNotes(repairRequest.getRepairNotes())
                .technicianId(repairRequest.getTechnicianId())
                .startDate(repairRequest.getStartDate())
                .endDate(repairRequest.getEndDate())
                .createdAt(repairRequest.getCreatedAt())
                .updatedAt(repairRequest.getUpdatedAt())
                .repairCost(repairRequest.getRepairCost())
                .withinWarranty(repairRequest.getWithinWarranty())
                .build();
        
        // Add technician name if technician is assigned
        if (repairRequest.getTechnicianId() != null) {
            technicianRepository.findById(repairRequest.getTechnicianId())
                    .ifPresent(technician -> dto.setTechnicianName(technician.getName()));
        }
        
        // Add status history
        List<RepairStatusHistory> statusHistory = statusHistoryRepository.findByRepairRequestIdOrderByCreatedAtDesc(repairRequest.getId());
        dto.setStatusHistory(statusHistory.stream()
                .map(this::convertToStatusHistoryDto)
                .collect(Collectors.toList()));
        
        // Add parts
        List<RepairPart> parts = partRepository.findByRepairRequestId(repairRequest.getId());
        dto.setParts(parts.stream()
                .map(this::convertToPartDto)
                .collect(Collectors.toList()));
        
        // Add actions
        List<RepairAction> actions = actionRepository.findByRepairRequestId(repairRequest.getId());
        dto.setActions(actions.stream()
                .map(this::convertToActionDto)
                .collect(Collectors.toList()));
        
        return dto;
    }
    
    /**
     * Convert status history entity to DTO
     */
    private RepairStatusHistoryDto convertToStatusHistoryDto(RepairStatusHistory history) {
        return RepairStatusHistoryDto.builder()
                .id(history.getId())
                .status(history.getStatus().name())
                .notes(history.getNotes())
                .createdBy(history.getCreatedBy())
                .createdAt(history.getCreatedAt())
                .build();
    }
    
    /**
     * Convert action entity to DTO
     */
    private RepairActionDto convertToActionDto(RepairAction action) {
        RepairActionDto dto = RepairActionDto.builder()
                .id(action.getId())
                .actionType(action.getActionType())
                .description(action.getDescription())
                .performedAt(action.getPerformedAt())
                .build();
        
        if (action.getPerformedBy() != null) {
            dto.setTechnicianId(action.getPerformedBy().getId());
            dto.setTechnicianName(action.getPerformedBy().getName());
        }
        
        return dto;
    }

    /**
     * Start the diagnostic process for a repair request
     */
    @Transactional
    public RepairRequestResponseDto startDiagnosis(Integer repairId, String username) {
        RepairRequest repairRequest = getRepairRequestEntityById(repairId);
        
        if (repairRequest.getStatus() != RepairStatus.RECEIVED) {
            throw new IllegalStateException("Repair request must be in RECEIVED state to start diagnosis");
        }
        
        RepairContext context = new RepairContext(repairRequest);
        context.moveToNextState();
        
        repairRequest.setStatus(context.getCurrentState().getStatus());
        repairRequest.setUpdatedAt(LocalDateTime.now());
        
        RepairRequest updatedRequest = repairRequestRepository.save(repairRequest);
        
        // Create status history entry
        RepairStatusHistory statusHistory = RepairStatusHistory.builder()
                .repairRequest(updatedRequest)
                .status(updatedRequest.getStatus())
                .notes("Started diagnosis process")
                .createdBy(username)
                .createdAt(LocalDateTime.now())
                .build();
        
        statusHistoryRepository.save(statusHistory);
        
        // Create repair action
        createStateChangeAction(updatedRequest, username, 
                "START_DIAGNOSIS", 
                "Started diagnosis process");
        
        return convertToResponseDto(updatedRequest);
    }

    /**
     * Complete diagnosis and start repair
     */
    @Transactional
    public RepairRequestResponseDto completeDiagnosisAndStartRepair(Integer repairId, DiagnosisDto diagnosisDto, String username) {
        RepairRequest repairRequest = getRepairRequestEntityById(repairId);
        
        if (repairRequest.getStatus() != RepairStatus.DIAGNOSING) {
            throw new IllegalStateException("Repair request must be in DIAGNOSING state to complete diagnosis");
        }
        
        // Update repair notes with diagnosis
        String currentNotes = repairRequest.getRepairNotes() != null ? repairRequest.getRepairNotes() : "";
        repairRequest.setRepairNotes(currentNotes + "\nDiagnosis results: " + diagnosisDto.getDiagnosticNotes());
        
        RepairContext context = new RepairContext(repairRequest);
        context.moveToNextState();
        
        repairRequest.setStatus(context.getCurrentState().getStatus());
        repairRequest.setUpdatedAt(LocalDateTime.now());
        
        RepairRequest updatedRequest = repairRequestRepository.save(repairRequest);
        
        // Create status history entry
        RepairStatusHistory statusHistory = RepairStatusHistory.builder()
                .repairRequest(updatedRequest)
                .status(updatedRequest.getStatus())
                .notes("Completed diagnosis and started repair. Diagnosis: " + diagnosisDto.getDiagnosticNotes())
                .createdBy(username)
                .createdAt(LocalDateTime.now())
                .build();
        
        statusHistoryRepository.save(statusHistory);
        
        // Create repair action
        createStateChangeAction(updatedRequest, username, 
                "COMPLETE_DIAGNOSIS", 
                "Completed diagnosis: " + diagnosisDto.getDiagnosticNotes());
        
        return convertToResponseDto(updatedRequest);
    }

    /**
     * Add part to repair
     */
    @Transactional
    public RepairPartDto addRepairPart(Integer repairId, RepairPartDto partDto, String username) {
        RepairRequest repairRequest = getRepairRequestEntityById(repairId);
        
        if (repairRequest.getStatus() != RepairStatus.REPAIRING) {
            throw new IllegalStateException("Can only add parts when repair is in REPAIRING state");
        }
        
        RepairPart part = partRepository.findById(partDto.getId()).orElse(null);
        part.setRepairRequest(repairRequest);
        partRepository.save(part);
        
        // Create repair action for adding part
        createStateChangeAction(repairRequest, username, 
                "PART_ADDED", 
                "Added part: " + part.getPartName());
        
        return convertToPartDto(part);
    }

    /**
     * Complete repair
     */
    @Transactional
    public RepairRequestResponseDto completeRepair(Integer repairId, RepairCompletionDto completionDto, String username) {
        RepairRequest repairRequest = getRepairRequestEntityById(repairId);
        
        if (repairRequest.getStatus() != RepairStatus.REPAIRING) {
            throw new IllegalStateException("Repair request must be in REPAIRING state to complete repair");
        }
        
        // Update repair notes with completion details
        String currentNotes = repairRequest.getRepairNotes() != null ? repairRequest.getRepairNotes() : "";
        repairRequest.setRepairNotes(currentNotes + "\nCompletion notes: " + completionDto.getCompletionNotes());
        
        RepairContext context = new RepairContext(repairRequest);
        context.moveToNextState();
        
        repairRequest.setStatus(context.getCurrentState().getStatus());
        repairRequest.setUpdatedAt(LocalDateTime.now());
        repairRequest.setEndDate(LocalDateTime.now());
        
        RepairRequest updatedRequest = repairRequestRepository.save(repairRequest);
        
        // Create status history entry
        RepairStatusHistory statusHistory = RepairStatusHistory.builder()
                .repairRequest(updatedRequest)
                .status(updatedRequest.getStatus())
                .notes("Repair completed. Notes: " + completionDto.getCompletionNotes())
                .createdBy(username)
                .createdAt(LocalDateTime.now())
                .build();
        
        statusHistoryRepository.save(statusHistory);
        
        // Create repair action
        createStateChangeAction(updatedRequest, username, 
                "COMPLETE_REPAIR", 
                "Repair completed: " + completionDto.getCompletionNotes());

        warrantyServiceClient.updateWarrantyStatus(repairRequest.getWarrantyId(), "COMPLETE_REPAIR", updatedRequest.getRepairNotes());
        
        return convertToResponseDto(updatedRequest);
    }

    /**
     * Convert part entity to DTO
     */
    private RepairPartDto convertToPartDto(RepairPart part) {
        return RepairPartDto.builder()
                .id(part.getId())
                .partName(part.getPartName())
                .partNumber(part.getPartNumber())
                .description(part.getDescription())
                .isWarrantyReplacement(part.getIsWarrantyReplacement())
                .build();
    }
}