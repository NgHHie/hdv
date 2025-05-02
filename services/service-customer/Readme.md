# Customer API Endpoints

Base URL: `/api/v1/customers`

## Danh sách các endpoint:

### 1. Tạo khách hàng mới

- **Method:** POST
- **Endpoint:** `/api/v1/customers`
- **Request Body:** Đối tượng `Customer` dạng JSON.

---

### 2. Lấy thông tin khách hàng theo ID

- **Method:** GET
- **Endpoint:** `/api/v1/customers/{id}`
- **Path Variable:** `id` (Integer)

---

### 3. Lấy thông tin khách hàng theo email

- **Method:** GET
- **Endpoint:** `/api/v1/customers/email/{email}`
- **Path Variable:** `email` (String)

---

### 4. Lấy danh sách tất cả khách hàng

- **Method:** GET
- **Endpoint:** `/api/v1/customers`

---

### 5. Cập nhật thông tin khách hàng

- **Method:** PUT
- **Endpoint:** `/api/v1/customers/{id}`
- **Path Variable:** `id` (Integer)
- **Request Body:** Đối tượng `Customer` dạng JSON.

---

### 6. Xóa khách hàng

- **Method:** DELETE
- **Endpoint:** `/api/v1/customers/{id}`
- **Path Variable:** `id` (Integer)
