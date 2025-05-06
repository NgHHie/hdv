package com.example.service_warranty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ServiceWarrantyApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceWarrantyApplication.class, args);
    }
}