package com.example.service_product.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class HelloController {
    @GetMapping("/hello")
    public String getMethodName() {
        return new String("hello product service");
    }
    
}
