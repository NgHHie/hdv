package com.example.service_survey;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ServiceSurveyApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceSurveyApplication.class, args);
    }
}