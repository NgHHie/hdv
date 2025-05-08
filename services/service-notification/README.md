# Dịch Vụ Thông Báo (Notification Service API)

Microservice này quản lý việc gửi thông báo đến khách hàng về trạng thái bảo hành, tiến độ sửa chữa và các sự kiện quan trọng trong hệ thống.

## Tính Năng

- Gửi email thông báo đến khách hàng
- Quản lý các mẫu thông báo theo loại
- Tiếp nhận sự kiện từ Kafka và xử lý thông báo
- Lưu trữ lịch sử thông báo
- Cung cấp API để gửi thông báo theo yêu cầu

## Các Điểm Cuối API (API Endpoints)

### Quản Lý Thông Báo

- **Lấy Thông Báo Theo ID Khách Hàng**: `GET /api/v1/notifications/customer/{customerId}`
  - Path Variable: `customerId` (Integer)
  - Response: Trả về danh sách các đối tượng `NotificationResponseDto`

- **Lấy Thông Báo Theo ID Entity Liên Quan**: `GET /api/v1/notifications/related/{relatedEntityId}`
  - Path Variable: `relatedEntityId` (Integer)
  - Response: Trả về danh sách các đối tượng `NotificationResponseDto`

### Mẫu Thông Báo (Templates)

Dịch vụ hỗ trợ nhiều loại thông báo khác nhau, bao gồm:
- `WARRANTY_RECEIVED`: Xác nhận tiếp nhận yêu cầu bảo hành
- `WARRANTY_REJECTED`: Từ chối yêu cầu bảo hành
- `WARRANTY_APPROVED`: Phê duyệt yêu cầu bảo hành
- `PRODUCT_RECEIVED`: Xác nhận đã nhận sản phẩm
- `DIAGNOSIS_STARTED`: Đã bắt đầu chẩn đoán
- `REPAIR_IN_PROGRESS`: Đang trong quá trình sửa chữa
- `REPAIR_COMPLETED`: Sửa chữa hoàn tất
- `PRODUCT_SHIPPING`: Sản phẩm đang được vận chuyển
- `PRODUCT_DELIVERED`: Sản phẩm đã được giao
- `FEEDBACK_REQUEST`: Yêu cầu phản hồi về dịch vụ

## Kafka Consumer

Dịch vụ này đăng ký lắng nghe sự kiện thông báo từ Kafka và tự động xử lý:
- Topic: `warranty-notifications`
- Consumer Group: `notification-group`

## Cấu Trúc Dữ Liệu

### Thông Báo (Notification)

```json
{
  "id": "Integer",
  "customerId": "Integer",
  "type": "NotificationType",
  "warrantyRequestId": "Integer",
  "email": "String",
  "subject": "String",
  "content": "String",
  "status": "NotificationStatus",
  "createdAt": "LocalDateTime",
  "sentAt": "LocalDateTime"
}
```

### Mẫu Thông Báo (NotificationTemplate)

```json
{
  "id": "Integer",
  "type": "NotificationType",
  "subject": "String",
  "contentTemplate": "String",
  "isActive": "Boolean"
}
```

### Sự Kiện Thông Báo (WarrantyNotificationEvent)

```json
{
  "warrantyRequestId": "Integer",
  "email": "String",
  "productName": "String",
  "customerId": "Integer",
  "type": "NotificationType",
  "message": "String",
  "customerName": "String"
}
```

## Tích Hợp Với Các Dịch Vụ Khác

Dịch vụ này được sử dụng bởi:
- **Dịch Vụ Bảo Hành**: Gửi thông báo về trạng thái bảo hành
- **Dịch Vụ Sửa Chữa**: Gửi thông báo về tiến độ sửa chữa
- **Dịch Vụ Khách Hàng**: Để lấy thông tin khách hàng cho gửi thông báo

## Email Configuration

Dịch vụ sử dụng SMTP để gửi email. Cấu hình được thiết lập trong `application.properties`.

## Cài Đặt và Chạy

```bash
# Build service
mvn clean package

# Chạy service
java -jar target/service-notification-1.0-SNAPSHOT.jar

# Hoặc sử dụng Docker
docker build -t service-notification .
docker run -p 8085:8085 service-notification
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
- `SPRING_MAIL_HOST`: SMTP server
- `SPRING_MAIL_PORT`: SMTP port
- `SPRING_MAIL_USERNAME`: Email username
- `SPRING_MAIL_PASSWORD`: Email password
- `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE`: URL của Eureka server
- `SPRING_KAFKA_BOOTSTRAP_SERVERS`: Địa chỉ máy chủ Kafka
- `SPRING_KAFKA_CONSUMER_GROUP_ID`: ID của consumer group
- `SERVICE_CUSTOMER_URL`: URL của Customer Service