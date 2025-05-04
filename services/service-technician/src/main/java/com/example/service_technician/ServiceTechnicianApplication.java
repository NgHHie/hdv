package com.example.service_technician;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ServiceTechnicianApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceTechnicianApplication.class, args);
    }
}