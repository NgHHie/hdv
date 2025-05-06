# Dịch Vụ Khảo Sát (Survey Service API)

Microservice này xử lý việc tạo, quản lý và phân tích các khảo sát cho hệ thống bảo hành và sửa chữa thiết bị điện tử.

## Tính Năng

- Tạo và quản lý các loại khảo sát khác nhau (phản hồi về sửa chữa, phản hồi về bảo hành, mức độ hài lòng của khách hàng, v.v.)
- Thu thập phản hồi của khách hàng cho các khảo sát
- Phân tích dữ liệu khảo sát và tạo ra các thông tin chi tiết
- Theo dõi tỷ lệ phản hồi và các số liệu hoàn thành khảo sát

## Các Điểm Cuối API (API Endpoints)

### Quản Lý Khảo Sát

- **Tạo Khảo Sát**: `POST /api/v1/surveys`
- **Lấy Tất Cả Khảo Sát**: `GET /api/v1/surveys`
- **Lấy Các Khảo Sát Đang Hoạt Động**: `GET /api/v1/surveys/active`
- **Lấy Khảo Sát Theo ID**: `GET /api/v1/surveys/{id}`
- **Lấy Khảo Sát Theo Loại**: `GET /api/v1/surveys/type/{surveyType}`
- **Lấy Khảo Sát Đang Hoạt Động Theo Loại**: `GET /api/v1/surveys/type/{surveyType}/active`
- **Cập Nhật Trạng Thái Khảo Sát**: `PATCH /api/v1/surveys/{id}/status`
- **Xóa Khảo Sát**: `DELETE /api/v1/surveys/{id}`

### Phản Hồi Khảo Sát

- **Gửi Phản Hồi Khảo Sát**: `POST /api/v1/survey-responses`
- **Lấy Tất Cả Phản Hồi Khảo Sát**: `GET /api/v1/survey-responses`
- **Lấy Phản Hồi Khảo Sát Theo ID**: `GET /api/v1/survey-responses/{id}`
- **Lấy Phản Hồi Khảo Sát Theo ID Khách Hàng**: `GET /api/v1/survey-responses/customer/{customerId}`
- **Lấy Phản Hồi Khảo Sát Theo ID Khảo Sát**: `GET /api/v1/survey-responses/survey/{surveyId}`
- **Lấy Phản Hồi Khảo Sát Theo Thực Thể Liên Quan**: `GET /api/v1/survey-responses/related?relatedEntityId={id}&relatedEntityType={type}`

### Phân Tích Khảo Sát

- **Lấy Phân Tích Khảo Sát**: `GET /api/v1/survey-analytics/{surveyId}`
- **Lấy Tỷ Lệ Phản Hồi Theo Thời Gian**: `GET /api/v1/survey-analytics/{surveyId}/response-rate?startDate={startDate}&endDate={endDate}`

## Các Loại Khảo Sát

Hệ thống hỗ trợ các loại khảo sát sau:

- **REPAIR_FEEDBACK**: Thu thập phản hồi về dịch vụ sửa chữa
- **WARRANTY_FEEDBACK**: Thu thập phản hồi về dịch vụ bảo hành
- **CUSTOMER_SATISFACTION**: Khảo sát mức độ hài lòng của khách hàng nói chung
- **PRODUCT_QUALITY**: Khảo sát về chất lượng sản phẩm
- **SERVICE_QUALITY**: Khảo sát về chất lượng dịch vụ

## Các Loại Câu Hỏi

Khảo sát có thể bao gồm nhiều loại câu hỏi khác nhau:

- **TEXT**: Câu trả lời văn bản ngắn
- **TEXTAREA**: Câu trả lời văn bản dài
- **SINGLE_CHOICE**: Lựa chọn một phương án từ nhiều lựa chọn
- **MULTIPLE_CHOICE**: Lựa chọn nhiều phương án từ nhiều lựa chọn
- **RATING**: Đánh giá bằng số (thường là 1-5 hoặc 1-10)
- **YES_NO**: Câu trả lời boolean có/không

## Tích Hợp Với Các Dịch Vụ Khác

Dịch vụ này có thể được tham chiếu bởi:

- **Dịch Vụ Sửa Chữa**: Để gửi khảo sát sau khi hoàn thành sửa chữa
- **Dịch Vụ Bảo Hành**: Để gửi khảo sát sau khi xử lý yêu cầu bảo hành
- **Dịch Vụ Khách Hàng**: Để phân tích mức độ hài lòng của khách hàng
- **Dịch Vụ Sản Phẩm**: Để thu thập phản hồi về chất lượng sản phẩm

## Xây Dựng và Chạy

```bash
# Xây dựng dịch vụ
mvn clean package

# Chạy dịch vụ
java -jar target/service-survey-1.0-SNAPSHOT.jar

# Hoặc sử dụng Docker
docker build -t service-survey .
docker run -p 8087:8087 service-survey
```