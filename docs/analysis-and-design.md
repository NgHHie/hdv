# 📊 Hệ thống Microservices - Phân tích và Thiết kế

Tài liệu **phân tích** và **thiết kế** cho hệ thống quản lý yêu cầu bảo hành sản phẩm dựa trên kiến trúc microservices.

---

## 1. 🎯 Xác định vấn đề

Hệ thống giải quyết vấn đề quản lý các yêu cầu bảo hành sản phẩm từ khi tiếp nhận đến khi hoàn thành.

- **Người dùng**:

  - Khách hàng cần dịch vụ bảo hành cho sản phẩm
  - Nhân viên dịch vụ khách hàng xử lý yêu cầu bảo hành
  - Kỹ thuật viên kiểm tra và sửa chữa sản phẩm
  - Quản trị viên giám sát toàn bộ quy trình và hệ thống

- **Mục tiêu chính**:
  - Tối ưu hóa quy trình yêu cầu bảo hành từ khi gửi đến khi hoàn thành
  - Xác thực điều kiện bảo hành một cách tự động và nhất quán
  - Thông báo cho khách hàng ở mỗi giai đoạn của quy trình
  - Cung cấp dữ liệu và báo cáo để cải thiện quy trình
  - Đảm bảo tính bảo mật và phân quyền hợp lý

## 2. 🧩 Các Microservices được xác định

| Tên Dịch vụ          | Loại Dịch vụ    | Trách nhiệm                                                    | Tech Stack           |
| -------------------- | --------------- | -------------------------------------------------------------- | -------------------- |
| Service Warranty     | Task Service    | Xử lý việc đăng ký và xác thực điều kiện bảo hành              | Spring Boot, MySQL   |
| Service Customer     | Entity Service  | Quản lý thông tin khách hàng, bao gồm lịch sử mua và bảo hành  | Spring Boot, MySQL   |
| Service Product      | Entity Service  | Quản lý sản phẩm, bao gồm số serial và thời hạn bảo hành       | Spring Boot, MySQL   |
| Service Technicians  | Entity Service  | Quản lý thông tin về các kỹ thuật viên phụ trách kiểm tra      | Spring Boot, MySQL   |
| Service Survey       | Entity Service  | Thu thập phản hồi từ khách hàng về chất lượng dịch vụ bảo hành | Spring Boot, MySQL   |
| Service Repair       | Micro Service   | Theo dõi tiến độ sửa chữa sản phẩm, cập nhật trạng thái        | Spring Boot, MySQL   |
| Service Condition    | Micro Service   | Quản lý điều kiện bảo hành và xác thực yêu cầu                 | Spring Boot, MySQL   |
| Service Notification | Utility Service | Gửi thông báo cho khách hàng và nhân viên                      | Spring Boot, MySQL   |
| API Gateway          | Infrastructure  | Định tuyến các yêu cầu API và xác thực JWT                     | Spring Cloud Gateway |
| Service Security     | Utility Service | Quản lý xác thực và phân quyền                                 | Spring Boot, JWT     |
| Discovery Server     | Infrastructure  | Đăng ký và khám phá dịch vụ                                    | Eureka Server        |

## 3. 🔄 Giao tiếp giữa các Dịch vụ

### 3.1. Giao tiếp qua API Gateway

- Gateway ⇄ Service Security (REST): Xác thực token và phân quyền
- Gateway ⇄ All Service (REST): Định tuyến các request đến dịch vụ tương ứng

### 3.2. Giao tiếp Đồng bộ (REST API)

- Service Warranty ⇄ Service Customer: Lấy thông tin khách hàng và lịch sử yêu cầu
- Service Warranty ⇄ Service Product : Xác minh thông tin sản phẩm và thời hạn bảo hành
- Service Warranty ⇄ Service Repair: Tạo yêu cầu sửa chữa và cập nhật tiến độ
- Service Warranty ⇄ Service Technicians: Lấy thông tin kỹ thuật viên cho công việc sửa chữa
- Service Warranty ⇄ Service Condition: Xác thực điều kiện bảo hành

- Service Customer ⇄ Service Product: Lấy thông tin sản phẩm đã mua

### 3.3. Giao tiếp Bất đồng bộ (Kafka)

- Service Warranty → Dịch vụ Thông báo: Gửi thông báo về trạng thái bảo hành qua topic "warranty-notifications"

### 3.4. Service Discovery

- Tất cả các dịch vụ đều đăng ký với Eureka Server
- Các dịch vụ sử dụng tên đã đăng ký để giao tiếp với nhau
- Load balancing được quản lý bởi client-side load balancing của Spring Cloud

---

## 4. 🗂️ Thiết kế Dữ liệu

### 4.1. Service Security

#### Bảng `users`

| Thuộc tính   | Kiểu dữ liệu | Mô tả                                |
| ------------ | ------------ | ------------------------------------ |
| `id`         | BIGINT       | Mã định danh duy nhất của người dùng |
| `username`   | VARCHAR(50)  | Tên đăng nhập                        |
| `email`      | VARCHAR(50)  | Địa chỉ email                        |
| `password`   | VARCHAR(120) | Mật khẩu đã mã hóa                   |
| `first_name` | VARCHAR(255) | Tên                                  |
| `last_name`  | VARCHAR(255) | Họ                                   |
| `active`     | BOOLEAN      | Trạng thái hoạt động của tài khoản   |
| `role_id`    | BIGINT       | Khóa ngoại đến bảng roles            |

#### Bảng `roles`

| Thuộc tính    | Kiểu dữ liệu | Mô tả                                                    |
| ------------- | ------------ | -------------------------------------------------------- |
| `id`          | BIGINT       | Mã định danh duy nhất của vai trò                        |
| `name`        | VARCHAR(20)  | Tên vai trò (ROLE_ADMIN, ROLE_TECHNICIAN, ROLE_CUSTOMER) |
| `description` | VARCHAR(100) | Mô tả vai trò                                            |

#### Bảng `permissions`

| Thuộc tính    | Kiểu dữ liệu | Mô tả                                            |
| ------------- | ------------ | ------------------------------------------------ |
| `id`          | BIGINT       | Mã định danh duy nhất của quyền                  |
| `name`        | VARCHAR(255) | Tên quyền                                        |
| `path`        | VARCHAR(255) | Đường dẫn API                                    |
| `method`      | VARCHAR(10)  | Phương thức HTTP (GET, POST, PUT, DELETE, PATCH) |
| `description` | VARCHAR(255) | Mô tả quyền                                      |

#### Bảng `role_permissions` (Many-to-Many)

| Thuộc tính      | Kiểu dữ liệu | Mô tả                           |
| --------------- | ------------ | ------------------------------- |
| `role_id`       | BIGINT       | Khóa ngoại đến bảng roles       |
| `permission_id` | BIGINT       | Khóa ngoại đến bảng permissions |

### 4.2. Service Product

#### Bảng `products`

| Thuộc tính          | Kiểu dữ liệu  | Mô tả                              |
| ------------------- | ------------- | ---------------------------------- |
| `id`                | INT           | Mã định danh duy nhất của sản phẩm |
| `name`              | VARCHAR(255)  | Tên sản phẩm                       |
| `description`       | VARCHAR(255)  | Mô tả ngắn gọn về sản phẩm         |
| `price`             | DECIMAL(38,2) | Giá sản phẩm                       |
| `quantity`          | INT           | Số lượng hiện có                   |
| `category`          | VARCHAR(255)  | Danh mục sản phẩm                  |
| `warranty_duration` | FLOAT         | Thời gian bảo hành (tháng)         |
| `serial_number`     | VARCHAR(255)  | Số serial của sản phẩm             |
| `created_at`        | DATETIME      | Ngày tạo bản ghi                   |
| `updated_at`        | DATETIME      | Ngày cập nhật bản ghi lần cuối     |

#### Sơ đồ cấu trúc bảng `products`:

<p align="center">
  <img src="./asset/productERD.png" alt="products table structure" />
</p>

### 4.3. Service Notification

#### Bảng `notifications`

| Thuộc tính            | Kiểu dữ liệu | Mô tả                               |
| --------------------- | ------------ | ----------------------------------- |
| `id`                  | INT          | Mã định danh duy nhất của thông báo |
| `customer_id`         | INT          | Mã khách hàng nhận thông báo        |
| `type`                | VARCHAR(50)  | Loại thông báo                      |
| `warranty_request_id` | INT          | Mã yêu cầu bảo hành                 |
| `email`               | VARCHAR(255) | Địa chỉ email nhận thông báo        |
| `subject`             | VARCHAR(255) | Tiêu đề của thông báo               |
| `content`             | TEXT         | Nội dung đầy đủ của thông báo       |
| `status`              | VARCHAR(20)  | Trạng thái của thông báo            |
| `created_at`          | DATETIME     | Ngày tạo bản ghi                    |
| `sent_at`             | DATETIME     | Thời điểm thông báo đã được gửi     |

#### Sơ đồ cấu trúc bảng `notifications`:

<p align="center">
  <img src="./asset/notificationERD.png" alt="notifications table structure" />
</p>

#### Bảng `notification_templates`

| Thuộc tính         | Kiểu dữ liệu | Mô tả                                   |
| ------------------ | ------------ | --------------------------------------- |
| `id`               | INT          | Mã định danh duy nhất của mẫu thông báo |
| `type`             | VARCHAR(50)  | Loại thông báo                          |
| `subject`          | VARCHAR(255) | Tiêu đề của thông báo                   |
| `content_template` | TEXT         | Nội dung mẫu                            |
| `is_active`        | TINYINT(1)   | Trạng thái hoạt động của mẫu            |

#### Sơ đồ cấu trúc bảng `notification_templates`:

<p align="center">
  <img src="./asset/notificationTemplateERD.png" alt="notification_templates table structure" />
</p>

### 4.4. Service Technicians

#### Bảng `technicians`

| Thuộc tính            | Kiểu dữ liệu | Mô tả                                      |
| --------------------- | ------------ | ------------------------------------------ |
| `id`                  | BIGINT       | Mã định danh duy nhất của kỹ thuật viên    |
| `name`                | VARCHAR(100) | Tên kỹ thuật viên                          |
| `email`               | VARCHAR(100) | Địa chỉ email                              |
| `phone`               | VARCHAR(20)  | Số điện thoại                              |
| `specialization`      | VARCHAR(100) | Chuyên môn                                 |
| `years_of_experience` | INT          | Số năm kinh nghiệm làm việc trong lĩnh vực |
| `is_active`           | TINYINT(1)   | Trạng thái hoạt động                       |
| `created_at`          | DATETIME     | Ngày tạo bản ghi                           |
| `updated_at`          | DATETIME     | Ngày cập nhật bản ghi lần cuối             |

#### Sơ đồ cấu trúc bảng `technicians`:

<p align="center">
  <img src="./asset/technicianERD.png" alt="technicians table structure" />
</p>

### 4.5. Service Customer

#### Bảng `customers`

| Thuộc tính   | Kiểu dữ liệu | Mô tả                                |
| ------------ | ------------ | ------------------------------------ |
| `id`         | INT          | Mã định danh duy nhất của khách hàng |
| `name`       | VARCHAR(255) | Tên khách hàng                       |
| `email`      | VARCHAR(255) | Địa chỉ email                        |
| `phone`      | VARCHAR(20)  | Số điện thoại                        |
| `address`    | VARCHAR(500) | Địa chỉ                              |
| `created_at` | DATETIME     | Ngày tạo bản ghi                     |
| `updated_at` | DATETIME     | Ngày cập nhật bản ghi lần cuối       |

#### Bảng `purchases`

| Thuộc tính          | Kiểu dữ liệu  | Mô tả                                  |
| ------------------- | ------------- | -------------------------------------- |
| `id`                | INT           | Mã định danh duy nhất của đơn mua hàng |
| `customer_id`       | INT           | Khóa ngoại đến bảng customers          |
| `product_id`        | INT           | Khóa ngoại đến bảng products           |
| `purchase_date`     | DATETIME      | Ngày mua hàng                          |
| `purchase_price`    | DECIMAL(10,2) | Giá mua                                |
| `warranty_end_date` | DATE          | Ngày kết thúc bảo hành                 |
| `created_at`        | DATETIME      | Ngày tạo bản ghi                       |

#### Bảng `warranty_requests`

| Thuộc tính          | Kiểu dữ liệu | Mô tả                                      |
| ------------------- | ------------ | ------------------------------------------ |
| `id`                | INT          | Mã định danh duy nhất của yêu cầu bảo hành |
| `customer_id`       | INT          | Khóa ngoại đến bảng customers              |
| `product_id`        | INT          | Khóa ngoại đến bảng products               |
| `issue_description` | TEXT         | Mô tả vấn đề sản phẩm                      |
| `image_urls`        | TEXT         | Đường dẫn đến ảnh chụp sản phẩm lỗi        |
| `status`            | VARCHAR(50)  | Trạng thái yêu cầu                         |
| `submission_date`   | DATETIME     | Ngày gửi yêu cầu                           |
| `validation_notes`  | TEXT         | Ghi chú xác thực                           |
| `repair_id`         | INT          | Khóa ngoại đến bảng repairs                |
| `created_at`        | DATETIME     | Ngày tạo bản ghi                           |
| `updated_at`        | DATETIME     | Ngày cập nhật bản ghi lần cuối             |

#### Bảng `warranty_history`

| Thuộc tính            | Kiểu dữ liệu | Mô tả                                 |
| --------------------- | ------------ | ------------------------------------- |
| `id`                  | INT          | Mã định danh duy nhất                 |
| `warranty_request_id` | INT          | Khóa ngoại đến bảng warranty_requests |
| `status`              | VARCHAR(50)  | Trạng thái                            |
| `notes`               | TEXT         | Ghi chú                               |
| `performed_by`        | VARCHAR(100) | Người thực hiện                       |
| `created_at`          | DATETIME     | Thời điểm thực hiện                   |

### 4.6. Service Repair

#### Bảng `repair_requests`

| Thuộc tính          | Kiểu dữ liệu  | Mô tả                                      |
| ------------------- | ------------- | ------------------------------------------ |
| `id`                | INT           | Mã định danh duy nhất của yêu cầu sửa chữa |
| `warranty_id`       | INT           | Khóa ngoại đến bảng warranty_requests      |
| `customer_id`       | INT           | Khóa ngoại đến bảng customers              |
| `product_id`        | INT           | Khóa ngoại đến bảng products               |
| `issue_description` | TEXT          | Mô tả vấn đề                               |
| `image_urls`        | TEXT          | Đường dẫn đến ảnh                          |
| `status`            | VARCHAR(50)   | Trạng thái sửa chữa                        |
| `repair_notes`      | TEXT          | Ghi chú sửa chữa                           |
| `technician_id`     | INT           | Khóa ngoại đến bảng technicians            |
| `start_date`        | DATETIME      | Ngày bắt đầu sửa chữa                      |
| `end_date`          | DATETIME      | Ngày kết thúc sửa chữa                     |
| `repair_cost`       | DECIMAL(10,2) | Chi phí sửa chữa                           |
| `within_warranty`   | BOOLEAN       | Trong thời hạn bảo hành                    |
| `created_at`        | DATETIME      | Ngày tạo bản ghi                           |
| `updated_at`        | DATETIME      | Ngày cập nhật bản ghi lần cuối             |

#### Bảng `repair_parts`

| Thuộc tính   | Kiểu dữ liệu  | Mô tả                               |
| ------------ | ------------- | ----------------------------------- |
| `id`         | INT           | Mã định danh duy nhất               |
| `repair_id`  | INT           | Khóa ngoại đến bảng repair_requests |
| `part_name`  | VARCHAR(100)  | Tên linh kiện                       |
| `quantity`   | INT           | Số lượng                            |
| `price`      | DECIMAL(10,2) | Giá mỗi linh kiện                   |
| `created_at` | DATETIME      | Ngày tạo bản ghi                    |

### 4.7. Service Condition

#### Bảng `warranty_conditions`

| Thuộc tính    | Kiểu dữ liệu | Mô tả                          |
| ------------- | ------------ | ------------------------------ |
| `id`          | INT          | Mã định danh duy nhất          |
| `name`        | VARCHAR(255) | Tên điều kiện                  |
| `description` | TEXT         | Mô tả điều kiện                |
| `is_active`   | BOOLEAN      | Trạng thái hoạt động           |
| `created_at`  | DATETIME     | Ngày tạo bản ghi               |
| `updated_at`  | DATETIME     | Ngày cập nhật bản ghi lần cuối |

#### Bảng `warranty_condition_results`

| Thuộc tính            | Kiểu dữ liệu | Mô tả                                   |
| --------------------- | ------------ | --------------------------------------- |
| `id`                  | INT          | Mã định danh duy nhất                   |
| `warranty_request_id` | INT          | Khóa ngoại đến bảng warranty_requests   |
| `condition_id`        | INT          | Khóa ngoại đến bảng warranty_conditions |
| `passed`              | BOOLEAN      | Kết quả xác thực (đạt/không đạt)        |
| `notes`               | TEXT         | Ghi chú                                 |
| `evaluated_by`        | VARCHAR(100) | Người đánh giá                          |
| `evaluated_at`        | DATETIME     | Thời điểm đánh giá                      |

### 4.9. Service Survey

#### Bảng `surveys`

| Thuộc tính    | Kiểu dữ liệu | Mô tả                          |
| ------------- | ------------ | ------------------------------ |
| `id`          | BIGINT       | Mã định danh duy nhất          |
| `title`       | VARCHAR(255) | Tiêu đề khảo sát               |
| `description` | TEXT         | Mô tả khảo sát                 |
| `active`      | BOOLEAN      | Trạng thái hoạt động           |
| `created_at`  | DATETIME     | Ngày tạo bản ghi               |
| `updated_at`  | DATETIME     | Ngày cập nhật bản ghi lần cuối |

#### Bảng `survey_questions`

| Thuộc tính       | Kiểu dữ liệu | Mô tả                       |
| ---------------- | ------------ | --------------------------- |
| `id`             | BIGINT       | Mã định danh duy nhất       |
| `survey_id`      | BIGINT       | Khóa ngoại đến bảng surveys |
| `question_text`  | TEXT         | Nội dung câu hỏi            |
| `question_order` | INT          | Thứ tự câu hỏi              |
| `required`       | BOOLEAN      | Bắt buộc phải trả lời       |

#### Bảng `survey_responses`

| Thuộc tính    | Kiểu dữ liệu | Mô tả                         |
| ------------- | ------------ | ----------------------------- |
| `id`          | BIGINT       | Mã định danh duy nhất         |
| `survey_id`   | BIGINT       | Khóa ngoại đến bảng surveys   |
| `customer_id` | BIGINT       | Khóa ngoại đến bảng customers |
| `created_at`  | DATETIME     | Ngày tạo bản ghi              |

#### Bảng `survey_answer`

| Thuộc tính    | Kiểu dữ liệu | Mô tả                                |
| ------------- | ------------ | ------------------------------------ |
| `id`          | BIGINT       | Mã định danh duy nhất                |
| `response_id` | BIGINT       | Khóa ngoại đến bảng survey_responses |
| `question_id` | BIGINT       | Khóa ngoại đến bảng survey_questions |
| `answer_text` | TEXT         | Nội dung câu trả lời                 |
| `created_at`  | DATETIME     | Ngày tạo bản ghi                     |

---

## 5. 🔐 Các Cân nhắc về Bảo mật

### 5.1. Xác thực và Ủy quyền

- **JWT Authentication**: Sử dụng JSON Web Tokens (JWT) để xác thực người dùng và duy trì phiên làm việc
- **Dịch vụ Bảo mật**: Quản lý việc cấp phát và xác thực token, lưu trữ thông tin người dùng
- **Kiểm soát Truy cập Dựa trên Vai trò (RBAC)**: Phân quyền truy cập dựa trên vai trò (Admin, Technician, Customer)
- **Ủy quyền Dựa trên Quyền hạn**: Quản lý chi tiết quyền truy cập đến từng API endpoint

### 5.2. Bảo mật API

- **API Gateway**: Tập trung xác thực và định tuyến
- **Cấu hình CORS**: Cấu hình kiểm soát truy cập từ các domain khác
- **Xác thực đầu vào**: Xác thực đầu vào trên mỗi dịch vụ

### 5.3. Bảo mật Dữ liệu

- **Mã hóa Mật khẩu**: Mã hóa mật khẩu bằng BCrypt
- **Xử lý Dữ liệu Nhạy cảm**: Xử lý dữ liệu nhạy cảm như thông tin khách hàng một cách an toàn
- **Bảo mật Cơ sở dữ liệu**: Sử dụng tài khoản có phân quyền, giới hạn truy cập cho từng dịch vụ

---

## 6. 📦 Kế hoạch Triển khai

### 6.1. Containerization

- Mỗi dịch vụ được containerized bằng Docker với Dockerfile riêng
- Docker Compose được sử dụng cho local development và testing

### 6.2. Cấu hình Môi trường

- **Configuration Server**: Quản lý cấu hình tập trung
- **Biến Môi trường**: Sử dụng biến môi trường cho các thông số cấu hình
- **Cấu hình theo Profile**: Cấu hình khác nhau cho development, testing, production

---

## 7. 🎨 Sơ đồ Kiến trúc

## 8. ✅ Tổng kết

Hệ thống quản lý yêu cầu bảo hành sản phẩm dựa trên kiến trúc microservices mang lại nhiều lợi ích so với thiết kế monolithic truyền thống:

### Ưu điểm của kiến trúc đã chọn

1. **Khả năng mở rộng độc lập**: Mỗi dịch vụ có thể được mở rộng dựa trên nhu cầu riêng.

2. **Phát triển độc lập**: Các team có thể làm việc trên các dịch vụ khác nhau mà không ảnh hưởng đến nhau. Điều này tăng tốc độ phát triển và triển khai.

3. **Khả năng chịu lỗi**: Nếu một dịch vụ gặp sự cố, hệ thống vẫn có thể hoạt động một phần.

4. **Tích hợp công nghệ phù hợp**: Mỗi dịch vụ có thể sử dụng công nghệ phù hợp nhất với nhiệm vụ của nó. Trong hệ thống này, chúng tôi sử dụng Spring Boot và MySQL làm nền tảng chung, nhưng trong tương lai có thể tích hợp các công nghệ khác nếu cần.

5. **Quản lý phức tạp hiệu quả**: Mỗi dịch vụ tập trung vào một phần nhỏ của domain, giúp giảm độ phức tạp và dễ dàng bảo trì.

### Khả năng mở rộng trong tương lai

Kiến trúc này cho phép dễ dàng bổ sung các dịch vụ mới hoặc cải tiến các dịch vụ hiện có:

1. **Dịch vụ Phân tích**: Có thể được thêm vào để phân tích dữ liệu về bảo hành và sửa chữa.
2. **Dịch vụ Quản lý Kho**: Quản lý kho linh kiện và phụ tùng.
3. **Dịch vụ Báo cáo**: Tạo báo cáo và dashboard cho quản lý.
4. **Gateway Ứng dụng Di động**: Cổng kết nối cho ứng dụng di động.

## Tác giả

| MSV          | Họ Và Tên           |
| ------------ | ------------------- |
| `B21DCCN031` | Trịnh Vinh Tuấn Đạt |
| `B21DCCN343` | Nguyễn Hoàng Hiệp   |
| `B21DCCN691` | Hà Cường Thịnh      |
