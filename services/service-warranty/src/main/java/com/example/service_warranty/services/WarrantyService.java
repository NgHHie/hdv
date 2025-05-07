// Warranty Service (restructured as orchestrator)
// src/main/java/com/example/service_warranty/services/WarrantyOrchestratorService.java
package com.example.service_warranty.services;

import com.example.service_warranty.client.CustomerServiceClient;
import com.example.service_warranty.client.ProductServiceClient;
import com.example.service_warranty.client.RepairServiceClient;
import com.example.service_warranty.client.ConditionServiceClient;
import com.example.service_warranty.dto.NotificationRequestDto;
import com.example.service_warranty.dto.WarrantyRequestCreateDto;
import com.example.service_warranty.dto.WarrantyRequestDto;
import com.example.service_warranty.dto.WarrantyValidationDto;
import com.example.service_warranty.event.WarrantyNotificationEvent;
import com.example.service_warranty.models.NotificationType;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WarrantyService {
    
    private final CustomerServiceClient customerServiceClient;
    private final ProductServiceClient productServiceClient;
    private final RepairServiceClient repairServiceClient;
    private final ConditionServiceClient conditionServiceClient; // New client
    private final KafkaProducerService kafkaProducerService;
    
    /**
     * Create a new warranty request
     */
    public WarrantyRequestDto createWarrantyRequest(WarrantyRequestCreateDto requestDto) throws JsonProcessingException {
        log.info("Orchestrating warranty request creation for product: {}, customer: {}", 
                requestDto.getSerialNumber(), requestDto.getCustomerId());
        
        // Check warranty validity first
        boolean isWithinWarranty = false;
        ProductServiceClient.ProductResponse product = 
                productServiceClient.getProductDetailsBySerial(requestDto.getSerialNumber());
        
        LocalDate warrantyEndDate = null;
        if (product != null) {
            Float warrantyDuration = product.getWarrantyDuration();
            
            // Get purchase date from customer service
            LocalDate purchaseDate = customerServiceClient.getPurchaseDate(product.getId());
            
            // Check if within warranty period based on duration
            if (warrantyDuration != null && purchaseDate != null) {
                // Convert warranty duration from years to months
                int warrantyMonths = Math.round(warrantyDuration * 12);
                
                // Calculate if current date is within warranty period
                warrantyEndDate = purchaseDate.plusMonths(warrantyMonths);
                isWithinWarranty = !LocalDate.now().isAfter(warrantyEndDate);
            }
        }
        
        // Create the warranty request in customer service
        requestDto.setExpirationDate(warrantyEndDate);
        WarrantyRequestDto createdRequest = customerServiceClient.createWarrantyRequest(requestDto, isWithinWarranty);
        createdRequest.setProductId(product.getId());
        createdRequest.setProductName(product.getName());
        
        // Get customer details
        CustomerServiceClient.CustomerResponse customer = customerServiceClient.getCustomerById(requestDto.getCustomerId());
        
        // Send notification
        if (isWithinWarranty) {
            // Send confirmation notification
            WarrantyNotificationEvent event = WarrantyNotificationEvent.builder()
                    .warrantyRequestId(createdRequest.getId())
                    .type(NotificationType.WARRANTY_RECEIVED)
                    .email(customer.getEmail())
                    .customerName(customer.getFirstName() + " " + customer.getLastName())
                    .productName(product != null ? product.getName() : "Product")
                    .message("Your warranty request has been received and is being processed.")
                    .customerId(customer.getId())
                    .build();
            kafkaProducerService.sendWarrantyEvent(event);
        } else {
            // Send rejection notification
            WarrantyNotificationEvent event = WarrantyNotificationEvent.builder()
                    .warrantyRequestId(createdRequest.getId())
                    .type(NotificationType.WARRANTY_REJECTED)
                    .email(customer.getEmail())
                    .customerName(customer.getFirstName() + " " + customer.getLastName())
                    .productName(product != null ? product.getName() : "Product")
                    .message("Your warranty request has been rejected. The product is out of warranty period.")
                    .customerId(customer.getId())
                    .build();
            kafkaProducerService.sendWarrantyEvent(event);
        }
        
        return createdRequest;
    }
    

    /**
     * Complete validation and forward to repair if approved
     */
    @Transactional
    public WarrantyRequestDto completeValidationAndForward(Integer id, WarrantyValidationDto validationDto) {
        log.info("Completing validation and forwarding warranty request: {}", id);
        // First validate with condition service
        Boolean isValid = conditionServiceClient.validateWarrantyConditions(validationDto);

        // Now update the status in customer service
        String status = isValid ? "APPROVED" : "REJECTED";
        WarrantyRequestDto updatedRequest = customerServiceClient.updateWarrantyRequestStatus(
                id, status, validationDto.getValidationReason(), validationDto.getValidatedBy());
        
        ProductServiceClient.ProductResponse product = productServiceClient.getProductDetailsBySerial(updatedRequest.getSerialNumber());
        log.info("tim thay product voi id: " + product.getId());
        // Get customer details for notification
        CustomerServiceClient.CustomerResponse customer = customerServiceClient.getCustomerById(updatedRequest.getCustomerId());
        
        // Send notification
        if (isValid) {
            // Send approval notification
            WarrantyNotificationEvent event = WarrantyNotificationEvent.builder()
                    .warrantyRequestId(id)
                    .type(NotificationType.WARRANTY_APPROVED)
                    .email(customer.getEmail())
                    .customerName(customer.getFirstName() + " " + customer.getLastName())
                    .productName(product != null ? product.getName() : "Product")
                    .message("Your warranty request has been approved. Please send your product to our service center.")
                    .customerId(customer.getId())
                    .build();
            kafkaProducerService.sendWarrantyEvent(event);
        } else {
            // Send rejection notification
            WarrantyNotificationEvent event = WarrantyNotificationEvent.builder()
                    .warrantyRequestId(id)
                    .type(NotificationType.WARRANTY_REJECTED)
                    .email(customer.getEmail())
                    .customerName(customer.getFirstName() + " " + customer.getLastName())
                    .productName(product != null ? product.getName() : "Product")
                    .message("Your warranty request has been rejected. Reason: " + validationDto.getValidationReason())
                    .customerId(customer.getId())
                    .build();
            kafkaProducerService.sendWarrantyEvent(event);
        }
        updatedRequest.setProductId(product.getId());
        updatedRequest.setProductName(product.getName());
        return updatedRequest;
    }

    /**
     * Process product receipt and forward to repair
     */
    @Transactional
    public WarrantyRequestDto receiveAndForwardToRepair(Integer id, String notes, String performedBy) {
        log.info("Processing product receipt and forwarding to repair: {}", id);
        
        // Mark as received in customer service
        WarrantyRequestDto updatedRequest = customerServiceClient.updateWarrantyRequestStatus(
                id, "PRODUCT_RECEIVED", notes, performedBy);
        
        // Get customer information for notification
        CustomerServiceClient.CustomerResponse customer = customerServiceClient.getCustomerById(updatedRequest.getCustomerId());
        ProductServiceClient.ProductResponse product = productServiceClient.getProductDetailsBySerial(updatedRequest.getSerialNumber());
        // Send product received notification
        WarrantyNotificationEvent event = WarrantyNotificationEvent.builder()
                .warrantyRequestId(id)
                .type(NotificationType.PRODUCT_RECEIVED)
                .email(customer.getEmail())
                .customerName(customer.getFirstName() + " " + customer.getLastName())
                .productName(product.getName())
                .message("We have received your product and will begin the repair process soon.")
                .customerId(customer.getId())
                .build();
        kafkaProducerService.sendWarrantyEvent(event);
        
        // Create repair request
        Integer repairId = repairServiceClient.createRepairRequest(
                updatedRequest.getCustomerId(), 
                product.getId(),
                id, 
                updatedRequest.getIssueDescription(), 
                (updatedRequest.getImageUrls() != null && !updatedRequest.getImageUrls().isEmpty()) ? 
                        String.join(",", updatedRequest.getImageUrls()) : null);
        
        return updatedRequest;
    }

    /**
     * Update repair status
     */
    public WarrantyRequestDto updateRepairStatus(Integer id, String status, String notes, String performedBy) {
        log.info("Updating repair status to {} for warranty request: {}", status, id);
        
        // Update status in customer service
        WarrantyRequestDto updatedRequest = customerServiceClient.updateWarrantyRequestStatus(
                id, status, notes, performedBy);
        
        // Send notifications based on status
        CustomerServiceClient.CustomerResponse customer = customerServiceClient.getCustomerById(updatedRequest.getCustomerId());
        ProductServiceClient.ProductResponse product = productServiceClient.getProductDetailsBySerial(updatedRequest.getSerialNumber());
        if ("REPAIR_IN_PROGRESS".equals(status)) {
            WarrantyNotificationEvent event = WarrantyNotificationEvent.builder()
                    .warrantyRequestId(id)
                    .type(NotificationType.REPAIR_IN_PROGRESS)
                    .email(customer.getEmail())
                    .customerName(customer.getFirstName() + " " + customer.getLastName())
                    .productName(product.getName())
                    .message("Your product is now being repaired.")
                    .customerId(customer.getId())
                    .build();
            kafkaProducerService.sendWarrantyEvent(event);
        } else if ("COMPLETE_REPAIR".equals(status)) {
            WarrantyNotificationEvent event = WarrantyNotificationEvent.builder()
                    .warrantyRequestId(id)
                    .type(NotificationType.REPAIR_COMPLETED)
                    .email(customer.getEmail())
                    .customerName(customer.getFirstName() + " " + customer.getLastName())
                    .productName(product.getName())
                    .message("Your product has been repaired. " + notes)
                    .customerId(customer.getId())
                    .build();
            kafkaProducerService.sendWarrantyEvent(event);
        }
        
        return updatedRequest;
    }
}