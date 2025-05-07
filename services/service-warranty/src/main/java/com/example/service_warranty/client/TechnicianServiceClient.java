package com.example.service_warranty.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.service_warranty.dto.TechnicianDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TechnicianServiceClient {
    private final WebClient.Builder webClientBuilder;
    
    @Value("${service.technician.url}")
    private String technicianServiceUrl;

    public TechnicianDto getTechnicianById(Integer id) {
        String url = "/api/v1/technicians/" + id;
        log.info("Getting info technician id: {}", id);

        try {
            return webClientBuilder.build().get()
                        .uri(technicianServiceUrl + url)
                        .retrieve()
                        .bodyToMono(TechnicianDto.class)
                        .block();
        } catch (Exception e) {
            log.error("Failed to get technician info: {}", e.getMessage());
            return null;
        }

    }
}
