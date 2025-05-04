package com.example.service_repair.constants;


public enum RepairStatus {
    SUBMITTED,       // Yêu cầu đã được gửi
    RECEIVED,        // Sản phẩm đã được tiếp nhận
    UNDER_DIAGNOSIS, // Đang kiểm tra sản phẩm
    DIAGNOSING,      // Chẩn đoán lỗi
    WAITING_APPROVAL,// Chờ khách hàng xác nhận sửa chữa (khi hết bảo hành)
    REPAIRING,       // Đang sửa chữa
    REPAIRED,        // Đã sửa chữa xong
    TESTING,         // Kiểm tra sau sửa chữa
    COMPLETED,       // Sửa chữa hoàn tất
    DELIVERING,      // Đang gửi trả sản phẩm
    DELIVERED,       // Đã gửi trả sản phẩm
    CANCELLED,       // Yêu cầu đã bị hủy
    REJECTED         // Từ chối bảo hành
}