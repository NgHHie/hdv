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
- **Lấy Tất Cả Sản Phẩm**: `GET /api/v1/products`
- **Lấy Sản Phẩm Theo ID**: `GET /api/v1/products/{id}`
- **Cập Nhật Sản Phẩm**: `PUT /api/v1/products/{id}`
- **Xóa Sản Phẩm**: `DELETE /api/v1/products/{id}`

### Tìm Kiếm và Lọc

- **Tìm Sản Phẩm Theo Tên**: `GET /api/v1/products/search?keyword={keyword}`
- **Lấy Sản Phẩm Theo Danh Mục**: `GET /api/v1/products/category/{category}`

## Cấu Trúc Dữ Liệu

### Sản Phẩm (Product)

```json
{
  "id": "Long",
  "name": "String",
  "description": "String",
  "price": "BigDecimal",
  "quantity": "Integer",
  "category": "String",
  "warrantyExpiration": "LocalDate",
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

## Xây Dựng và Chạy

```bash
# Xây dựng dịch vụ
mvn clean package

# Chạy dịch vụ
java -jar target/service-product-1.0-SNAPSHOT.jar

# Hoặc sử dụng Docker
docker build -t service-product .
docker run -p 8083:8083 service-product
```

## Cấu Hình

Các cấu hình có thể được điều chỉnh trong file `application.properties`.