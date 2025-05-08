# Kiến trúc Hệ thống

## Tổng quan
Hệ thống quản lý bảo hành dựa trên kiến trúc microservices được thiết kế để xử lý hiệu quả toàn bộ vòng đời của các yêu cầu bảo hành sản phẩm từ khi gửi đến khi hoàn thành. Hệ thống kết nối khách hàng với kỹ thuật viên và nhân viên quản lý thông qua một tập hợp các microservices chuyên biệt giao tiếp qua RESTful APIs và hàng đợi tin nhắn.

Mục tiêu chính của kiến trúc là tạo ra một hệ thống có khả năng mở rộng, khả năng chịu lỗi, và dễ bảo trì, có thể thích ứng với các yêu cầu nghiệp vụ thay đổi đồng thời cung cấp trải nghiệm liền mạch cho tất cả các bên liên quan.

## Các Thành phần Hệ thống

### Các Dịch vụ Chính
Hệ thống bao gồm các microservices chính sau đây:

- **API Gateway**: Đóng vai trò là điểm vào cho tất cả các yêu cầu từ client, xử lý định tuyến, cân bằng tải, và kiểm tra bảo mật trước khi chuyển tiếp yêu cầu đến các dịch vụ thích hợp.

- **Dịch vụ Bảo mật**: Quản lý xác thực, ủy quyền và quản lý người dùng trên toàn hệ thống, triển khai kiểm soát truy cập dựa trên vai trò (RBAC) và xác thực dựa trên JWT.

- **Dịch vụ Bảo hành**: Đóng vai trò như một điều phối viên cho các yêu cầu bảo hành, phối hợp giữa các dịch vụ khác nhau để xử lý yêu cầu bảo hành từ khi gửi đến khi hoàn thành.

- **Dịch vụ Khách hàng**: Duy trì thông tin khách hàng, lịch sử mua hàng và hồ sơ yêu cầu bảo hành.

- **Dịch vụ Sản phẩm**: Quản lý chi tiết sản phẩm, bao gồm số sê-ri và thời hạn bảo hành.

- **Dịch vụ Kỹ thuật viên**: Xử lý thông tin kỹ thuật viên, chuyên môn và tình trạng sẵn sàng cho việc phân công sửa chữa.

- **Dịch vụ Sửa chữa**: Theo dõi tiến độ sửa chữa, quản lý kho linh kiện và cập nhật trạng thái sửa chữa thông qua mô hình máy trạng thái.

- **Dịch vụ Điều kiện Bảo hành**: Xác thực các điều kiện bảo hành dựa trên các quy tắc được xác định trước để xác định tính đủ điều kiện bảo hành.

- **Dịch vụ Khảo sát**: Thu thập và phân tích phản hồi của khách hàng về quy trình bảo hành và sửa chữa.

- **Dịch vụ Thông báo**: Gửi thông báo đến khách hàng và nhân viên ở các giai đoạn khác nhau của quy trình bảo hành.

### Các Thành phần Hạ tầng

- **Discovery Server (Eureka)**: Cho phép khám phá dịch vụ, cho phép các dịch vụ xác định vị trí và giao tiếp với nhau mà không cần URL cố định.

- **Kafka Message Broker**: Tạo điều kiện cho giao tiếp bất đồng bộ giữa các dịch vụ, đảm bảo việc gửi tin nhắn đáng tin cậy cho các sự kiện quan trọng như cập nhật trạng thái.

- **Cơ sở dữ liệu MySQL**: Mỗi dịch vụ có cơ sở dữ liệu riêng để đảm bảo liên kết lỏng lẻo và khả năng mở rộng độc lập.

## Giao tiếp

### Mô hình Giao tiếp Nội bộ

Các dịch vụ trong hệ thống giao tiếp sử dụng hai mô hình chính:

1. **Giao tiếp Đồng bộ (REST APIs)**:
   - Sử dụng cho tương tác yêu cầu-phản hồi khi cần phản hồi ngay lập tức
   - Triển khai với Spring RestTemplate hoặc WebClient cho giao tiếp dịch vụ-đến-dịch vụ
   - Phát hiện dịch vụ được kích hoạt thông qua đăng ký Eureka client

2. **Giao tiếp Bất đồng bộ (Kafka)**:
   - Sử dụng cho các quy trình hướng sự kiện nơi tính nhất quán cuối cùng là chấp nhận được
   - Các trường hợp sử dụng chính bao gồm:
     - Sự kiện thông báo
     - Cập nhật trạng thái giữa các dịch vụ
     - Ghi nhật ký kiểm toán
     - Thu thập dữ liệu phân tích

### Ví dụ về Giao tiếp Dịch vụ

Dưới đây là các đường dẫn giao tiếp chính trong hệ thống:

- **Xử lý Yêu cầu Bảo hành**:
  1. API Gateway xác thực yêu cầu và chuyển tiếp đến Dịch vụ Bảo hành
  2. Dịch vụ Bảo hành gọi Dịch vụ Khách hàng để xác minh chi tiết khách hàng
  3. Dịch vụ Bảo hành gọi Dịch vụ Sản phẩm để xác minh trạng thái bảo hành sản phẩm
  4. Dịch vụ Bảo hành gọi Dịch vụ Điều kiện Bảo hành để xác thực các điều kiện bảo hành
  5. Dịch vụ Bảo hành xuất bản sự kiện cập nhật trạng thái đến Kafka
  6. Dịch vụ Thông báo tiêu thụ sự kiện và gửi thông báo đến khách hàng

- **Phân công Sửa chữa**:
  1. Dịch vụ Bảo hành gọi Dịch vụ Sửa chữa để tạo yêu cầu sửa chữa
  2. Dịch vụ Sửa chữa gọi Dịch vụ Kỹ thuật viên để tìm kỹ thuật viên phù hợp
  3. Dịch vụ Sửa chữa xuất bản sự kiện phân công đến Kafka
  4. Dịch vụ Thông báo thông báo cho kỹ thuật viên và khách hàng

### Cấu hình Mạng

- Giao tiếp dịch vụ nội bộ diễn ra qua mạng nội bộ (tên dịch vụ Docker Compose trong môi trường phát triển)
- Tất cả giao tiếp bên ngoài được định tuyến thông qua API Gateway
- CORS được cấu hình ở cấp gateway để kiểm soát các yêu cầu từ nguồn gốc khác

## Luồng Dữ liệu

### Luồng Yêu cầu Bảo hành

1. **Gửi yêu cầu**:
   - Khách hàng gửi yêu cầu bảo hành thông qua Gateway
   - Yêu cầu được định tuyến đến Dịch vụ Bảo hành
   - Dịch vụ Bảo hành tạo bản ghi yêu cầu và xác thực thông tin cơ bản
   - Thông báo xác nhận ban đầu được gửi đến khách hàng

2. **Xác thực**:
   - Dịch vụ Bảo hành yêu cầu xác thực từ Dịch vụ Điều kiện Bảo hành
   - Tính đủ điều kiện bảo hành được xác định dựa trên các điều kiện đã xác định trước
   - Nếu đủ điều kiện, trạng thái được cập nhật thành "APPROVED"; nếu không, cập nhật thành "REJECTED"
   - Thông báo được gửi đến khách hàng về kết quả xác thực

3. **Quy trình Sửa chữa**:
   - Các yêu cầu được phê duyệt được chuyển tiếp đến Dịch vụ Sửa chữa
   - Kỹ thuật viên được phân công dựa trên chuyên môn và tình trạng sẵn có
   - Dịch vụ Sửa chữa theo dõi việc sửa chữa qua các trạng thái (RECEIVED → DIAGNOSING → REPAIRING → COMPLETED)
   - Cập nhật trạng thái được xuất bản đến Kafka để thông báo và kiểm toán

4. **Hoàn thành**:
   - Sửa chữa hoàn tất và trạng thái được cập nhật
   - Khách hàng được thông báo về việc hoàn thành
   - Yêu cầu khảo sát được gửi để thu thập phản hồi

## Sơ đồ

## Triển khai Bảo mật

### Xác thực & Ủy quyền

1. **Xác thực dựa trên JWT**:
   - Dịch vụ Bảo mật cấp token JWT khi đăng nhập thành công
   - Token chứa ID người dùng, vai trò và quyền hạn
   - API Gateway xác thực token trước khi chuyển tiếp yêu cầu

2. **Kiểm soát Truy cập Dựa trên Vai trò**:
   - Ba vai trò chính: ADMIN, TECHNICIAN, CUSTOMER
   - Mỗi vai trò có quyền hạn cụ thể cho các endpoint khác nhau
   - Quyền hạn được xác thực ở cả cấp Gateway và cấp dịch vụ riêng lẻ

3. **Bảo mật API**:
   - Tất cả các endpoint đều được bảo mật ngoại trừ các endpoint xác thực công khai
   - Xác thực đầu vào được triển khai trên tất cả các dịch vụ
   - Giới hạn tốc độ được cấu hình ở cấp gateway

### Bảo mật Dữ liệu

1. **Mã hóa**:
   - Mật khẩu được lưu trữ với hàm băm BCrypt
   - Dữ liệu nhạy cảm được mã hóa khi lưu trữ
   - HTTPS được áp dụng cho tất cả các giao tiếp

2. **Kiểm soát Truy cập Dữ liệu**:
   - Mỗi dịch vụ có quyền cơ sở dữ liệu hạn chế
   - Nguyên tắc đặc quyền tối thiểu được áp dụng cho tài khoản dịch vụ
   - Các trường nhạy cảm được che dấu trong nhật ký và dấu vết kiểm toán

## Kiến trúc Triển khai

### Containerization

- Mỗi dịch vụ được containerized sử dụng Docker
- Docker Compose được sử dụng cho môi trường phát triển

### Cấu hình Môi trường

- Các tham số cấu hình được ngoại hóa thông qua biến môi trường
- Các profile khác nhau cho phát triển, kiểm thử và sản xuất


## Kết luận

Kiến trúc microservices được phác thảo ở trên cung cấp một giải pháp mạnh mẽ, có khả năng mở rộng và dễ bảo trì cho hệ thống quản lý bảo hành. Bằng cách chia nhỏ hệ thống thành các dịch vụ nhỏ hơn, chuyên biệt, chúng ta đạt được:

- Phân tách mối quan tâm tốt hơn
- Phát triển và triển khai độc lập
- Linh hoạt trong lựa chọn công nghệ
- Cải thiện cô lập lỗi
- Khả năng mở rộng cho nhu cầu kinh doanh ngày càng tăng

Kiến trúc cân bằng độ phức tạp của hệ thống phân tán với lợi ích của microservices để cung cấp giải pháp có thể phát triển theo yêu cầu kinh doanh thay đổi đồng thời duy trì độ tin cậy và hiệu suất.

## Chi tiết Triển khai Cụ thể

### Dịch vụ API Gateway

API Gateway phục vụ như một điểm vào duy nhất cho tất cả các yêu cầu từ client. Nó được triển khai bằng Spring Cloud Gateway và cung cấp các tính năng sau:

1. **Định tuyến thông minh**:
   - Chuyển tiếp yêu cầu đến microservices thích hợp
   - Hỗ trợ cân bằng tải phía client
   - Tích hợp với Eureka để phát hiện dịch vụ động

2. **Bảo mật Tập trung**:
   - Xác thực JWT cho tất cả các yêu cầu đến
   - Kiểm tra vai trò và quyền hạn ở cấp API
   - Kiểm soát và cấu hình CORS

3. **Các trường hợp Sử dụng**:
   - Tất cả client web và mobile tương tác với API Gateway
   - Xác thực đầu cuối được xử lý ở Gateway
   - Định tuyến yêu cầu được cấu hình trong application.properties

### Dịch vụ Bảo mật

Dịch vụ Bảo mật xử lý xác thực và ủy quyền cho toàn bộ hệ thống:

1. **Quản lý Người dùng**:
   - Đăng ký người dùng và quản lý hồ sơ
   - Lưu trữ mật khẩu được băm an toàn
   - Quản lý vai trò và quyền hạn

2. **Cấp phát và Xác thực JWT**:
   - Tạo JWT khi đăng nhập thành công
   - Nhúng thông tin người dùng và quyền hạn trong token
   - Cung cấp API cho các dịch vụ khác để xác thực token

3. **Kiểm soát Truy cập Dựa trên Vai trò và Quyền hạn**:
   - Cấu trúc vai trò: ADMIN, TECHNICIAN, CUSTOMER
   - Mỗi vai trò có tập hợp quyền hạn được xác định trước
   - API kiểm tra quyền cho API Gateway

### Kiến trúc Dữ liệu Chi tiết

Mô hình dữ liệu cho hệ thống tuân theo nguyên tắc Cơ sở dữ liệu mỗi Dịch vụ, nơi mỗi microservice quản lý cơ sở dữ liệu riêng. Mối quan hệ giữa các thực thể trong các dịch vụ khác nhau được duy trì thông qua tham chiếu ID.

**Ví dụ về Luồng Dữ liệu Bảo hành**:

1. **Cơ sở dữ liệu Khách hàng** → Lưu trữ thông tin hồ sơ khách hàng và lịch sử mua hàng
2. **Cơ sở dữ liệu Sản phẩm** → Lưu trữ chi tiết sản phẩm và thông tin bảo hành
3. **Cơ sở dữ liệu Bảo hành** → Lưu trữ yêu cầu bảo hành và tham chiếu đến khách hàng/sản phẩm bằng ID
4. **Cơ sở dữ liệu Sửa chữa** → Lưu trữ thông tin sửa chữa và tham chiếu đến yêu cầu bảo hành
5. **Cơ sở dữ liệu Khảo sát** → Lưu trữ phản hồi khách hàng đối với dịch vụ bảo hành/sửa chữa

Mô hình này đảm bảo rằng mỗi dịch vụ có thể hoạt động độc lập, đồng thời vẫn duy trì khả năng truy vấn các dữ liệu liên quan khi cần thiết.
