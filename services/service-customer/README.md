# Dịch Vụ Khách Hàng (Customer Service)

## Giới Thiệu

Dịch vụ khách hàng quản lý thông tin khách hàng, lịch sử mua hàng và yêu cầu bảo hành. Đây là điểm truy cập đầu tiên cho khách hàng khi muốn đăng ký thông tin cá nhân, xem lịch sử mua hàng và tạo yêu cầu bảo hành.

## Tính Năng Chính

- Đăng ký và quản lý hồ sơ khách hàng
- Theo dõi lịch sử mua hàng
- Tạo yêu cầu bảo hành ban đầu
- Lưu trữ lịch sử yêu cầu bảo hành
- Tích hợp với các dịch vụ khác để cung cấp thông tin khách hàng

## Các Điểm Cuối API (API Endpoints)

### Quản Lý Khách Hàng

- **Tạo khách hàng mới**
  - **Method:** POST
  - **Endpoint:** `/api/v1/customers`
  - **Request Body:** Đối tượng `Customer` dạng JSON
  - **Response:** Trả về đối tượng `Customer` và mã trạng thái 201 (Created)

- **Lấy danh sách tất cả khách hàng**
  - **Method:** GET
  - **Endpoint:** `/api/v1/customers`
  - **Response:** Trả về danh sách các đối tượng `Customer`

- **Cập nhật thông tin khách hàng**
  - **Method:** PUT
  - **Endpoint:** `/api/v1/customers/{id}`
  - **Path Variable:** `id` (Integer)
  - **Request Body:** Đối tượng `Customer` dạng JSON
  - **Response:** Trả về đối tượng `Customer` đã cập nhật, hoặc 400 nếu có lỗi

- **Xóa khách hàng**
  - **Method:** DELETE
  - **Endpoint:** `/api/v1/customers/{id}`
  - **Path Variable:** `id` (Integer)
  - **Response:** Trả về trạng thái 204 (No Content) nếu thành công

- **Lấy thông tin khách hàng theo ID**
  - **Method:** GET
  - **Endpoint:** `/api/v1/customers/{id}`
  - **Path Variable:** `id` (Integer)
  - **Response:** Trả về đối tượng `Customer` nếu tìm thấy, hoặc 404 nếu không tìm thấy

- **Lấy thông tin khách hàng theo email**
  - **Method:** GET
  - **Endpoint:** `/api/v1/customers/email`
  - **Query Parameter:** `email` (String)
  - **Response:** Trả về đối tượng `Customer` nếu tìm thấy, hoặc 404 nếu không tìm thấy

### Quản Lý Mua Hàng

- **Lấy tất cả các đơn mua hàng của khách hàng**
  - **Method:** GET
  - **Endpoint:** `/api/v1/customers/{customerId}/purchase`
  - **Path Variable:** `customerId` (Integer)
  - **Response:** Trả về danh sách các đối tượng `PurchaseDTO` của khách hàng

- **Lấy chi tiết đơn mua hàng theo ID**
  - **Method:** GET
  - **Endpoint:** `/api/v1/customers/{customerId}/purchase/{purchaseId}`
  - **Path Variables:**
    - `customerId` (Integer)
    - `purchaseId` (Integer)
  - **Response:** Trả về đối tượng `PurchaseDTO` chi tiết của đơn hàng

- **Lấy đơn mua hàng theo ID sản phẩm**
  - **Method:** GET
  - **Endpoint:** `/api/v1/customers/purchase/{productId}`
  - **Path Variable:** `productId` (Integer)
  - **Response:** Trả về đối tượng `PurchaseDTO` chứa sản phẩm có ID tương ứng

### Quản Lý Bảo Hành

- **Tạo yêu cầu bảo hành**
  - **Method:** POST
  - **Endpoint:** `/api/v1/customers/warranty/requests`
  - **Request Body:** Đối tượng `WarrantyRequestDTO` dạng JSON
  - **Response:** Trả về đối tượng `WarrantyRequestDTO` và mã trạng thái 201 (Created)

- **Lấy yêu cầu bảo hành theo ID**
  - **Method:** GET
  - **Endpoint:** `/api/v1/customers/warranty/requests/{id}`
  - **Path Variable:** `id` (Integer)
  - **Response:** Trả về đối tượng `WarrantyRequestDTO` nếu tìm thấy, hoặc 404 nếu không tìm thấy

- **Lấy yêu cầu bảo hành theo ID khách hàng**
  - **Method:** GET
  - **Endpoint:** `/api/v1/customers/warranty/requests/customer/{customerId}`
  - **Path Variable:** `customerId` (Integer)
  - **Response:** Trả về danh sách các đối tượng `WarrantyRequestDTO` của khách hàng

- **Lấy yêu cầu bảo hành theo trạng thái**
  - **Method:** GET
  - **Endpoint:** `/api/v1/customers/warranty/requests/status/{status}`
  - **Path Variable:** `status` (String)
  - **Response:** Trả về danh sách các đối tượng `WarrantyRequestDTO` có trạng thái tương ứng

- **Cập nhật trạng thái yêu cầu bảo hành**
  - **Method:** PUT
  - **Endpoint:** `/api/v1/customers/warranty/requests/{id}/status`
  - **Path Variable:** `id` (Integer)
  - **Request Body:** Map chứa các trường `status`, `notes`, `performedBy`
  - **Response:** Trả về đối tượng `WarrantyRequestDTO` đã cập nhật

- **Cập nhật ID sửa chữa cho yêu cầu bảo hành**
  - **Method:** PUT
  - **Endpoint:** `/api/v1/customers/warranty/requests/{id}/repair`
  - **Path Variable:** `id` (Integer)
  - **Request Body:** Đối tượng chứa trường `repairId`
  - **Response:** Trả về đối tượng `WarrantyRequestDTO` đã cập nhật

- **Lấy lịch sử yêu cầu bảo hành**
  - **Method:** GET
  - **Endpoint:** `/api/v1/customers/warranty/requests/{id}/history`
  - **Path Variable:** `id` (Integer)
  - **Response:** Trả về danh sách các đối tượng `WarrantyHistoryDTO`

## Cài Đặt và Chạy

```bash
# Build service
mvn clean package

# Chạy service
java -jar target/service-customer-1.0-SNAPSHOT.jar

# Hoặc sử dụng Docker
docker build -t service-customer .
docker run -p 8081:8081 service-customer
```

## Cấu Hình

Các cấu hình có thể được điều chỉnh trong file `application.properties`. Service này sử dụng các biến môi trường cho cấu hình như:

- `SERVER_PORT`: Port để chạy service
- `SPRING_APPLICATION_NAME`: Tên ứng dụng
- `MYSQL_HOST`: Host của MySQL
- `MYSQL_PORT`: Port của MySQL
- `MYSQL_DATABASE`: Tên database
- `MYSQL_USERNAME`: Username MySQL
- `MYSQL_PASSWORD`: Password MySQL
- `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE`: URL của Eureka server

## Tích Hợp Với Các Service Khác

Service Customer được sử dụng bởi:
- Service Warranty: Để xác minh thông tin khách hàng và lưu trữ yêu cầu bảo hành
- Service Notification: Để gửi thông báo đến khách hàng
- Service Repair: Để lấy thông tin khách hàng cho các yêu cầu sửa chữa