# Dịch Vụ Khảo Sát (Survey Service API)

Microservice này xử lý việc tạo, quản lý và phân tích các khảo sát cho hệ thống bảo hành và sửa chữa thiết bị điện tử.

## Tính Năng

- Tạo và quản lý các loại khảo sát khác nhau (phản hồi về sửa chữa, phản hồi về bảo hành, mức độ hài lòng của khách hàng, v.v.)
- Thu thập phản hồi của khách hàng cho các khảo sát
- Phân tích dữ liệu khảo sát
- Theo dõi tỷ lệ phản hồi và các số liệu hoàn thành khảo sát

## Các Điểm Cuối API (API Endpoints)

### Quản Lý Khảo Sát

- **Tạo Khảo Sát Mới**: `POST /api/v1/surveys`
  - Request Body: Đối tượng `CreateSurveyRequest` dạng JSON
  - Response: Trả về đối tượng `SurveyDto` và mã trạng thái 201 (Created)

- **Lấy Tất Cả Khảo Sát**: `GET /api/v1/surveys`
  - Response: Trả về danh sách các đối tượng `SurveyDto`

- **Lấy Các Khảo Sát Đang Hoạt Động**: `GET /api/v1/surveys/active`
  - Response: Trả về danh sách các đối tượng `SurveyDto` có trạng thái active = true

- **Lấy Khảo Sát Theo ID**: `GET /api/v1/surveys/{id}`
  - Path Variable: `id` (Long)
  - Response: Trả về đối tượng `SurveyDto` nếu tìm thấy, hoặc 404 nếu không tìm thấy

- **Cập Nhật Trạng Thái Khảo Sát**: `PATCH /api/v1/surveys/{id}/status`
  - Path Variable: `id` (Long)
  - Query Parameter: `active` (Boolean)
  - Response: Trả về đối tượng `SurveyDto` đã cập nhật

- **Xóa Khảo Sát**: `DELETE /api/v1/surveys/{id}`
  - Path Variable: `id` (Long)
  - Response: Trả về trạng thái 204 (No Content) nếu thành công

### Phản Hồi Khảo Sát

- **Gửi Phản Hồi Khảo Sát**: `POST /api/v1/surveys/survey-responses`
  - Request Body: Đối tượng `SubmitSurveyResponseRequest` dạng JSON
  - Response: Trả về đối tượng `ResponseDto` và mã trạng thái 201 (Created)

- **Lấy Tất Cả Phản Hồi Khảo Sát**: `GET /api/v1/surveys/survey-responses`
  - Response: Trả về danh sách các đối tượng `ResponseDto`

- **Lấy Phản Hồi Khảo Sát Theo ID**: `GET /api/v1/surveys/survey-responses/{id}`
  - Path Variable: `id` (Long)
  - Response: Trả về đối tượng `ResponseDto` nếu tìm thấy, hoặc 404 nếu không tìm thấy

- **Lấy Phản Hồi Khảo Sát Theo ID Khách Hàng**: `GET /api/v1/surveys/survey-responses/customer/{customerId}`
  - Path Variable: `customerId` (Long)
  - Response: Trả về danh sách các đối tượng `ResponseDto` của khách hàng

- **Lấy Phản Hồi Khảo Sát Theo ID Khảo Sát**: `GET /api/v1/surveys/survey-responses/survey/{surveyId}`
  - Path Variable: `surveyId` (Long)
  - Response: Trả về danh sách các đối tượng `ResponseDto` của khảo sát

## Cấu Trúc Dữ Liệu

### Khảo Sát (Survey)

```json
{
  "id": "Long",
  "title": "String",
  "description": "String",
  "active": "Boolean",
  "createdAt": "LocalDateTime",
  "updatedAt": "LocalDateTime",
  "questions": [
    {
      "id": "Long",
      "surveyId": "Long",
      "questionText": "String",
      "questionOrder": "Integer",
      "required": "Boolean"
    }
  ]
}
```

### Phản Hồi Khảo Sát (Response)

```json
{
  "id": "Long",
  "surveyId": "Long",
  "surveyTitle": "String",
  "customerId": "Long",
  "createdAt": "LocalDateTime",