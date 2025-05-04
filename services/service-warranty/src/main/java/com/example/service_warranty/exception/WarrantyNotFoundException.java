package com.example.service_warranty.exception;

public class WarrantyNotFoundException extends RuntimeException {
    public WarrantyNotFoundException(String message) {
        super(message);
    }
}