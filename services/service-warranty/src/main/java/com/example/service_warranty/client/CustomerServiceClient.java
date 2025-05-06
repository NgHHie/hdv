package com.example.service_warranty.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceClient {
    
    private final WebClient.Builder webClientBuilder;
    
    @Value("${service.customer.url:http://localhost:8081}")
    private String customerServiceUrl;
    
    /**
     * Get purchase date for a specific customer and product
     */
    public LocalDate getPurchaseDate(Long productId) {
        String url = "/api/v1/customers" + "/purchase/" + productId;
        log.info("Getting purchase history from: {}{}", customerServiceUrl, url);
        
        try {
            PurchaseResponse purchase = webClientBuilder.build()
                    .get()
                    .uri(customerServiceUrl + url)
                    .retrieve()
                    .bodyToMono(PurchaseResponse.class)
                    .block();
            
            if (purchase != null) {
                if (purchase.getPurchaseDate() != null) {
                    try {
                        if (purchase.getPurchaseDate().contains("T")) {
                            // If ISO format with time
                            LocalDateTime dateTime = LocalDateTime.parse(purchase.getPurchaseDate());
                            return dateTime.toLocalDate();
                        } else {
                            // If just date
                            return LocalDate.parse(purchase.getPurchaseDate());
                        }
                    } catch (Exception e) {
                        log.error("Error parsing purchase date: {}", e.getMessage());
                    }
                }

            }
            
            log.warn("No purchase found for product {}", productId);
            return null;
        } catch (Exception e) {
            log.error("Failed to get purchase history: {}", e.getMessage());
            return null;
        }
    }
    
    // Response models
    public static class PurchaseResponse {
        private Integer id;
        private String purchaseDate;
        private String invoiceNumber;
        private Double totalAmount;
        private String paymentMethod;
        private Integer customerId;
        private String customerName;
        private List<PurchaseItemResponse> items;
        
        // Getters and setters
        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }
        
        public String getPurchaseDate() { return purchaseDate; }
        public void setPurchaseDate(String purchaseDate) { this.purchaseDate = purchaseDate; }
        
        public String getInvoiceNumber() { return invoiceNumber; }
        public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }
        
        public Double getTotalAmount() { return totalAmount; }
        public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
        
        public String getPaymentMethod() { return paymentMethod; }
        public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
        
        public Integer getCustomerId() { return customerId; }
        public void setCustomerId(Integer customerId) { this.customerId = customerId; }
        
        public String getCustomerName() { return customerName; }
        public void setCustomerName(String customerName) { this.customerName = customerName; }
        
        public List<PurchaseItemResponse> getItems() { return items; }
        public void setItems(List<PurchaseItemResponse> items) { this.items = items; }
    }
    
    public static class PurchaseItemResponse {
        private Integer id;
        private Integer purchaseId;
        private Integer productId;
        private Integer quantity;
        private Double unitPrice;
        private String productName;
        private String productSerialNumber;
        private String productCategory;
        private Float warrantyDuration;
        
        // Getters and setters
        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }
        
        public Integer getPurchaseId() { return purchaseId; }
        public void setPurchaseId(Integer purchaseId) { this.purchaseId = purchaseId; }
        
        public Integer getProductId() { return productId; }
        public void setProductId(Integer productId) { this.productId = productId; }
        
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
        
        public Double getUnitPrice() { return unitPrice; }
        public void setUnitPrice(Double unitPrice) { this.unitPrice = unitPrice; }
        
        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }
        
        public String getProductSerialNumber() { return productSerialNumber; }
        public void setProductSerialNumber(String productSerialNumber) { this.productSerialNumber = productSerialNumber; }
        
        public String getProductCategory() { return productCategory; }
        public void setProductCategory(String productCategory) { this.productCategory = productCategory; }
        
        public Float getWarrantyDuration() { return warrantyDuration; }
        public void setWarrantyDuration(Float warrantyDuration) { this.warrantyDuration = warrantyDuration; }
    }
}