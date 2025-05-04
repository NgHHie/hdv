package com.example.service_warranty.exception;

public class WarrantyRequestNotFoundException extends RuntimeException {
    public WarrantyRequestNotFoundException(String message) {
        super(message);
    }
}