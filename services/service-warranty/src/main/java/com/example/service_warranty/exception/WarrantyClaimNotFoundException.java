package com.example.service_warranty.exception;

public class WarrantyClaimNotFoundException extends RuntimeException {
    public WarrantyClaimNotFoundException(String message) {
        super(message);
    }
}