# Customer Service API Endpoints

## Base URL: `/api/v1/customers`

### 1. Tạo khách hàng mới

- **Method:** POST
- **Endpoint:** `/api/v1/customers`
- **Request Body:** Đối tượng `Customer` dạng JSON
- **Response:** Trả về đối tượng `Customer` và mã trạng thái 201 (Created)

### 2. Lấy danh sách tất cả khách hàng

- **Method:** GET
- **Endpoint:** `/api/v1/customers`
- **Response:** Trả về danh sách các đối tượng `Customer`

### 3. Cập nhật thông tin khách hàng

- **Method:** PUT
- **Endpoint:** `/api/v1/customers/{id}`
- **Path Variable:** `id` (Integer)
- **Request Body:** Đối tượng `Customer` dạng JSON
- **Response:** Trả về đối tượng `Customer` đã cập nhật, hoặc 400 nếu có lỗi

### 4. Xóa khách hàng

- **Method:** DELETE
- **Endpoint:** `/api/v1/customers/{id}`
- **Path Variable:** `id` (Integer)
- **Response:** Trả về trạng thái 204 (No Content) nếu thành công

### 5. Lấy thông tin khách hàng theo ID

- **Method:** GET
- **Endpoint:** `/api/v1/customers/{id}`
- **Path Variable:** `id` (Integer)
- **Response:** Trả về đối tượng `Customer` nếu tìm thấy, hoặc 404 nếu không tìm thấy

### 6. Lấy thông tin khách hàng theo email

- **Method:** GET
- **Endpoint:** `/api/v1/customers/email`
- **Query Parameter:** `email` (String)
- **Response:** Trả về đối tượng `Customer` nếu tìm thấy, hoặc 404 nếu không tìm thấy

### 7. Lấy tất cả các đơn mua hàng của khách hàng

- **Method:** GET
- **Endpoint:** `/api/v1/customers/{customerId}/purchase`
- **Path Variable:** `customerId` (Integer)
- **Response:** Trả về danh sách các đối tượng `PurchaseDTO` của khách hàng

### 8. Lấy chi tiết đơn mua hàng theo ID

- **Method:** GET
- **Endpoint:** `/api/v1/customers/{customerId}/purchase/{purchaseId}`
- **Path Variables:**
    - `customerId` (Integer)
    - `purchaseId` (Integer)
- **Response:** Trả về đối tượng `PurchaseDTO` chi tiết của đơn hàng