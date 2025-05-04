package com.example.service_repair.exception;

public class InvalidRepairStateException extends RuntimeException {
    public InvalidRepairStateException(String message) {
        super(message);
    }
}