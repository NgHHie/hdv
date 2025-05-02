# Order API Endpoints

Base URL: `/api/v1/orders`

## Danh sách các endpoint:

### 1. Lấy tất cả sản phẩm trong đơn hàng có bảo hành (warranty != null)

- **Method:** GET
- **Endpoint:** `/api/v1/orders/items/with-warranty`

---

### 2. Lấy sản phẩm có bảo hành theo ID đơn hàng

- **Method:** GET
- **Endpoint:** `/api/v1/orders/items/with-warranty/order/{orderId}`
- **Path Variable:** `orderId` (Long)

---

### 3. Lấy sản phẩm có bảo hành theo ID người dùng

- **Method:** GET
- **Endpoint:** `/api/v1/orders/items/with-warranty/user/{userId}`
- **Path Variable:** `userId` (Long)
