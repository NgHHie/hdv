# 📊 Microservices System - Analysis and Design

Tài liệu **phân tích** và **thiết kế** cho hệ thống quản lý yêu cầu bảo hành sản phẩm dựa trên kiến trúc microservices.

---

## 1. 🎯 Problem Statement

Hệ thống giải quyết vấn đề quản lý các yêu cầu bảo hành sản phẩm từ khi tiếp nhận đến khi hoàn thành.

- **Người dùng**: - Khách hàng cần dịch vụ bảo hành cho sản phẩm - Nhân viên dịch vụ khách hàng xử lý yêu cầu bảo hành - Kỹ thuật viên kiểm tra và sửa chữa sản phẩm

- **Mục tiêu chính**: - Tối ưu hóa quy trình yêu cầu bảo hành từ khi gửi đến khi hoàn thành - Xác thực điều kiện bảo hành một cách tự động và nhất quán - Cập nhật tiến độ sửa chữa theo thời gian - Thông báo cho khách hàng ở mỗi giai đoạn của quy trình
- **Dữ liệu được xử lý**: - Thông tin khách hàng (thông tin cá nhân, thông tin liên hệ) - Thông tin sản phẩm (số serial, ngày mua, thời hạn bảo hành) - Yêu cầu bảo hành (mô tả vấn đề, hình ảnh, thời gian) - Chi tiết sửa chữa

## 2. 🧩 Identified Microservices

| Service Name         | Responsibility                                    | Tech Stack         |
| -------------------- | ------------------------------------------------- | ------------------ |
| Customer Service     | Quản lý thông tin khách hàng                      | Spring Boot, MySQL |
| Product Service      | Quản lý catalog sản phẩm và thông tin kỹ thuật    | Spring Boot, MySQL |
| Warranty Service     | Xử lý việc đăng ký và xác thực điều kiện bảo hành | Spring Boot, MySQL |
| Repair Service       | Quản lý quá trình sửa chữa và bảo trì             | Spring Boot, MySQL |
| Notification Service | Gửi thông báo cho khách hàng và nhân viên         | Spring Boot, Redis |

## 3. 🔄 Service Communication

Describe how your services communicate (e.g., REST APIs, message queue, gRPC).

- Gateway ⇄ service-a (REST)
- Gateway ⇄ service-b (REST)
- Internal: service-a ⇄ service-b (optional)

---

## 4. 🗂️ Data Design

Describe how data is structured and stored in each service.

- service-a: User accounts, credentials
- service-b: Course catalog, registrations

Use diagrams if possible (DB schema, ERD, etc.)

---

## 5. 🔐 Security Considerations

- Use JWT for user sessions
- Validate input on each service
- Role-based access control for APIs

---

## 6. 📦 Deployment Plan

- Use `docker-compose` to manage local environment
- Each service has its own Dockerfile
- Environment config stored in `.env` file

---

## 7. 🎨 Architecture Diagram

> _(You can add an image or ASCII diagram below)_

```
+---------+        +--------------+
| Gateway | <----> | Service A    |
|         | <----> | Auth Service |
+---------+        +--------------+
       |                ^
       v                |
+--------------+   +------------------+
| Service B    |   | Database / Redis |
| Course Mgmt  |   +------------------+
+--------------+
```

---

## ✅ Summary

Summarize why this architecture is suitable for your use case, how it scales, and how it supports independent development and deployment.

## Author

This template was created by Hung Dang.

- Email: hungdn@ptit.edu.vn
- GitHub: hungdn1701

Good luck! 💪🚀
