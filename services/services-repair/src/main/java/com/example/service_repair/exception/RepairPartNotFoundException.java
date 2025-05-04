package com.example.service_repair.exception;

public class RepairPartNotFoundException extends RuntimeException {
    public RepairPartNotFoundException(String message) {
        super(message);
    }
}