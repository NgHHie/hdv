# Dịch Vụ Điều Kiện Bảo Hành (Condition Service API)

Microservice này quản lý các điều kiện bảo hành và xác thực yêu cầu bảo hành đối với các điều kiện này.

## Tính Năng

- Định nghĩa và quản lý các điều kiện bảo hành
- Đánh giá sản phẩm dựa trên các điều kiện bảo hành
- Lưu trữ kết quả xác thực
- Hỗ trợ quy trình xác thực nhiều bước

## Các Điểm Cuối API (API Endpoints)

### Quản Lý Điều Kiện Bảo Hành

- **Lấy Tất Cả Điều Kiện Bảo Hành**: `GET /api/v1/conditions`
  - Response: Trả về danh sách các đối tượng `WarrantyConditionDTO`

- **Lấy Tất Cả Điều Kiện Bảo Hành Đang Hoạt Động**: `GET /api/v1/conditions/active`
  - Response: Trả về danh sách các đối tượng `WarrantyConditionDTO` có trạng thái active = true

- **Lấy Điều Kiện Bảo Hành Theo ID**: `GET /api/v1/conditions/{id}`
  - Path Variable: `id` (Integer)
  - Response: Trả về đối tượng `WarrantyConditionDTO` nếu tìm thấy, hoặc 404 nếu không tìm thấy

- **Tạo Điều Kiện Bảo Hành Mới**: `POST /api/v1/conditions`
  - Request Body: Đối tượng `WarrantyConditionDTO` dạng JSON
  - Response: Trả về đối tượng `WarrantyConditionDTO` và mã trạng thái 201 (Created)

### Xác Thực Điều Kiện Bảo Hành

- **Xác Thực Điều Kiện Bảo Hành**: `POST /api/v1/conditions/validate`
  - Request Body: Đối tượng `WarrantyValidationDTO` dạng JSON
  - Response: Trả về giá trị Boolean (true/false) xác định yêu cầu có hợp lệ hay không

- **Lấy Kết Quả Xác Thực Theo ID Yêu Cầu Bảo Hành**: `GET /api/v1/conditions/results/{warrantyRequestId}`
  - Path Variable: `warrantyRequestId` (Integer)
  - Response: Trả về danh sách các đối tượng `WarrantyConditionResultDTO`

## Cấu Trúc Dữ Liệu

### Điều Kiện Bảo Hành (WarrantyCondition)

```json
{
  "id": "Integer",
  "name": "String",
  "description": "String",
  "isActive": "Boolean"
}
```

### Yêu Cầu Xác Thực Bảo Hành (WarrantyValidationDTO)

```json
{
  "warrantyRequestId": "Integer",
  "isValid": "Boolean",
  "validationReason": "String",
  "validatedBy": "String",
  "conditionResults": [
    {
      "conditionId": "Integer",
      "passed": "Boolean",
      "notes": "String"
    }
  ]
}
```

### Kết Quả Xác Thực Điều Kiện (WarrantyConditionResult)

```json
{
  "id": "Integer",
  "warrantyRequestId": "Integer",
  "condition": {
    "id": "Integer",
    "name": "String",
    "description": "String",
    "isActive": "Boolean"
  },
  "passed": "Boolean",
  "notes": "String",
  "evaluatedBy": "String",
  "evaluatedAt": "LocalDateTime"
}
```

## Điều Kiện Bảo Hành Mặc Định

Service này bao gồm các điều kiện bảo hành mặc định:
1. **Không Có Hư Hỏng Vật Lý**: Sản phẩm không có dấu hiệu hư hỏng vật lý như vết nứt, vết lõm hoặc bộ phận bị vỡ
2. **Không Có Hư Hỏng Do Nước**: Sản phẩm không có dấu hiệu hư hỏng do chất lỏng hoặc tiếp xúc với nước
3. **Không Có Sửa Chữa Trái Phép**: Sản phẩm chưa được mở hoặc sửa chữa bởi nhân viên không được ủy quyền
4. **Sử Dụng Theo Hướng Dẫn**: Sản phẩm đã được sử dụng theo đúng hướng dẫn sử dụng
5. **Linh Kiện Gốc**: Tất cả các linh kiện đều là linh kiện gốc và không có linh kiện thay thế của bên thứ ba nào được sử dụng

## Tích Hợp Với Các Dịch Vụ Khác

Dịch vụ này được sử dụng bởi:
- **Dịch Vụ Bảo Hành**: Để xác thực yêu cầu bảo hành theo các điều kiện

## Cơ Sở Dữ Liệu

Dịch vụ sử dụng MySQL để lưu trữ các điều kiện bảo hành và kết quả xác thực. Script khởi tạo cơ sở dữ liệu có sẵn trong thư mục `init-scripts`.

## Cài Đặt và Chạy

```bash
# Build service
mvn clean package

# Chạy service
java -jar target/service-condition-1.0-SNAPSHOT.jar

# Hoặc sử dụng Docker
docker build -t service-condition .
docker run -p 8087:8087 service-condition
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