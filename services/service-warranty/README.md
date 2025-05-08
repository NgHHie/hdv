# Dịch Vụ Bảo Hành (Warranty Service API)

Microservice này đóng vai trò như một dịch vụ phối hợp (orchestrator) quản lý quy trình bảo hành từ đầu đến cuối, kết nối các dịch vụ khác và điều phối quy trình bảo hành sản phẩm.

## Tính Năng

- Xác minh tính đủ điều kiện bảo hành
- Điều phối giữa các dịch vụ để xử lý bảo hành
- Theo dõi và quản lý trạng thái bảo hành
- Gửi thông báo về các thay đổi trạng thái bảo hành
- Cung cấp thông tin chi tiết về bảo hành

## Các Điểm Cuối API (API Endpoints)

### Quản Lý Yêu Cầu Bảo Hành

- **Tạo Yêu Cầu Bảo Hành Mới**: `POST /api/v1/warranty/requests`
  - Request Body: Đối tượng `WarrantyRequestCreateDto` dạng JSON
  - Response: Trả về đối tượng `WarrantyRequestDto` và mã trạng thái 201 (Created)

- **Lấy Yêu Cầu Bảo Hành Theo ID**: `GET /api/v1/warranty/requests/{id}`
  - Path Variable: `id` (Integer)
  - Response: Trả về đối tượng `WarrantyRequestDto` nếu tìm thấy, hoặc 404 nếu không tìm thấy

- **Lấy Thông Tin Chi Tiết Bảo Hành**: `GET /api/v1/warranty/requests/{id}/details`
  - Path Variable: `id` (Integer)
  - Response: Trả về đối tượng `WarrantyDetailDto` với thông tin đầy đủ

### Xác Thực và Xử Lý Bảo Hành

- **Xác Thực Yêu Cầu Bảo Hành**: `POST /api/v1/warranty/requests/{id}/validate`
  - Path Variable: `id` (Integer)
  - Request Body: Đối tượng `WarrantyValidationDto` dạng JSON
  - Response: Trả về đối tượng `WarrantyRequestDto` đã cập nhật

- **Tiếp Nhận Sản Phẩm và Chuyển Đến Sửa Chữa**: `POST /api/v1/warranty/requests/{id}/receive-and-forward`
  - Path Variable: `id` (Integer)
  - Request Body: Object chứa trường `notes`
  - Response: Trả về đối tượng `WarrantyRequestDto` đã cập nhật

- **Cập Nhật Trạng Thái Sửa Chữa**: `PUT /api/v1/warranty/requests/{id}/update-repair-status`
  - Path Variable: `id` (Integer)
  - Request Body: Object chứa các trường `status` và `notes`
  - Response: Trả về đối tượng `WarrantyRequestDto` đã cập nhật

- **Xác Nhận Giao Sản Phẩm**: `POST /api/v1/warranty/requests/{id}/confirm-delivery`
  - Path Variable: `id` (Integer)
  - Request Body: Object chứa trường `notes`
  - Response: Trả về đối tượng `WarrantyRequestDto` đã cập nhật

### Kiểm Tra Trạng Thái Bảo Hành

- **Kiểm Tra Tình Trạng Bảo Hành**: `GET /api/v1/warranty/check`
  - Query Parameters:
    - `productId` (Integer)
    - `customerId` (Integer)
  - Response: Trả về đối tượng `WarrantyStatusResponse`

## Cấu Trúc Dữ Liệu

### Yêu Cầu Bảo Hành (WarrantyRequestDto)

```json
{
  "id": "Integer",
  "customerId": "Integer",
  "customerName": "String",
  "productId": "Integer",
  "productName": "String",
  "serialNumber": "String",
  "issueDescription": "String",
  "imageUrls": "List<String>",
  "status": "String",
  "submissionDate": "LocalDateTime",
  "expirationDate": "LocalDate",
  "validationNotes": "String",
  "repairId": "Integer"
}
```

### Chi Tiết Bảo Hành (WarrantyDetailDto)

```json
{
  "id": "Integer",
  "status": "String",
  "submissionDate": "LocalDateTime",
  "expirationDate": "LocalDate",
  "issueDescription": "String",
  "imageUrls": "List<String>",
  "validationNotes": "String",
  "customerId": "Integer",
  "customerName": "String",
  "customerEmail": "String",
  "productId": "Integer",
  "productName": "String",
  "serialNumber": "String",
  "warrantyDuration": "Float",
  "repairId": "Integer",
  "repairStatus": "String",
  "repairCreatedAt": "LocalDateTime",
  "repairEndDate": "LocalDateTime",
  "technicianId": "Integer",
  "technicianName": "String"
}
```

## Tích Hợp Với Các Dịch Vụ Khác

Dịch vụ này tích hợp với:

- **Dịch Vụ Khách Hàng**: Để lấy thông tin khách hàng và lịch sử mua hàng
- **Dịch Vụ Sản Phẩm**: Để xác minh thông tin sản phẩm và thời hạn bảo hành
- **Dịch Vụ Sửa Chữa**: Để tạo yêu cầu sửa chữa và theo dõi tiến độ
- **Dịch Vụ Điều Kiện Bảo Hành**: Để xác thực các điều kiện bảo hành
- **Dịch Vụ Thông Báo**: Để gửi thông báo tới khách hàng

## Thông Báo Qua Kafka

Dịch vụ này sử dụng Kafka để gửi sự kiện thông báo về các cập nhật trạng thái bảo hành. Các loại thông báo bao gồm:
- Xác nhận đăng ký bảo hành
- Phê duyệt hoặc từ chối bảo hành
- Thông báo tiếp nhận sản phẩm
- Cập nhật tình trạng sửa chữa
- Hoàn thành sửa chữa
- Thông báo giao sản phẩm

## Cài Đặt và Chạy

```bash
# Build service
mvn clean package

# Chạy service
java -jar target/service-warranty-1.0-SNAPSHOT.jar

# Hoặc sử dụng Docker
docker build -t service-warranty .
docker run -p 8086:8086 service-warranty
```

## Cấu Hình

Các cấu hình có thể được điều chỉnh trong file `application.properties`. Service này sử dụng các biến môi trường cho cấu hình:

- `SERVER_PORT`: Port để chạy service
- `SPRING_APPLICATION_NAME`: Tên ứng dụng
- `SERVICE_PRODUCT_URL`: URL của Product Service
- `SERVICE_REPAIR_URL`: URL của Repair Service
- `SERVICE_NOTIFICATION_URL`: URL của Notification Service
- `SERVICE_CUSTOMER_URL`: URL của Customer Service
- `SERVICE_CONDITION_URL`: URL của Condition Service
- `SERVICE_TECHNICIAN_URL`: URL của Technician Service
- `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE`: URL của Eureka server
- `SPRING_KAFKA_BOOTSTRAP_SERVERS`: Địa chỉ máy chủ Kafka