# Dịch Vụ Sản Phẩm (Product Service API)

Microservice này quản lý thông tin về các sản phẩm điện tử, bao gồm chi tiết sản phẩm, giá cả, tồn kho và các thông tin liên quan đến bảo hành.

## Tính Năng

- Quản lý danh mục sản phẩm
- Theo dõi tồn kho và giá cả
- Cung cấp thông tin chi tiết sản phẩm cho các dịch vụ khác
- Hỗ trợ tìm kiếm và lọc sản phẩm

## Các Điểm Cuối API (API Endpoints)

### Quản Lý Sản Phẩm

- **Tạo Sản Phẩm Mới**: `POST /api/v1/products`
  - Request Body: Đối tượng `ProductRequest` dạng JSON
  - Response: Trả về đối tượng `ProductResponse` và mã trạng thái 201 (Created)

- **Lấy Tất Cả Sản Phẩm**: `GET /api/v1/products`
  - Response: Trả về danh sách các đối tượng `ProductResponse`

- **Lấy Sản Phẩm Theo ID**: `GET /api/v1/products/{id}`
  - Path Variable: `id` (Integer)
  - Response: Trả về đối tượng `ProductResponse` nếu tìm thấy, hoặc 404 nếu không tìm thấy

- **Cập Nhật Sản Phẩm**: `PUT /api/v1/products/{id}`
  - Path Variable: `id` (Integer)
  - Request Body: Đối tượng `ProductRequest` dạng JSON
  - Response: Trả về đối tượng `ProductResponse` đã cập nhật

- **Xóa Sản Phẩm**: `DELETE /api/v1/products/{id}`
  - Path Variable: `id` (Integer)
  - Response: Trả về trạng thái 204 (No Content) nếu thành công

### Tìm Kiếm và Lọc

- **Lấy Sản Phẩm Theo Số Serial**: `GET /api/v1/products/serial/{serial}`
  - Path Variable: `serial` (String)
  - Response: Trả về đối tượng `ProductResponse` nếu tìm thấy, hoặc 404 nếu không tìm thấy

- **Lấy Sản Phẩm Theo Danh Mục**: `GET /api/v1/products/category/{category}`
  - Path Variable: `category` (String)
  - Response: Trả về danh sách các đối tượng `ProductResponse` thuộc danh mục

- **Tìm Sản Phẩm Theo Tên**: `GET /api/v1/products/search?keyword={keyword}`
  - Query Parameter: `keyword` (String)
  - Response: Trả về danh sách các đối tượng `ProductResponse` phù hợp với từ khóa

## Cấu Trúc Dữ Liệu

### Sản Phẩm (Product)

```json
{
  "id": "Integer",
  "name": "String",
  "description": "String",
  "price": "BigDecimal",
  "quantity": "Integer",
  "category": "String",
  "warrantyDuration": "Float",
  "serialNumber": "String",
  "createdAt": "LocalDateTime",
  "updatedAt": "LocalDateTime"
}
```

## Tích Hợp Với Các Dịch Vụ Khác

Dịch vụ này được sử dụng bởi:

- **Dịch Vụ Khách Hàng**: Để hiển thị thông tin sản phẩm cho khách hàng
- **Dịch Vụ Bảo Hành**: Để xác minh thông tin sản phẩm và thời hạn bảo hành
- **Dịch Vụ Sửa Chữa**: Để cung cấp thông tin chi tiết về sản phẩm cần sửa chữa

## Cơ Sở Dữ Liệu

Dịch vụ sử dụng MySQL để lưu trữ dữ liệu sản phẩm. Script khởi tạo cơ sở dữ liệu có sẵn trong thư mục `init-scripts`.

## Cài Đặt và Chạy

```bash
# Build service
mvn clean package

# Chạy service
java -jar target/service-product-1.0-SNAPSHOT.jar

# Hoặc sử dụng Docker
docker build -t service-product .
docker run -p 8083:8083 service-product
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