package com.example.service_repair.exception;

public class RepairRequestNotFoundException extends RuntimeException {
    public RepairRequestNotFoundException(String message) {
        super(message);
    }
}