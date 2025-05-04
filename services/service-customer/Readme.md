# Customer Service API Endpoints

## Base URL: `/api/v1/customers`

### 1. Tạo khách hàng mới

- **Method:** POST
- **Endpoint:** `/api/v1/customers`
- **Request Body:** Đối tượng `Customer` dạng JSON
- **Response:** Trả về đối tượng `CustomerDTO` và mã trạng thái 201 (Created)

---

### 2. Lấy thông tin khách hàng theo ID

- **Method:** GET
- **Endpoint:** `/api/v1/customers/{id}`
- **Path Variable:** `id` (Integer)
- **Response:** Trả về đối tượng `CustomerDTO` nếu tìm thấy, hoặc 404 nếu không tìm thấy

---

### 3. Lấy thông tin khách hàng theo email

- **Method:** GET
- **Endpoint:** `/api/v1/customers/email/{email}`
- **Path Variable:** `email` (String)
- **Response:** Trả về đối tượng `CustomerDTO` nếu tìm thấy, hoặc 404 nếu không tìm thấy

---

### 4. Lấy danh sách tất cả khách hàng

- **Method:** GET
- **Endpoint:** `/api/v1/customers`
- **Response:** Trả về danh sách các đối tượng `CustomerDTO`

---

### 5. Cập nhật thông tin khách hàng

- **Method:** PUT
- **Endpoint:** `/api/v1/customers/{id}`
- **Path Variable:** `id` (Integer)
- **Request Body:** Đối tượng `Customer` dạng JSON
- **Response:** Trả về đối tượng `CustomerDTO` đã cập nhật, hoặc 400 nếu có lỗi

---

### 6. Xóa khách hàng

- **Method:** DELETE
- **Endpoint:** `/api/v1/customers/{id}`
- **Path Variable:** `id` (Integer)
- **Response:** Trả về trạng thái 204 (No Content) nếu thành công

---

### 7. Lấy tất cả các sản phẩm đang trong thời gian bảo hành

- **Method:** GET
- **Endpoint:** `/api/v1/customers/purchases/items/with-warranty`
- **Response:** Trả về danh sách các đối tượng `PurchaseItemDTO` có bảo hành còn hiệu lực

---

### 8. Lấy các sản phẩm đang trong thời gian bảo hành theo ID đơn hàng

- **Method:** GET
- **Endpoint:** `/api/v1/customers/purchases/{purchaseId}/items/with-warranty`
- **Path Variable:** `purchaseId` (Long)
- **Response:** Trả về danh sách các đối tượng `PurchaseItemDTO` có bảo hành còn hiệu lực trong đơn hàng

---

### 9. Lấy các sản phẩm đang trong thời gian bảo hành theo ID khách hàng

- **Method:** GET
- **Endpoint:** `/api/v1/customers/{customerId}/purchases/items/with-warranty`
- **Path Variable:** `customerId` (Integer)
- **Response:** Trả về danh sách các đối tượng `PurchaseItemDTO` có bảo hành còn hiệu lực của khách hàng
