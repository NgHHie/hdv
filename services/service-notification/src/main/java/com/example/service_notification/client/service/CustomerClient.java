package com.example.service_notification.client.service;

import com.example.service_notification.client.response.CustomerResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerClient {

    private final WebClient.Builder webClientBuilder;
    @Value("${service.customer.url}")
    private String customerServiceUrl;


    public CustomerResponse getCustomer(Integer customerID) {
        String url = "api/v1/customers/" + customerID;
        log.info("Getting product details: {}", customerID);

        try {
            return webClientBuilder.build().get()
                    .uri(customerServiceUrl + url)
                    .retrieve()
                    .bodyToMono(CustomerResponse.class)
                    .block(); // Blocking call for synchronous behavior
        } catch (Exception e) {
            log.error("Failed to get product details: {}", e.getMessage());
            return null;
        }

    }
}





