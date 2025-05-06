package com.example.service_warranty.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${service.product.url:http://service-product}")
    private String productServiceUrl;

    /**
     * Get product details to check warranty information
     */
    public ProductResponse getProductDetails(Integer productId) {
        String url = "/api/v1/products/" + productId;
        log.info("Getting product details from: {}{}", productServiceUrl, url);

        try {
            return webClientBuilder.build()
                    .get()
                    .uri(productServiceUrl + url)
                    .retrieve()
                    .bodyToMono(ProductResponse.class)
                    .block(); // Blocking call for synchronous behavior
        } catch (Exception e) {
            log.error("Failed to get product details: {}", e.getMessage());
            return null;
        }
    }

    public ProductResponse getProductDetailsBySerial(String serialNumber) {
        String url = "/api/v1/products/serial/" + serialNumber;
        log.info("Getting product details from: {}{}", productServiceUrl, url);

        try {
            return webClientBuilder.build()
                    .get()
                    .uri(productServiceUrl + url)
                    .retrieve()
                    .bodyToMono(ProductResponse.class)
                    .block(); // Blocking call for synchronous behavior
        } catch (Exception e) {
            log.error("Failed to get product details: {}", e.getMessage());
            return null;
        }
    }

    // Response model for product details
    public static class ProductResponse {
        private Integer id;
        private String name;
        private String description;
        private String category;
        private Float warrantyDuration;

        // Getters and setters
        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }


        public Float getWarrantyDuration() { return warrantyDuration; }
        public void setWarrantyDuration(Float warrantyDuration) { this.warrantyDuration = warrantyDuration; }
    }


}