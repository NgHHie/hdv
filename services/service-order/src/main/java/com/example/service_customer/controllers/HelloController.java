package com.example.service_customer.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public String getMethodName() {
        return new String("hello customer service");
    }
    
}
