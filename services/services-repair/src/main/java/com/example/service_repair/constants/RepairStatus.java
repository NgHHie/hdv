package com.example.service_repair.constants;

public enum RepairStatus {
    SUBMITTED,       // Yêu cầu đã được gửi
    RECEIVED,        // Sản phẩm đã được tiếp nhận
    DIAGNOSING,      // Chẩn đoán lỗi
    REPAIRING,       // Đang sửa chữa
    COMPLETED        // Sửa chữa hoàn tất
}