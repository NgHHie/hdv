package com.example.service_repair.services;

import com.example.service_repair.client.NotificationServiceClient;
import com.example.service_repair.client.ProductServiceClient;
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
    
    private final NotificationServiceClient notificationServiceClient;
    private final WarrantyServiceClient warrantyServiceClient;
    private final ProductServiceClient productServiceClient;

    /**
     * Create a new repair request
     */
    @Transactional
    public RepairRequestResponseDto createRepairRequest(RepairRequestDto repairRequestDto) {
        log.info("Creating new repair request for customer: {}, product: {}", 
            repairRequestDto.getCustomerId(), repairRequestDto.getProductId());
        
        // Check if product exists
        boolean productExists = productServiceClient.checkProductExists(repairRequestDto.getProductId());
        if (!productExists) {
            throw new IllegalArgumentException("Product does not exist");
        }
        
        // Check warranty status
        boolean isWithinWarranty = warrantyServiceClient.checkWarrantyStatus(
            repairRequestDto.getProductId(), repairRequestDto.getCustomerId());
            
        RepairRequest repairRequest = RepairRequest.builder()
                .warrantyId(repairRequestDto.getWarrantyId())
                .customerId(repairRequestDto.getCustomerId())
                .productId(repairRequestDto.getProductId())
                .issueDescription(repairRequestDto.getIssueDescription())
                .imageUrls(repairRequestDto.getImageUrls())
                .status(RepairStatus.SUBMITTED)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .withinWarranty(isWithinWarranty)
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
        notificationServiceClient.sendRepairRequestCreatedNotification(
            savedRequest.getCustomerId(), savedRequest.getId());
            
        return convertToResponseDto(savedRequest);
    }
    
    /**
     * Get repair request by ID
     */
    public RepairRequestResponseDto getRepairRequestById(Long id) {
        RepairRequest repairRequest = repairRequestRepository.findById(id)
                .orElseThrow(() -> new RepairRequestNotFoundException("Repair request not found with id: " + id));
        
        return convertToResponseDto(repairRequest);
    }
    
    /**
     * Get all repair requests for a customer
     */
    public List<RepairRequestResponseDto> getRepairRequestsByCustomerId(Long customerId) {
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
     * Update repair status to next state
     */
    @Transactional
    public RepairRequestResponseDto moveToNextState(Long repairId, String notes, String username) {
        RepairRequest repairRequest = getRepairRequestEntityById(repairId);
        RepairContext context = new RepairContext(repairRequest);
        
        RepairStatus previousStatus = repairRequest.getStatus();
        
        // Move to next state
        context.moveToNextState();
        
        // Update repair request
        repairRequest.setStatus(context.getCurrentState().getStatus());
        repairRequest.setUpdatedAt(LocalDateTime.now());
        
        // If status is now UNDER_DIAGNOSIS, set startDate
        if (repairRequest.getStatus() == RepairStatus.UNDER_DIAGNOSIS && repairRequest.getStartDate() == null) {
            repairRequest.setStartDate(LocalDateTime.now());
        }
        
        // If status is COMPLETED, set endDate
        if (repairRequest.getStatus() == RepairStatus.COMPLETED && repairRequest.getEndDate() == null) {
            repairRequest.setEndDate(LocalDateTime.now());
        }
        
        RepairRequest updatedRequest = repairRequestRepository.save(repairRequest);
        
        // Create status history entry
        RepairStatusHistory statusHistory = RepairStatusHistory.builder()
                .repairRequest(updatedRequest)
                .status(updatedRequest.getStatus())
                .notes(notes)
                .createdBy(username)
                .createdAt(LocalDateTime.now())
                .build();
        
        statusHistoryRepository.save(statusHistory);
        
        // Create repair action for the state change
        createStateChangeAction(updatedRequest, username, 
                "STATUS_CHANGE", 
                "Status changed from " + previousStatus + " to " + updatedRequest.getStatus());
        
        // Send notification based on the new status
        sendStatusChangeNotification(updatedRequest);
        
        return convertToResponseDto(updatedRequest);
    }
    
    /**
     * Update repair status to previous state
     */
    @Transactional
    public RepairRequestResponseDto moveToPreviousState(Long repairId, String notes, String username) {
        RepairRequest repairRequest = getRepairRequestEntityById(repairId);
        RepairContext context = new RepairContext(repairRequest);
        
        RepairStatus previousStatus = repairRequest.getStatus();
        
        // Move to previous state
        context.moveToPreviousState();
        
        // Update repair request
        repairRequest.setStatus(context.getCurrentState().getStatus());
        repairRequest.setUpdatedAt(LocalDateTime.now());
        
        RepairRequest updatedRequest = repairRequestRepository.save(repairRequest);
        
        // Create status history entry
        RepairStatusHistory statusHistory = RepairStatusHistory.builder()
                .repairRequest(updatedRequest)
                .status(updatedRequest.getStatus())
                .notes(notes)
                .createdBy(username)
                .createdAt(LocalDateTime.now())
                .build();
        
        statusHistoryRepository.save(statusHistory);
        
        // Create repair action for the state change
        createStateChangeAction(updatedRequest, username, 
                "STATUS_REVERSAL", 
                "Status reverted from " + previousStatus + " to " + updatedRequest.getStatus());
        
        // Send notification based on the new status
        sendStatusChangeNotification(updatedRequest);
        
        return convertToResponseDto(updatedRequest);
    }
    
    /**
     * Cancel a repair request
     */
    @Transactional
    public RepairRequestResponseDto cancelRepairRequest(Long repairId, String notes, String username) {
        RepairRequest repairRequest = getRepairRequestEntityById(repairId);
        RepairContext context = new RepairContext(repairRequest);
        
        try {
            RepairStatus previousStatus = repairRequest.getStatus();
            
            // Try to cancel
            context.cancelRepair();
            
            // Update repair request
            repairRequest.setStatus(context.getCurrentState().getStatus());
            repairRequest.setUpdatedAt(LocalDateTime.now());
            repairRequest.setEndDate(LocalDateTime.now());
            
            RepairRequest updatedRequest = repairRequestRepository.save(repairRequest);
            
            // Create status history entry
            RepairStatusHistory statusHistory = RepairStatusHistory.builder()
                    .repairRequest(updatedRequest)
                    .status(updatedRequest.getStatus())
                    .notes(notes)
                    .createdBy(username)
                    .createdAt(LocalDateTime.now())
                    .build();
            
            statusHistoryRepository.save(statusHistory);
            
            // Create repair action for the cancellation
            createStateChangeAction(updatedRequest, username, 
                    "CANCELLATION", 
                    "Request cancelled. Previous status: " + previousStatus);
            
            // Send cancellation notification
            notificationServiceClient.sendRepairRequestCancelledNotification(
                updatedRequest.getCustomerId(), updatedRequest.getId());
                
            return convertToResponseDto(updatedRequest);
        } catch (Exception e) {
            log.error("Cannot cancel repair request: {}", e.getMessage());
            throw new IllegalStateException("Cannot cancel repair request in current state: " + repairRequest.getStatus());
        }
    }
    
    /**
     * Update repair notes
     */
    @Transactional
    public RepairRequestResponseDto updateRepairNotes(Long repairId, String notes, String username) {
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
    public RepairRequestResponseDto assignTechnician(Long repairId, Long technicianId, String username) {
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
     * Add part to repair
     */
    @Transactional
    public RepairPartDto addRepairPart(Long repairId, RepairPartDto partDto, String username) {
        RepairRequest repairRequest = getRepairRequestEntityById(repairId);
        
        RepairPart part = RepairPart.builder()
                .repairRequest(repairRequest)
                .partName(partDto.getPartName())
                .partNumber(partDto.getPartNumber())
                .quantity(partDto.getQuantity())
                .unitPrice(partDto.getUnitPrice())
                .build();
        
        RepairPart savedPart = partRepository.save(part);
        
        // Create repair action for adding part
        createStateChangeAction(repairRequest, username, 
                "PART_ADDED", 
                "Added part: " + part.getPartName() + " x" + part.getQuantity());
        
        // Update total repair cost
        updateRepairCost(repairId);
        
        return convertToPartDto(savedPart);
    }
    
    /**
     * Remove part from repair
     */
    @Transactional
    public void removeRepairPart(Long repairId, Long partId, String username) {
        RepairRequest repairRequest = getRepairRequestEntityById(repairId);
        
        RepairPart part = partRepository.findById(partId)
                .orElseThrow(() -> new IllegalArgumentException("Part not found with id: " + partId));
        
        if (!part.getRepairRequest().getId().equals(repairId)) {
            throw new IllegalArgumentException("Part does not belong to this repair request");
        }
        
        // Create repair action for removing part
        createStateChangeAction(repairRequest, username, 
                "PART_REMOVED", 
                "Removed part: " + part.getPartName() + " x" + part.getQuantity());
        
        partRepository.delete(part);
        
        // Update total repair cost
        updateRepairCost(repairId);
    }
    
    /**
     * Add repair action
     */
    @Transactional
    public RepairActionDto addRepairAction(Long repairId, RepairActionDto actionDto, String username) {
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
     * Reject a repair request (e.g., if warranty verification fails)
     */
    @Transactional
    public RepairRequestResponseDto rejectRepairRequest(Long repairId, String reason, String username) {
        RepairRequest repairRequest = getRepairRequestEntityById(repairId);
        RepairStatus previousStatus = repairRequest.getStatus();
        
        repairRequest.setStatus(RepairStatus.REJECTED);
        repairRequest.setRepairNotes(repairRequest.getRepairNotes() != null ? 
                repairRequest.getRepairNotes() + "\nRejection reason: " + reason : 
                "Rejection reason: " + reason);
        repairRequest.setUpdatedAt(LocalDateTime.now());
        repairRequest.setEndDate(LocalDateTime.now());
        
        RepairRequest updatedRequest = repairRequestRepository.save(repairRequest);
        
        // Create status history entry
        RepairStatusHistory statusHistory = RepairStatusHistory.builder()
                .repairRequest(updatedRequest)
                .status(RepairStatus.REJECTED)
                .notes("Request rejected. Reason: " + reason)
                .createdBy(username)
                .createdAt(LocalDateTime.now())
                .build();
        
        statusHistoryRepository.save(statusHistory);
        
        // Create repair action for rejection
        createStateChangeAction(updatedRequest, username, 
                "REJECTION", 
                "Request rejected. Reason: " + reason + ". Previous status: " + previousStatus);
        
        // Send rejection notification
        notificationServiceClient.sendRepairRequestRejectedNotification(
            updatedRequest.getCustomerId(), updatedRequest.getId(), reason);
            
        return convertToResponseDto(updatedRequest);
    }
    
    /**
     * Process product receipt - mark as RECEIVED
     */
    @Transactional
    public RepairRequestResponseDto processProductReceipt(Long repairId, String username) {
        RepairRequest repairRequest = getRepairRequestEntityById(repairId);
        
        if (repairRequest.getStatus() != RepairStatus.SUBMITTED) {
            throw new IllegalStateException("Repair request is not in SUBMITTED state");
        }
        
        repairRequest.setStatus(RepairStatus.RECEIVED);
        repairRequest.setUpdatedAt(LocalDateTime.now());
        
        RepairRequest updatedRequest = repairRequestRepository.save(repairRequest);
        
        // Create status history entry
        RepairStatusHistory statusHistory = RepairStatusHistory.builder()
                .repairRequest(updatedRequest)
                .status(RepairStatus.RECEIVED)
                .notes("Product received for repair")
                .createdBy(username)
                .createdAt(LocalDateTime.now())
                .build();
        
        statusHistoryRepository.save(statusHistory);
        
        // Create repair action for product receipt
        createStateChangeAction(updatedRequest, username, 
                "PRODUCT_RECEIPT", 
                "Product received for repair");
        
        // Send product received notification
        notificationServiceClient.sendProductReceivedNotification(
            updatedRequest.getCustomerId(), updatedRequest.getId());
            
        return convertToResponseDto(updatedRequest);
    }
    
    /**
     * Update repair cost
     */
    @Transactional
    public RepairRequestResponseDto updateRepairCost(Long repairId, BigDecimal manualCost, String username) {
        RepairRequest repairRequest = getRepairRequestEntityById(repairId);
        
        if (manualCost != null) {
            repairRequest.setRepairCost(manualCost);
        } else {
            // Calculate cost based on parts
            updateRepairCost(repairId);
            return getRepairRequestById(repairId);
        }
        
        repairRequest.setUpdatedAt(LocalDateTime.now());
        RepairRequest updatedRequest = repairRequestRepository.save(repairRequest);
        
        // Create repair action for cost update
        createStateChangeAction(updatedRequest, username, 
                "COST_UPDATE", 
                "Repair cost updated to: " + manualCost);
        
        return convertToResponseDto(updatedRequest);
    }
    
    /**
     * Calculate and update repair cost based on parts
     */
    private void updateRepairCost(Long repairId) {
        RepairRequest repairRequest = getRepairRequestEntityById(repairId);
        
        // Skip if it's under warranty
        if (Boolean.TRUE.equals(repairRequest.getWithinWarranty())) {
            repairRequest.setRepairCost(BigDecimal.ZERO);
            repairRequestRepository.save(repairRequest);
            return;
        }
        
        List<RepairPart> parts = partRepository.findByRepairRequestId(repairId);
        
        BigDecimal totalCost = parts.stream()
                .map(part -> part.getUnitPrice().multiply(BigDecimal.valueOf(part.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        repairRequest.setRepairCost(totalCost);
        repairRequest.setUpdatedAt(LocalDateTime.now());
        repairRequestRepository.save(repairRequest);
    }
    
    /**
     * Get dashboard statistics
     */
    public DashboardStatsDto getDashboardStats() {
        DashboardStatsDto stats = new DashboardStatsDto();
        
        stats.setTotalRepairRequests(repairRequestRepository.count());
        
        List<RepairStatus> pendingStatuses = List.of(RepairStatus.SUBMITTED, RepairStatus.RECEIVED);
        stats.setPendingRepairRequests((long) repairRequestRepository.findByStatusIn(pendingStatuses).size());
        
        List<RepairStatus> inProgressStatuses = List.of(
                RepairStatus.UNDER_DIAGNOSIS, RepairStatus.DIAGNOSING, 
                RepairStatus.WAITING_APPROVAL, RepairStatus.REPAIRING,
                RepairStatus.REPAIRED, RepairStatus.TESTING);
        stats.setInProgressRepairRequests((long) repairRequestRepository.findByStatusIn(inProgressStatuses).size());
        
        stats.setCompletedRepairRequests((long) repairRequestRepository.findByStatus(RepairStatus.DELIVERED).size());
        stats.setCancelledRepairRequests((long) repairRequestRepository.findByStatus(RepairStatus.CANCELLED).size());
        
        // Calculate average repair time for completed repairs
        List<RepairRequest> completedRepairs = repairRequestRepository.findByStatus(RepairStatus.DELIVERED);
        if (!completedRepairs.isEmpty()) {
            double averageHours = completedRepairs.stream()
                    .filter(repair -> repair.getStartDate() != null && repair.getEndDate() != null)
                    .mapToLong(repair -> Duration.between(repair.getStartDate(), repair.getEndDate()).toHours())
                    .average()
                    .orElse(0);
            stats.setAverageRepairTime(averageHours);
        } else {
            stats.setAverageRepairTime(0.0);
        }
        
        // Get technician performance
        List<Technician> activeTechnicians = technicianRepository.findByIsActiveTrue();
        List<DashboardStatsDto.TechnicianPerformanceDto> technicianPerformance = new ArrayList<>();
        
        for (Technician technician : activeTechnicians) {
            List<RepairRequest> technicianCompletedRepairs = repairRequestRepository.findByTechnicianId(technician.getId()).stream()
                    .filter(repair -> repair.getStatus() == RepairStatus.DELIVERED)
                    .collect(Collectors.toList());
            
            double techAverageHours = technicianCompletedRepairs.stream()
                    .filter(repair -> repair.getStartDate() != null && repair.getEndDate() != null)
                    .mapToLong(repair -> Duration.between(repair.getStartDate(), repair.getEndDate()).toHours())
                    .average()
                    .orElse(0);
            
            DashboardStatsDto.TechnicianPerformanceDto perfDto = DashboardStatsDto.TechnicianPerformanceDto.builder()
                    .technicianId(technician.getId())
                    .technicianName(technician.getName())
                    .completedRepairs((long) technicianCompletedRepairs.size())
                    .averageRepairTime(techAverageHours)
                    .build();
            
            technicianPerformance.add(perfDto);
        }
        
        stats.setTechnicianPerformance(technicianPerformance);
        
        return stats;
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
    private RepairRequest getRepairRequestEntityById(Long id) {
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
     * Convert part entity to DTO
     */
    private RepairPartDto convertToPartDto(RepairPart part) {
        BigDecimal totalPrice = part.getUnitPrice().multiply(BigDecimal.valueOf(part.getQuantity()));
        
        return RepairPartDto.builder()
                .id(part.getId())
                .partName(part.getPartName())
                .partNumber(part.getPartNumber())
                .quantity(part.getQuantity())
                .unitPrice(part.getUnitPrice())
                .totalPrice(totalPrice)
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
     * Send appropriate notification based on status change
     */
    private void sendStatusChangeNotification(RepairRequest repairRequest) {
        switch (repairRequest.getStatus()) {
            case UNDER_DIAGNOSIS:
                notificationServiceClient.sendRepairDiagnosisStartedNotification(
                    repairRequest.getCustomerId(), repairRequest.getId());
                break;
            case WAITING_APPROVAL:
                notificationServiceClient.sendRepairApprovalRequiredNotification(
                    repairRequest.getCustomerId(), repairRequest.getId(), 
                    repairRequest.getRepairCost() != null ? repairRequest.getRepairCost().doubleValue() : 0.0);
                break;
            case REPAIRING:
                notificationServiceClient.sendRepairInProgressNotification(
                    repairRequest.getCustomerId(), repairRequest.getId());
                break;
            case COMPLETED:
                notificationServiceClient.sendRepairCompletedNotification(
                    repairRequest.getCustomerId(), repairRequest.getId());
                break;
            case DELIVERING:
                notificationServiceClient.sendProductShippingNotification(
                    repairRequest.getCustomerId(), repairRequest.getId());
                break;
            case DELIVERED:
                notificationServiceClient.sendProductDeliveredNotification(
                    repairRequest.getCustomerId(), repairRequest.getId());
                break;
            default:
                // No notification for other status changes
                break;
        }
    }
}