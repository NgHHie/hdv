# Dịch Vụ Kỹ Thuật Viên (Technician Service API)

Microservice này quản lý thông tin về các kỹ thuật viên của hệ thống bảo hành và sửa chữa thiết bị điện tử.

## Tính Năng

- Quản lý thông tin kỹ thuật viên
- Theo dõi chuyên môn và kinh nghiệm
- Quản lý trạng thái hoạt động 
- Cung cấp API để tìm kiếm kỹ thuật viên phù hợp cho công việc sửa chữa

## Các Điểm Cuối API (API Endpoints)

### Quản Lý Kỹ Thuật Viên

- **Tạo Kỹ Thuật Viên Mới**: `POST /api/v1/technicians`
  - Request Body: Đối tượng `TechnicianRequest` dạng JSON
  - Response: Trả về đối tượng `TechnicianResponse` và mã trạng thái 201 (Created)

- **Lấy Thông Tin Kỹ Thuật Viên Theo ID**: `GET /api/v1/technicians/{id}`
  - Path Variable: `id` (Integer)
  - Response: Trả về đối tượng `TechnicianResponse` nếu tìm thấy, hoặc 404 nếu không tìm thấy

- **Lấy Thông Tin Kỹ Thuật Viên Theo Email**: `GET /api/v1/technicians/email/{email}`
  - Path Variable: `email` (String)
  - Response: Trả về đối tượng `TechnicianResponse` nếu tìm thấy, hoặc 404 nếu không tìm thấy

- **Lấy Tất Cả Kỹ Thuật Viên**: `GET /api/v1/technicians`
  - Response: Trả về danh sách các đối tượng `TechnicianResponse`

- **Lấy Tất Cả Kỹ Thuật Viên Đang Hoạt Động**: `GET /api/v1/technicians/active`
  - Response: Trả về danh sách các đối tượng `TechnicianResponse` có trạng thái active = true

- **Lấy Kỹ Thuật Viên Theo Chuyên Môn**: `GET /api/v1/technicians/specialization/{specialization}`
  - Path Variable: `specialization` (String)
  - Response: Trả về danh sách các đối tượng `TechnicianResponse` với chuyên môn tương ứng

- **Cập Nhật Thông Tin Kỹ Thuật Viên**: `PUT /api/v1/technicians/{id}`
  - Path Variable: `id` (Integer)
  - Request Body: Đối tượng `TechnicianRequest` dạng JSON
  - Response: Trả về đối tượng `TechnicianResponse` đã cập nhật

- **Xóa Kỹ Thuật Viên**: `DELETE /api/v1/technicians/{id}`
  - Path Variable: `id` (Integer)
  - Response: Trả về trạng thái 204 (No Content) nếu thành công

- **Thay Đổi Trạng Thái Hoạt Động Của Kỹ Thuật Viên**: `PATCH /api/v1/technicians/{id}/toggle-status`
  - Path Variable: `id` (Integer)
  - Response: Trả về đối tượng `TechnicianResponse` đã cập nhật

## Cấu Trúc Dữ Liệu

### Kỹ Thuật Viên (Technician)

```json
{
  "id": "Integer",
  "name": "String",
  "email": "String",
  "phone": "String",
  "specialization": "String",
  "yearsOfExperience": "Integer",
  "isActive": "Boolean",
  "createdAt": "LocalDateTime",
  "updatedAt": "LocalDateTime"
}
```

### Yêu Cầu Tạo/Cập Nhật Kỹ Thuật Viên (TechnicianRequest)

```json
{
  "name": "String",
  "email": "String",
  "phone": "String",
  "specialization": "String",
  "yearsOfExperience": "Integer",
  "isActive": "Boolean"
}
```

## Tích Hợp Với Các Dịch Vụ Khác

Dịch vụ này được sử dụng bởi:
- **Dịch Vụ Sửa Chữa**: Để tìm và phân công kỹ thuật viên phù hợp cho các công việc sửa chữa
- **Dịch Vụ Bảo Hành**: Để hiển thị thông tin kỹ thuật viên trong chi tiết bảo hành

## Cơ Sở Dữ Liệu

Dịch vụ sử dụng MySQL để lưu trữ dữ liệu kỹ thuật viên. Script khởi tạo cơ sở dữ liệu có sẵn trong thư mục `init-scripts`.

## Cài Đặt và Chạy

```bash
# Build service
mvn clean package

# Chạy service
java -jar target/service-technician-1.0-SNAPSHOT.jar

# Hoặc sử dụng Docker
docker build -t service-technician .
docker run -p 8086:8086 service-technician
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