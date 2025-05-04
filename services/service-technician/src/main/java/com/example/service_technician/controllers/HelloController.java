package com.example.service_technician.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello() {
        return new String("hello technician service");
    }
    
}
