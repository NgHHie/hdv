package com.example.service_customer.service;

import com.example.service_customer.dto.WarrantyHistoryDTO;
import com.example.service_customer.dto.WarrantyRequestDTO;
import com.example.service_customer.model.Customer;
import com.example.service_customer.model.WarrantyHistory;
import com.example.service_customer.model.WarrantyRequest;
import com.example.service_customer.repository.CustomerRepository;
import com.example.service_customer.repository.WarrantyHistoryRepository;
import com.example.service_customer.repository.WarrantyRequestRepository;
import com.example.service_customer.util.UpdateStatusWarranty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final CustomerRepository customerRepository;
    private final ObjectMapper objectMapper;
    
    @Transactional
    public WarrantyRequestDTO createWarrantyRequest(WarrantyRequestDTO requestDto) throws JsonProcessingException {
        log.info("Creating new warranty request for product: {}, customer: {}", 
                requestDto.getSerialNumber(), requestDto.getCustomerId());
        
        Customer customer = customerRepository.findById(requestDto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + requestDto.getCustomerId()));
        
        // Serialize image URLs to JSON
        String imageUrlsJson = objectMapper.writeValueAsString(requestDto.getImageUrls());
        
        // Create the warranty request
        WarrantyRequest warrantyRequest = WarrantyRequest.builder()
                .customer(customer)
                .serialNumber(requestDto.getSerialNumber())
                .issueDescription(requestDto.getIssueDescription())
                .imageUrls(imageUrlsJson)
                .status(requestDto.getStatus())
                .submissionDate(LocalDateTime.now())
                .expirationDate(requestDto.getExpirationDate())
                .build();
        
        WarrantyRequest savedRequest = warrantyRequestRepository.save(warrantyRequest);
        
        // Create initial history entry
        WarrantyHistory history = WarrantyHistory.builder()
                .warrantyRequest(savedRequest)
                .status(requestDto.getStatus())
                .notes("Warranty request submitted")
                .performedBy("SYSTEM")
                .performedAt(LocalDateTime.now())
                .build();
        
        warrantyHistoryRepository.save(history);
        
        return mapToWarrantyRequestDTO(savedRequest);
    }
    
    public WarrantyRequestDTO getWarrantyRequestById(Integer id) {
        log.info("Getting warranty request with id: {}", id);
        
        WarrantyRequest request = warrantyRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Warranty request not found with id: " + id));
        
        return mapToWarrantyRequestDTO(request);
    }
    
    public List<WarrantyRequestDTO> getWarrantyRequestsByCustomerId(Integer customerId) {
        log.info("Getting warranty requests for customer: {}", customerId);
        
        List<WarrantyRequest> requests = warrantyRequestRepository.findByCustomerId(customerId);
        List<WarrantyRequestDTO> dtos = new ArrayList<>();
        
        for (WarrantyRequest request : requests) {
            dtos.add(mapToWarrantyRequestDTO(request));
        }
        
        return dtos;
    }
    
    public List<WarrantyRequestDTO> getWarrantyRequestsByStatus(String status) {
        log.info("Getting warranty requests with status: {}", status);
        
        List<WarrantyRequest> requests = warrantyRequestRepository.findByStatus(status);
        List<WarrantyRequestDTO> dtos = new ArrayList<>();
        
        for (WarrantyRequest request : requests) {
            dtos.add(mapToWarrantyRequestDTO(request));
        }
        
        return dtos;
    }
    
    @Transactional
    public WarrantyRequestDTO updateWarrantyRequestStatus(Integer id, String status, String notes, String performedBy) {
        log.info("Updating warranty request status: {}", id);
        
        WarrantyRequest request = warrantyRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Warranty request not found with id: " + id));
        Boolean allowUpdateStatus = UpdateStatusWarranty.updateStatus(request.getStatus(), status);
        if(allowUpdateStatus) request.setStatus(status);
        else throw new IllegalArgumentException("Khong duoc update status");
        if (notes != null && !notes.isEmpty()) {
            request.setValidationNotes(notes);
        }
        
        WarrantyRequest updatedRequest = warrantyRequestRepository.save(request);
        
        // Add history entry
        WarrantyHistory history = WarrantyHistory.builder()
                .warrantyRequest(updatedRequest)
                .status(status)
                .notes(notes)
                .performedBy(performedBy)
                .performedAt(LocalDateTime.now())
                .build();
        
        warrantyHistoryRepository.save(history);
        
        return mapToWarrantyRequestDTO(updatedRequest);
    }
    
    public List<WarrantyHistoryDTO> getWarrantyHistoryByRequestId(Integer requestId) {
        log.info("Getting warranty history for request: {}", requestId);
        
        List<WarrantyHistory> historyList = warrantyHistoryRepository.findByWarrantyRequestIdOrderByPerformedAtDesc(requestId);
        List<WarrantyHistoryDTO> dtos = new ArrayList<>();
        
        for (WarrantyHistory history : historyList) {
            dtos.add(mapToWarrantyHistoryDTO(history));
        }
        
        return dtos;
    }

    public WarrantyRequestDTO updateRepairId(Integer warrantyId, Integer repairId) {
        WarrantyRequest request = warrantyRequestRepository.findById(warrantyId).orElse(null);
        if(request != null) {
            request.setRepairId(repairId);
            warrantyRequestRepository.save(request);
        }
        return mapToWarrantyRequestDTO(request);
    }
    
    // Map entity to DTO
    private WarrantyRequestDTO mapToWarrantyRequestDTO(WarrantyRequest request) {
        List<String> imageUrlsList = new ArrayList<>();
        if (request.getImageUrls() != null && !request.getImageUrls().isEmpty()) {
            try {
                imageUrlsList = objectMapper.readValue(request.getImageUrls(), List.class);
            } catch (JsonProcessingException e) {
                log.error("Failed to parse image URLs", e);
            }
        }
        
        return WarrantyRequestDTO.builder()
                .id(request.getId())
                .customerId(request.getCustomer().getId())
                .customerName(request.getCustomer().getFirstName() + " " + request.getCustomer().getLastName())
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
    
    private WarrantyHistoryDTO mapToWarrantyHistoryDTO(WarrantyHistory history) {
        return WarrantyHistoryDTO.builder()
                .id(history.getId())
                .warrantyRequestId(history.getWarrantyRequest().getId())
                .status(history.getStatus())
                .notes(history.getNotes())
                .performedBy(history.getPerformedBy())
                .performedAt(history.getPerformedAt())
                .build();
    }

    
}