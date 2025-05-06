package com.example.service_warranty.services;

import com.example.service_warranty.client.ProductServiceClient;
import com.example.service_warranty.client.RepairServiceClient;
import com.example.service_warranty.dto.*;
import com.example.service_warranty.event.WarrantyNotificationEvent;
import com.example.service_warranty.exception.WarrantyRequestNotFoundException;
import com.example.service_warranty.models.Warranty;
import com.example.service_warranty.models.WarrantyHistory;
import com.example.service_warranty.models.WarrantyRequest;
import com.example.service_warranty.models.WarrantyValidation;
import com.example.service_warranty.repositories.WarrantyHistoryRepository;
import com.example.service_warranty.repositories.WarrantyRepository;
import com.example.service_warranty.repositories.WarrantyRequestRepository;
import com.example.service_warranty.repositories.WarrantyValidationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WarrantyRequestService {
    
    private final WarrantyRequestRepository warrantyRequestRepository;
    private final WarrantyHistoryRepository warrantyHistoryRepository;
    private final WarrantyValidationRepository warrantyValidationRepository;
    private final WarrantyRepository warrantyRepository;
    private final ProductServiceClient productServiceClient;
    private final RepairServiceClient repairServiceClient;
    private final ObjectMapper objectMapper;
    private final KafkaProducerService kafkaProducerService;
    
    /**
     * Create a new warranty request
     * @throws JsonProcessingException 
     */
    @Transactional
    public WarrantyRequestDto createWarrantyRequest(WarrantyRequestCreateDto requestDto) throws JsonProcessingException {
        log.info("Creating new warranty request for product: {}, customer: {}", 
                requestDto.getProductId(), requestDto.getCustomerId());
        

        Optional<Warranty> warrantyOpt = warrantyRepository.findByProductIdAndCustomerId(
                requestDto.getProductId(), requestDto.getCustomerId());
        
        LocalDate expirationDate = null;
        ProductServiceClient.ProductResponse product = null;
        if (warrantyOpt.isPresent()) {
            Warranty warranty = warrantyOpt.get();
            expirationDate = warranty.getExpirationDate();
        } else {

            product =
                    productServiceClient.getProductDetails(requestDto.getProductId());
            
            if (product != null) {
                expirationDate = product.getWarrantyExpiration();
            }
        }
        String imageUrlsJson = objectMapper.writeValueAsString(requestDto.getImageUrls());
        WarrantyRequest warrantyRequest = WarrantyRequest.builder()
                .customerId(requestDto.getCustomerId())
                .productId(requestDto.getProductId())
                .serialNumber(requestDto.getSerialNumber())
                .issueDescription(requestDto.getIssueDescription())
                .imageUrls(imageUrlsJson)
                .status("PENDING")
                .submissionDate(LocalDateTime.now())
                .expirationDate(expirationDate)
                .build();
        
        WarrantyRequest savedRequest = warrantyRequestRepository.save(warrantyRequest);



        WarrantyNotificationEvent notificationEvent = WarrantyNotificationEvent.builder()
        .warrantyRequestId(savedRequest.getId())
        .customerId(savedRequest.getCustomerId())
        .productName(product.getName())
        .type(NotificationType.REPAIR_CREATED).message("Warranty Request Created")
        .build();


        kafkaProducerService.sendWarrantyCreatedEvent(notificationEvent);




        

        return mapToWarrantyRequestDto(savedRequest);
    }
    
    /**
     * Get warranty request by ID
     */
    public WarrantyRequestDto getWarrantyRequestById(Long id) {
        log.info("Getting warranty request with id: {}", id);
        
        WarrantyRequest request = warrantyRequestRepository.findById(id)
                .orElseThrow(() -> new WarrantyRequestNotFoundException("Warranty request not found with id: " + id));
        
        return mapToWarrantyRequestDto(request);
    }
    
    /**
     * Get warranty requests by customer ID
     */
    public List<WarrantyRequestDto> getWarrantyRequestsByCustomerId(Long customerId) {
        log.info("Getting warranty requests for customer: {}", customerId);
        
        List<WarrantyRequest> requests = warrantyRequestRepository.findByCustomerId(customerId);
        return requests.stream()
                .map(this::mapToWarrantyRequestDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Get warranty requests by status
     */
    public List<WarrantyRequestDto> getWarrantyRequestsByStatus(String status) {
        log.info("Getting warranty requests with status: {}", status);
        
        List<WarrantyRequest> requests = warrantyRequestRepository.findByStatus(status);
        return requests.stream()
                .map(this::mapToWarrantyRequestDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Validate warranty request - step 3 in workflow
     */
    @Transactional
    public WarrantyRequestDto validateWarrantyRequest(Long id, WarrantyValidationDto validationDto) {
        log.info("Validating warranty request: {}", id);
        
        WarrantyRequest request = warrantyRequestRepository.findById(id)
                .orElseThrow(() -> new WarrantyRequestNotFoundException("Warranty request not found with id: " + id));
        
        if (!"PENDING".equals(request.getStatus())) {
            throw new IllegalStateException("Cannot validate warranty request with status: " + request.getStatus());
        }
        
        // Create validation record
        WarrantyValidation validation = WarrantyValidation.builder()
                .warrantyRequestId(id)
                .isValid(validationDto.getIsValid())
                .validationReason(validationDto.getValidationReason())
                .validatedBy(validationDto.getValidatedBy())
                .validatedAt(LocalDateTime.now())
                .build();
        
        warrantyValidationRepository.save(validation);
        
        // Update request
        request.setValidationNotes(validationDto.getValidationReason());
        
        // If valid, move to APPROVED, otherwise REJECTED
        String newStatus = validationDto.getIsValid() ? "APPROVED" : "REJECTED";
        request.setStatus(newStatus);
        
        WarrantyRequest updatedRequest = warrantyRequestRepository.save(request);
        
        // Add history entry
        WarrantyHistory history = WarrantyHistory.builder()
                .warrantyRequestId(id)
                .status(newStatus)
                .notes(validationDto.getValidationReason())
                .performedBy(validationDto.getValidatedBy())
                .performedAt(LocalDateTime.now())
                .build();
        
        warrantyHistoryRepository.save(history);
        
        return mapToWarrantyRequestDto(updatedRequest);
    }
    
    /**
     * Reject warranty request - step 4 in workflow
     */
    @Transactional
    public WarrantyRequestDto rejectWarrantyRequest(Long id, String reason, String performedBy) {
        log.info("Rejecting warranty request: {}", id);
        
        WarrantyRequest request = warrantyRequestRepository.findById(id)
                .orElseThrow(() -> new WarrantyRequestNotFoundException("Warranty request not found with id: " + id));
        
        if (!"PENDING".equals(request.getStatus()) && !"APPROVED".equals(request.getStatus())) {
            throw new IllegalStateException("Cannot reject warranty request with status: " + request.getStatus());
        }
        
        request.setStatus("REJECTED");
        request.setValidationNotes(reason);
        
        WarrantyRequest updatedRequest = warrantyRequestRepository.save(request);
        
        // Add history entry
        WarrantyHistory history = WarrantyHistory.builder()
                .warrantyRequestId(id)
                .status("REJECTED")
                .notes(reason)
                .performedBy(performedBy)
                .performedAt(LocalDateTime.now())
                .build();
        
        warrantyHistoryRepository.save(history);
        
        return mapToWarrantyRequestDto(updatedRequest);
    }
    
    /**
     * Approve warranty request - step 5 in workflow
     */
    @Transactional
    public WarrantyRequestDto approveWarrantyRequest(Long id, String notes, String performedBy) {
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
        
        return mapToWarrantyRequestDto(updatedRequest);
    }
    
    /**
     * Mark warranty request as received - step 6 in workflow
     */
    @Transactional
    public WarrantyRequestDto receiveWarrantyRequest(Long id, String notes, String performedBy) {
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
        
        return mapToWarrantyRequestDto(updatedRequest);
    }
    
    /**
     * Forward warranty request to repair service - additional step after receiving
     */
    @Transactional
    public WarrantyRequestDto forwardToRepair(Long id, String notes, String performedBy) {
        log.info("Forwarding warranty request to repair: {}", id);
        
        WarrantyRequest request = warrantyRequestRepository.findById(id)
                .orElseThrow(() -> new WarrantyRequestNotFoundException("Warranty request not found with id: " + id));
        
        if (!"RECEIVED".equals(request.getStatus())) {
            throw new IllegalStateException("Cannot forward to repair warranty request with status: " + request.getStatus());
        }
        
        // Create repair request using repair service client
        Long repairId = repairServiceClient.createRepairRequest(
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
                imageUrlsList = objectMapper.readValue(request.getImageUrls(), new TypeReference<List<String>>() {});
            } catch (JsonProcessingException e) {
                log.error("Failed to parse image URLs", e);
            }
        }
        
        // Get product name from product service
        String productName = "";
        if (request.getProductId() != null) {
            ProductServiceClient.ProductResponse product = 
                    productServiceClient.getProductDetails(request.getProductId());
            if (product != null) {
                productName = product.getName();
            }
        }
        
        // Get history
        // List<WarrantyHistory> historyList = 
        //         warrantyHistoryRepository.findByWarrantyRequestIdOrderByPerformedAtDesc(request.getId());
        
        // List<WarrantyHistoryDto> historyDtos = historyList.stream()
        //         .map(history -> WarrantyHistoryDto.builder()
        //                 .id(history.getId())
        //                 .warrantyRequestId(history.getWarrantyRequestId())
        //                 .status(history.getStatus())
        //                 .notes(history.getNotes())
        //                 .performedBy(history.getPerformedBy())
        //                 .performedAt(history.getPerformedAt())
        //                 .build())
        //         .collect(Collectors.toList());
        
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