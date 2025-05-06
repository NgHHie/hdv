package com.example.service_warranty.services;

import com.example.service_warranty.client.ProductServiceClient;
import com.example.service_warranty.client.RepairServiceClient;
import com.example.service_warranty.client.CustomerServiceClient;
import com.example.service_warranty.client.NotificationServiceClient;
import com.example.service_warranty.dto.*;
import com.example.service_warranty.dto.NotificationType;
import com.example.service_warranty.event.WarrantyNotificationEvent;
import com.example.service_warranty.exception.WarrantyRequestNotFoundException;
import com.example.service_warranty.models.*;
import com.example.service_warranty.repositories.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class WarrantyRequestService {
    
    private final WarrantyRequestRepository warrantyRequestRepository;
    private final WarrantyHistoryRepository warrantyHistoryRepository;
    private final WarrantyRepository warrantyRepository;
    private final ProductServiceClient productServiceClient;
    private final RepairServiceClient repairServiceClient;
    private final NotificationServiceClient notificationServiceClient;
    private final CustomerServiceClient customerServiceClient;
    private final ObjectMapper objectMapper;
    private final KafkaProducerService kafkaProducerService;

    /**
     * Create a new warranty request
     */
    @Transactional
    public WarrantyRequestDto createWarrantyRequest(WarrantyRequestCreateDto requestDto) throws JsonProcessingException {
        log.info("Creating new warranty request for product: {}, customer: {}", 
                requestDto.getSerialNumber(), requestDto.getCustomerId());
        
        boolean isWithinWarranty = false;
        String productName = "Product";
        Float warrantyDuration = null;
        LocalDate purchaseDate = null;
        
        ProductServiceClient.ProductResponse product = 
                productServiceClient.getProductDetailsBySerial(requestDto.getSerialNumber());
        CustomerServiceClient.CustomerResponse customer = customerServiceClient.getCustomerById(requestDto.getCustomerId());
        
        if (product != null) {
            warrantyDuration = product.getWarrantyDuration();
            productName = product.getName();
            
            // 2. Get purchase date from customer service
            purchaseDate = customerServiceClient.getPurchaseDate(product.getId());
            
            log.info("Purchase date for product {}: {}", product.getId(), purchaseDate);
            
            // 3. Check if within warranty period based on duration
            if (warrantyDuration != null && purchaseDate != null) {
                // Convert warranty duration from years to months
                int warrantyMonths = Math.round(warrantyDuration * 12);
                
                // Calculate if current date is within warranty period
                LocalDate warrantyEndDate = purchaseDate.plusMonths(warrantyMonths);
                isWithinWarranty = !LocalDate.now().isAfter(warrantyEndDate);
                
                log.info("Warranty end date: {}, Is within warranty: {}", warrantyEndDate, isWithinWarranty);
            } else {
                log.warn("Missing warranty duration or purchase date. Duration: {}, Purchase date: {}", 
                        warrantyDuration, purchaseDate);
            }
        } else {
            log.warn("Product information not found for product ID: {}", product.getId());
        }
        
        // Serialize image URLs to JSON
        String imageUrlsJson = objectMapper.writeValueAsString(requestDto.getImageUrls());
        
        // Create the warranty request
        WarrantyRequest warrantyRequest = WarrantyRequest.builder()
                .customerId(requestDto.getCustomerId())
                .productId(product.getId())
                .serialNumber(requestDto.getSerialNumber())
                .issueDescription(requestDto.getIssueDescription())
                .imageUrls(imageUrlsJson)
                .submissionDate(LocalDateTime.now())
                .build();

                

//        public class WarrantyNotificationEvent {
//            private Integer warrantyRequestId;
//            private String email;
//            private String productName;
//
//            @Enumerated(EnumType.STRING)
//            private NotificationType type;
//            private String message;
//        }

      
        System.out.println(customer);

        WarrantyRequest saveWarranty = warrantyRequestRepository.save(warrantyRequest);
        WarrantyNotificationEvent event = WarrantyNotificationEvent.builder()
                .warrantyRequestId(saveWarranty.getId())
                .type(NotificationType.WARRANTY_CREATE)
                .email(customer.getEmail())
                .customerName(customer.getFirstName() + " " + customer.getLastName())
                
                .productName(productName)
                .message("Warranty Request Created!")
                .customerId(customer.getId())
                .build();
        kafkaProducerService.sendWarrantyEvent(event);
        return mapToWarrantyRequestDto(warrantyRequest);
    }
    
    /**
     * Get warranty request by ID
     */
    public WarrantyRequestDto getWarrantyRequestById(Integer id) {
        log.info("Getting warranty request with id: {}", id);
        
        WarrantyRequest request = warrantyRequestRepository.findById(id)
                .orElseThrow(() -> new WarrantyRequestNotFoundException("Warranty request not found with id: " + id));
        
        return mapToWarrantyRequestDto(request);
    }
    
    /**
     * Get warranty requests by customer ID
     */
    public List<WarrantyRequestDto> getWarrantyRequestsByCustomerId(Integer customerId) {
        log.info("Getting warranty requests for customer: {}", customerId);
        
        List<WarrantyRequest> requests = warrantyRequestRepository.findByCustomerId(customerId);
        List<WarrantyRequestDto> dtos = new ArrayList<>();
        
        for (WarrantyRequest request : requests) {
            dtos.add(mapToWarrantyRequestDto(request));
        }
        
        return dtos;
    }
    
    /**
     * Get warranty requests by status
     */
    public List<WarrantyRequestDto> getWarrantyRequestsByStatus(String status) {
        log.info("Getting warranty requests with status: {}", status);
        
        List<WarrantyRequest> requests = warrantyRequestRepository.findByStatus(status);
        List<WarrantyRequestDto> dtos = new ArrayList<>();
        
        for (WarrantyRequest request : requests) {
            dtos.add(mapToWarrantyRequestDto(request));
        }
        
        return dtos;
    }
    
    /**
     * Validate warranty request
     */
    // @Transactional
    // public WarrantyRequestDto validateWarrantyRequest(Integer id, WarrantyValidationDto validationDto) {
    //     log.info("Validating warranty request: {}", id);
        
    //     WarrantyRequest request = warrantyRequestRepository.findById(id)
    //             .orElseThrow(() -> new WarrantyRequestNotFoundException("Warranty request not found with id: " + id));
        
    //     if (!"PENDING".equals(request.getStatus())) {
    //         throw new IllegalStateException("Cannot validate warranty request with status: " + request.getStatus());
    //     }
        
    //     // Update request with validation result
    //     String newStatus = validationDto.getIsValid() ? "APPROVED" : "REJECTED";
    //     request.setStatus(newStatus);
    //     request.setValidationNotes(validationDto.getValidationReason());
        
    //     WarrantyRequest updatedRequest = warrantyRequestRepository.save(request);
        
    //     // Add history entry
    //     WarrantyHistory history = WarrantyHistory.builder()
    //             .warrantyRequestId(id)
    //             .status(newStatus)
    //             .notes(validationDto.getValidationReason())
    //             .performedBy(validationDto.getValidatedBy())
    //             .performedAt(LocalDateTime.now())
    //             .build();
        
    //     warrantyHistoryRepository.save(history);
        
    //     // Send notification to customer
    //     if (validationDto.getIsValid()) {
    //         // Send approval notification
    //         notificationServiceClient.sendWarrantyApprovedNotification(
    //                 request.getCustomerId(),
    //                 request.getId(),
    //                 "Your warranty request has been approved. Please send your product to our service center."
    //         );
    //     } else {
    //         // Send rejection notification
    //         notificationServiceClient.sendWarrantyRejectedNotification(
    //                 request.getCustomerId(),
    //                 request.getId(),
    //                 "Your warranty request has been rejected. Reason: " + validationDto.getValidationReason()
    //         );
    //     }
        
    //     return mapToWarrantyRequestDto(updatedRequest);
    // }
    
    /**
     * Reject warranty request
     */
    // @Transactional
    // public WarrantyRequestDto rejectWarrantyRequest(Integer id, String reason, String performedBy) {
    //     log.info("Rejecting warranty request: {}", id);
        
    //     WarrantyRequest request = warrantyRequestRepository.findById(id)
    //             .orElseThrow(() -> new WarrantyRequestNotFoundException("Warranty request not found with id: " + id));
        
    //     if (!"PENDING".equals(request.getStatus()) && !"APPROVED".equals(request.getStatus())) {
    //         throw new IllegalStateException("Cannot reject warranty request with status: " + request.getStatus());
    //     }
        
    //     request.setStatus("REJECTED");
    //     request.setValidationNotes(reason);
        
    //     WarrantyRequest updatedRequest = warrantyRequestRepository.save(request);
        
    //     // Add history entry
    //     WarrantyHistory history = WarrantyHistory.builder()
    //             .warrantyRequestId(id)
    //             .status("REJECTED")
    //             .notes(reason)
    //             .performedBy(performedBy)
    //             .performedAt(LocalDateTime.now())
    //             .build();
        
    //     warrantyHistoryRepository.save(history);
        
    //     // Send notification
    //     notificationServiceClient.sendWarrantyRejectedNotification(
    //             request.getCustomerId(),
    //             request.getId(),
    //             "Your warranty request has been rejected. Reason: " + reason
    //     );
        
    //     return mapToWarrantyRequestDto(updatedRequest);
    // }
    
    /**
     * Approve warranty request
     */
    @Transactional
    public WarrantyRequestDto approveWarrantyRequest(Integer id, String notes, String performedBy) {
        log.info("Approving warranty request: {}", id);
        
        WarrantyRequest request = warrantyRequestRepository.findById(id)
                .orElseThrow(() -> new WarrantyRequestNotFoundException("Warranty request not found with id: " + id));
        
        if (!"PENDING".equals(request.getStatus())) {
            throw new IllegalStateException("Cannot approve warranty request with status: " + request.getStatus());
        }
        
        request.setStatus("APPROVED");
        request.setValidationNotes(notes);
        
        WarrantyRequest updatedRequest = warrantyRequestRepository.save(request);
        
        // Add history entry
        WarrantyHistory history = WarrantyHistory.builder()
                .warrantyRequestId(id)
                .status("APPROVED")
                .notes(notes)
                .performedBy(performedBy)
                .performedAt(LocalDateTime.now())
                .build();
        
        warrantyHistoryRepository.save(history);
        
        // Send notification
        notificationServiceClient.sendWarrantyApprovedNotification(
                request.getCustomerId(),
                request.getId(),
                "Your warranty request has been approved. Please send your product to our service center."
        );
        
        return mapToWarrantyRequestDto(updatedRequest);
    }
    
    /**
     * Mark warranty request as received
     */
    @Transactional
    public WarrantyRequestDto receiveWarrantyRequest(Integer id, String notes, String performedBy) {
        log.info("Marking warranty request as received: {}", id);
        
        WarrantyRequest request = warrantyRequestRepository.findById(id)
                .orElseThrow(() -> new WarrantyRequestNotFoundException("Warranty request not found with id: " + id));
        
        if (!"APPROVED".equals(request.getStatus())) {
            throw new IllegalStateException("Cannot mark as received warranty request with status: " + request.getStatus());
        }
        
        request.setStatus("RECEIVED");
        
        WarrantyRequest updatedRequest = warrantyRequestRepository.save(request);
        
        // Add history entry
        WarrantyHistory history = WarrantyHistory.builder()
                .warrantyRequestId(id)
                .status("RECEIVED")
                .notes(notes)
                .performedBy(performedBy)
                .performedAt(LocalDateTime.now())
                .build();
        
        warrantyHistoryRepository.save(history);
        
        // Send notification
        notificationServiceClient.sendProductReceivedNotification(
                request.getCustomerId(),
                request.getId(),
                "We have received your product and will begin the repair process soon."
        );
        
        return mapToWarrantyRequestDto(updatedRequest);
    }
    
    /**
     * Forward warranty request to repair service
     */
    @Transactional
    public WarrantyRequestDto forwardToRepair(Integer id, String notes, String performedBy) {
        log.info("Forwarding warranty request to repair: {}", id);
        
        WarrantyRequest request = warrantyRequestRepository.findById(id)
                .orElseThrow(() -> new WarrantyRequestNotFoundException("Warranty request not found with id: " + id));
        
        if (!"RECEIVED".equals(request.getStatus())) {
            throw new IllegalStateException("Cannot forward to repair warranty request with status: " + request.getStatus());
        }
        
        // Create repair request using repair service client
        Integer repairId = repairServiceClient.createRepairRequest(
                request.getCustomerId(), 
                request.getProductId(),
                request.getId(), 
                request.getIssueDescription(), 
                request.getImageUrls());
        
        request.setStatus("IN_REPAIR");
        request.setRepairId(repairId);
        
        WarrantyRequest updatedRequest = warrantyRequestRepository.save(request);
        
        // Add history entry
        WarrantyHistory history = WarrantyHistory.builder()
                .warrantyRequestId(id)
                .status("IN_REPAIR")
                .notes("Forwarded to repair service. Repair ID: " + repairId)
                .performedBy(performedBy)
                .performedAt(LocalDateTime.now())
                .build();
        
        warrantyHistoryRepository.save(history);
        
        return mapToWarrantyRequestDto(updatedRequest);
    }
    
    /**
     * Map entity to DTO
     */
    private WarrantyRequestDto mapToWarrantyRequestDto(WarrantyRequest request) {
        List<String> imageUrlsList = new ArrayList<>();
        if (request.getImageUrls() != null && !request.getImageUrls().isEmpty()) {
            try {
                imageUrlsList = objectMapper.readValue(request.getImageUrls(), List.class);
            } catch (JsonProcessingException e) {
                log.error("Failed to parse image URLs", e);
            }
        }
        
        // Get product name from product service
        String productName = "";
        
        return WarrantyRequestDto.builder()
                .id(request.getId())
                .customerId(request.getCustomerId())
                .productId(request.getProductId())
                .productName(productName)
                .serialNumber(request.getSerialNumber())
                .issueDescription(request.getIssueDescription())
                .imageUrls(imageUrlsList)
                .status(request.getStatus())
                .submissionDate(request.getSubmissionDate())
                .expirationDate(request.getExpirationDate())
                .validationNotes(request.getValidationNotes())
                .repairId(request.getRepairId())
                .build();
    }
}