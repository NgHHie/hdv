# Các Điểm Cuối API Dịch Vụ Kỹ Thuật Viên

URL cơ sở: `/api/v1/technicians`

## Danh sách các điểm cuối:

### 1. Tạo kỹ thuật viên mới

- **Phương thức:** POST
- **Điểm cuối:** `/api/v1/technicians`
- **Thân yêu cầu:** Đối tượng `TechnicianRequest` ở định dạng JSON.

---

### 2. Lấy kỹ thuật viên theo ID

- **Phương thức:** GET
- **Điểm cuối:** `/api/v1/technicians/{id}`
- **Biến đường dẫn:** `id` (Long)

---

### 3. Lấy kỹ thuật viên theo email

- **Phương thức:** GET
- **Điểm cuối:** `/api/v1/technicians/email/{email}`
- **Biến đường dẫn:** `email` (String)

---

### 4. Lấy tất cả kỹ thuật viên

- **Phương thức:** GET
- **Điểm cuối:** `/api/v1/technicians`

---

### 5. Lấy tất cả kỹ thuật viên đang hoạt động

- **Phương thức:** GET
- **Điểm cuối:** `/api/v1/technicians/active`

---

### 6. Lấy kỹ thuật viên theo chuyên môn

- **Phương thức:** GET
- **Điểm cuối:** `/api/v1/technicians/specialization/{specialization}`
- **Biến đường dẫn:** `specialization` (String)

---

### 7. Cập nhật kỹ thuật viên

- **Phương thức:** PUT
- **Điểm cuối:** `/api/v1/technicians/{id}`
- **Biến đường dẫn:** `id` (Long)
- **Thân yêu cầu:** Đối tượng `TechnicianRequest` ở định dạng JSON.

---

### 8. Xóa kỹ thuật viên

- **Phương thức:** DELETE
- **Điểm cuối:** `/api/v1/technicians/{id}`
- **Biến đường dẫn:** `id` (Long)

---

### 9. Chuyển đổi trạng thái hoạt động của kỹ thuật viên

- **Phương thức:** PATCH
- **Điểm cuối:** `/api/v1/technicians/{id}/toggle-status`
- **Biến đường dẫn:** `id` (Long)