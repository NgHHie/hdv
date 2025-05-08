# Dịch Vụ Sửa Chữa (Repair Service API)

Microservice này quản lý các yêu cầu sửa chữa và quy trình sửa chữa sản phẩm điện tử, bao gồm theo dõi trạng thái, phân công kỹ thuật viên và quản lý linh kiện.

## Tính Năng

- Quản lý yêu cầu sửa chữa
- Phân công kỹ thuật viên
- Cập nhật trạng thái sửa chữa
- Quản lý linh kiện và phụ tùng
- Quy trình sửa chữa từng bước với chuyển đổi trạng thái

## Các Điểm Cuối API (API Endpoints)

### Quản Lý Yêu Cầu Sửa Chữa

- **Tạo Yêu Cầu Sửa Chữa Mới**: `POST /api/v1/repairs`
  - Request Body: Đối tượng `RepairRequestDto` dạng JSON
  - Response: Trả về đối tượng `RepairRequestResponseDto` và mã trạng thái 201 (Created)

- **Lấy Yêu Cầu Sửa Chữa Theo ID**: `GET /api/v1/repairs/{id}`
  - Path Variable: `id` (Integer)
  - Response: Trả về đối tượng `RepairRequestResponseDto` nếu tìm thấy, hoặc 404 nếu không tìm thấy

- **Lấy Yêu Cầu Sửa Chữa Theo ID Khách Hàng**: `GET /api/v1/repairs/customer/{customerId}`
  - Path Variable: `customerId` (Integer)
  - Response: Trả về danh sách các đối tượng `RepairRequestResponseDto` của khách hàng

- **Lấy Yêu Cầu Sửa Chữa Theo Trạng Thái**: `GET /api/v1/repairs/status/{status}`
  - Path Variable: `status` (String)
  - Response: Trả về danh sách các đối tượng `RepairRequestResponseDto` có trạng thái tương ứng

### Quy Trình Sửa Chữa

- **Tiếp Nhận Sản Phẩm**: `POST /api/v1/repairs/{id}/receive`
  - Path Variable: `id` (Integer)
  - Request Body: Đối tượng `TechnicianDto` dạng JSON
  - Response: Trả về đối tượng `RepairRequestResponseDto` đã cập nhật

- **Bắt Đầu Chẩn Đoán**: `POST /api/v1/repairs/{id}/start-diagnosis`
  - Path Variable: `id` (Integer)
  - Response: Trả về đối tượng `RepairRequestResponseDto` đã cập nhật

- **Hoàn Thành Chẩn Đoán Và Bắt Đầu Sửa Chữa**: `POST /api/v1/repairs/{id}/complete-diagnosis`
  - Path Variable: `id` (Integer)
  - Request Body: Đối tượng `DiagnosisDto` dạng JSON
  - Response: Trả về đối tượng `RepairRequestResponseDto` đã cập nhật

- **Thêm Linh Kiện Sửa Chữa**: `POST /api/v1/repairs/{id}/parts`
  - Path Variable: `id` (Integer)
  - Request Body: Đối tượng `RepairPartDto` dạng JSON
  - Response: Trả về đối tượng `RepairPartDto` đã thêm

- **Hoàn Thành Sửa Chữa**: `POST /api/v1/repairs/{id}/complete-repair`
  - Path Variable: `id` (Integer)
  - Request Body: Đối tượng `RepairCompletionDto` dạng JSON
  - Response: Trả về đối tượng `RepairRequestResponseDto` đã cập nhật

### Mô Hình Trạng Thái

Dịch vụ sửa chữa sử dụng mô hình State để quản lý các trạng thái khác nhau của quy trình sửa chữa:

1. **SUBMITTED**: Yêu cầu đã được gửi và đang chờ xử lý
2. **RECEIVED**: Sản phẩm đã được tiếp nhận để sửa chữa
3. **DIAGNOSING**: Đang trong quá trình chẩn đoán lỗi
4. **REPAIRING**: Đang trong quá trình sửa chữa
5. **COMPLETED**: Sửa chữa đã hoàn tất

## Cấu Trúc Dữ Liệu

### Yêu Cầu Sửa Chữa (RepairRequest)

```json
{
  "id": "Integer",
  "warrantyId": "Integer",
  "customerId": "Integer",
  "productId": "Integer",
  "issueDescription": "String",
  "imageUrls": "String",
  "status": "RepairStatus",
  "repairNotes": "String",
  "technicianId": "Integer",
  "startDate": "LocalDateTime",
  "endDate": "LocalDateTime",
  "createdAt": "LocalDateTime",
  "updatedAt": "LocalDateTime",
  "repairCost": "BigDecimal",
  "withinWarranty": "Boolean"
}
```

## Tích Hợp Với Các Dịch Vụ Khác

Dịch vụ này tích hợp với:

- **Dịch Vụ Bảo Hành**: Để kiểm tra tình trạng bảo hành và cập nhật tiến độ sửa chữa
- **Dịch Vụ Kỹ Thuật Viên**: Để lấy thông tin kỹ thuật viên được phân công
- **Dịch Vụ Sản Phẩm**: Để lấy thông tin chi tiết về sản phẩm cần sửa chữa
- **Dịch Vụ Thông Báo**: Để gửi thông báo về tiến độ sửa chữa cho khách hàng

## Cơ Sở Dữ Liệu

Dịch vụ sử dụng MySQL để lưu trữ dữ liệu sửa chữa và trạng thái. Schema bao gồm các bảng cho yêu cầu sửa chữa, lịch sử trạng thái, linh kiện đã sử dụng và các hành động sửa chữa.

## Cài Đặt và Chạy

```bash
# Build service
mvn clean package

# Chạy service
java -jar target/service-repair-1.0-SNAPSHOT.jar

# Hoặc sử dụng Docker
docker build -t service-repair .
docker run -p 8084:8084 service-repair
```

## Cấu Hình

Các cấu hình có thể được điều chỉnh trong file `application.properties`. Service này sử dụng các biến môi trường cho cấu hình:

- `SERVER_PORT`: Port để chạy service
- `SPRING_APPLICATION_NAME`: Tên ứng dụng
- `MYSQL_HOST`: Host của MySQL
- `MYSQL_PORT`: Port của MySQL
- `MYSQL_DATABASE`: Tên database
- `MYSQL_USERNAME`: Username MySQL
- `MYSQL_PASSWORD`: Password MySQL
- `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE`: URL của Eureka server
- `SERVICE_NOTIFICATION_URL`: URL của Notification Service
- `SERVICE_WARRANTY_URL`: URL của Warranty Service
- `SERVICE_PRODUCT_URL`: URL của Product Service