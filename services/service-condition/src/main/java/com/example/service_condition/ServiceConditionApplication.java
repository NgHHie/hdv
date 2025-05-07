package com.example.service_condition;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ServiceConditionApplication {
    public static void main(String[] args) {
		SpringApplication.run(ServiceConditionApplication.class, args);
	}

}


