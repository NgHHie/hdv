package com.example.service_technician.services;

import com.example.service_technician.dto.TechnicianRequest;
import com.example.service_technician.dto.TechnicianResponse;
import com.example.service_technician.exception.ResourceNotFoundException;
import com.example.service_technician.models.Technician;
import com.example.service_technician.repository.TechnicianRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TechnicianService {
    
    private final TechnicianRepository technicianRepository;
    
    @Transactional
    public TechnicianResponse createTechnician(TechnicianRequest technicianRequest) {
        log.info("Creating new technician with email: {}", technicianRequest.getEmail());
        
        // Check if email already exists
        if (technicianRepository.findByEmail(technicianRequest.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Technician with email " + technicianRequest.getEmail() + " already exists");
        }
        
        Technician technician = Technician.builder()
                .name(technicianRequest.getName())
                .email(technicianRequest.getEmail())
                .phone(technicianRequest.getPhone())
                .specialization(technicianRequest.getSpecialization())
                .yearsOfExperience(technicianRequest.getYearsOfExperience())
                .isActive(technicianRequest.getIsActive() != null ? technicianRequest.getIsActive() : true)
                .build();
        
        Technician savedTechnician = technicianRepository.save(technician);
        return mapToTechnicianResponse(savedTechnician);
    }
    
    public TechnicianResponse getTechnicianById(Long id) {
        log.info("Getting technician with id: {}", id);
        
        Technician technician = technicianRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Technician not found with id: " + id));
        
        return mapToTechnicianResponse(technician);
    }
    
    public TechnicianResponse getTechnicianByEmail(String email) {
        log.info("Getting technician with email: {}", email);
        
        Technician technician = technicianRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Technician not found with email: " + email));
        
        return mapToTechnicianResponse(technician);
    }
    
    public List<TechnicianResponse> getAllTechnicians() {
        log.info("Getting all technicians");
        
        return technicianRepository.findAll().stream()
                .map(this::mapToTechnicianResponse)
                .collect(Collectors.toList());
    }
    
    public List<TechnicianResponse> getActiveTechnicians() {
        log.info("Getting all active technicians");
        
        return technicianRepository.findByIsActiveTrue().stream()
                .map(this::mapToTechnicianResponse)
                .collect(Collectors.toList());
    }
    
    public List<TechnicianResponse> getTechniciansBySpecialization(String specialization) {
        log.info("Getting technicians with specialization: {}", specialization);
        
        return technicianRepository.findBySpecialization(specialization).stream()
                .map(this::mapToTechnicianResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public TechnicianResponse updateTechnician(Long id, TechnicianRequest technicianRequest) {
        log.info("Updating technician with id: {}", id);
        
        Technician technician = technicianRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Technician not found with id: " + id));
        
        // Check if email is being changed and if the new email already exists
        if (!technician.getEmail().equals(technicianRequest.getEmail()) &&
                technicianRepository.findByEmail(technicianRequest.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Technician with email " + technicianRequest.getEmail() + " already exists");
        }
        
        technician.setName(technicianRequest.getName());
        technician.setEmail(technicianRequest.getEmail());
        technician.setPhone(technicianRequest.getPhone());
        technician.setSpecialization(technicianRequest.getSpecialization());
        technician.setYearsOfExperience(technicianRequest.getYearsOfExperience());
        
        if (technicianRequest.getIsActive() != null) {
            technician.setIsActive(technicianRequest.getIsActive());
        }
        
        Technician updatedTechnician = technicianRepository.save(technician);
        return mapToTechnicianResponse(updatedTechnician);
    }
    
    @Transactional
    public void deleteTechnician(Long id) {
        log.info("Deleting technician with id: {}", id);
        
        if (!technicianRepository.existsById(id)) {
            throw new ResourceNotFoundException("Technician not found with id: " + id);
        }
        
        technicianRepository.deleteById(id);
    }
    
    @Transactional
    public TechnicianResponse toggleTechnicianActiveStatus(Long id) {
        log.info("Toggling active status for technician with id: {}", id);
        
        Technician technician = technicianRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Technician not found with id: " + id));
        
        technician.setIsActive(!technician.getIsActive());
        
        Technician updatedTechnician = technicianRepository.save(technician);
        return mapToTechnicianResponse(updatedTechnician);
    }
    
    private TechnicianResponse mapToTechnicianResponse(Technician technician) {
        return TechnicianResponse.builder()
                .id(technician.getId())
                .name(technician.getName())
                .email(technician.getEmail())
                .phone(technician.getPhone())
                .specialization(technician.getSpecialization())
                .yearsOfExperience(technician.getYearsOfExperience())
                .isActive(technician.getIsActive())
                .createdAt(technician.getCreatedAt())
                .updatedAt(technician.getUpdatedAt())
                .build();
    }
}