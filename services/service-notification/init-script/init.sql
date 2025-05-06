-- Thêm các mẫu thông báo cho các loại thông báo khác nhau
INSERT INTO notification_templates (type, subject, content_template, is_active) VALUES
-- Thông báo tạo yêu cầu bảo hành
('WARRANTY_CREATE', 'Xác nhận yêu cầu bảo hành',
 'Kính gửi Quý khách,

 Chúng tôi xác nhận đã nhận được yêu cầu bảo hành sản phẩm {{productName}} của Quý khách.

 Mã yêu cầu bảo hành của Quý khách là: {{warrantyRequestId}}. Quý khách có thể sử dụng mã này để theo dõi tiến độ xử lý.

 Chúng tôi sẽ kiểm tra điều kiện bảo hành và thông báo cho Quý khách trong thời gian sớm nhất.

 Trân trọng,
 Đội ngũ Dịch vụ Khách hàng',
 TRUE),

-- Thông báo từ chối bảo hành
('WARRANTY_REJECT', 'Thông báo về yêu cầu bảo hành',
 'Kính gửi Quý khách,

 Chúng tôi rất tiếc phải thông báo rằng yêu cầu bảo hành của Quý khách không được chấp nhận.

 Lý do: {{message}}

 Nếu Quý khách có bất kỳ thắc mắc nào, vui lòng liên hệ với chúng tôi qua số điện thoại: 1900-xxxx.

 Trân trọng,
 Đội ngũ Dịch vụ Khách hàng',
 TRUE),

-- Thông báo chấp nhận và hướng dẫn
('WARRANTY_APPROVED', 'Xác nhận bảo hành sản phẩm - Hướng dẫn gửi sản phẩm',
 'Kính gửi Quý khách,

 Yêu cầu bảo hành cho sản phẩm {{productName}} của Quý khách đã được chấp nhận. Sản phẩm của Quý khách hiện đang trong thời hạn bảo hành và đủ điều kiện để được bảo hành theo chính sách của chúng tôi.

 Quý khách vui lòng gửi sản phẩm về địa chỉ trung tâm bảo hành của chúng tôi:

 Trung tâm Bảo hành
 Địa chỉ: 123 Đường Nguyễn Văn A, Quận B, TP.C
 Điện thoại: (024) xxxxx

 Vui lòng đóng gói sản phẩm cẩn thận và đính kèm một bản sao của hóa đơn mua hàng (nếu có).

 Trân trọng,
 Đội ngũ Dịch vụ Khách hàng',
 TRUE),

-- Thông báo tiếp nhận sản phẩm
('PRODUCT_RECEIVED', 'Xác nhận đã nhận sản phẩm bảo hành',
 'Kính gửi Quý khách,

 Chúng tôi xác nhận đã nhận được sản phẩm {{productName}} của Quý khách để tiến hành bảo hành.

 Tình trạng nhận: {{message}}

 Mã theo dõi bảo hành của Quý khách là: {{warrantyRequestId}}. Chúng tôi sẽ tiến hành kiểm tra sản phẩm và thông báo cho Quý khách về tiến độ sửa chữa.

 Quý khách có thể kiểm tra trạng thái bảo hành thông qua trang web của chúng tôi hoặc gọi đến số hotline: 1900-xxxx.

 Trân trọng,
 Đội ngũ Dịch vụ Khách hàng',
 TRUE),

-- Thông báo bắt đầu chẩn đoán
('DIAGNOSIS_STARTED', 'Thông báo bắt đầu kiểm tra sản phẩm',
 'Kính gửi Quý khách,

 Chúng tôi đang tiến hành kiểm tra và chẩn đoán sản phẩm {{productName}} của Quý khách.

 Kết quả kiểm tra ban đầu: {{message}}

 Quá trình chẩn đoán có thể mất từ 1-3 ngày làm việc, tùy thuộc vào tình trạng của sản phẩm. Chúng tôi sẽ thông báo cho Quý khách kết quả kiểm tra và các bước tiếp theo.

 Trân trọng,
 Đội ngũ Kỹ thuật',
 TRUE),

-- Thông báo đang sửa chữa
('REPAIR_IN_PROGRESS', 'Cập nhật tiến độ sửa chữa sản phẩm',
 'Kính gửi Quý khách,

 Chúng tôi đang tiến hành sửa chữa sản phẩm {{productName}} của Quý khách.

 Thông tin cập nhật: {{message}}

 Dự kiến quá trình sửa chữa sẽ hoàn tất trong vòng 3-5 ngày làm việc. Chúng tôi sẽ thông báo cho Quý khách khi sản phẩm đã được sửa chữa xong.

 Trân trọng,
 Đội ngũ Kỹ thuật',
 TRUE),

-- Thông báo hoàn tất sửa chữa
('REPAIR_COMPLETED', 'Thông báo hoàn tất sửa chữa sản phẩm',
 'Kính gửi Quý khách,

 Chúng tôi vui mừng thông báo sản phẩm {{productName}} của Quý khách đã được sửa chữa hoàn tất.

 Chi tiết sửa chữa: {{message}}

 Sản phẩm của Quý khách đã được kiểm tra chất lượng và hoạt động tốt. Chúng tôi đang chuẩn bị gửi trả sản phẩm cho Quý khách.

 Trân trọng,
 Đội ngũ Dịch vụ Khách hàng',
 TRUE),

-- Thông báo gửi trả sản phẩm
('PRODUCT_SHIPPING', 'Thông báo gửi trả sản phẩm bảo hành',
 'Kính gửi Quý khách,

 Sản phẩm {{productName}} đã bảo hành của Quý khách đang được vận chuyển về địa chỉ đăng ký.

 Thông tin vận chuyển: {{message}}

 Mã vận đơn: {{trackingNumber}}

 Dự kiến sản phẩm sẽ được giao đến Quý khách trong vòng 2-3 ngày làm việc. Quý khách có thể theo dõi trạng thái vận chuyển thông qua mã vận đơn ở trên.

 Trân trọng,
 Đội ngũ Dịch vụ Khách hàng',
 TRUE),

-- Thông báo đã giao sản phẩm
('PRODUCT_DELIVERED', 'Xác nhận đã giao sản phẩm bảo hành',
 'Kính gửi Quý khách,

 Sản phẩm {{productName}} đã bảo hành của Quý khách đã được giao thành công.

 Thông tin bảo hành: {{message}}

 Quý khách vui lòng kiểm tra sản phẩm và cho chúng tôi biết nếu có bất kỳ vấn đề nào. Chúng tôi rất mong nhận được phản hồi về chất lượng dịch vụ bảo hành từ Quý khách.

 Trân trọng,
 Đội ngũ Dịch vụ Khách hàng',
 TRUE),

-- Thông báo yêu cầu đánh giá
('FEEDBACK_REQUEST', 'Đánh giá dịch vụ bảo hành',
 'Kính gửi Quý khách,

 Cảm ơn Quý khách đã sử dụng dịch vụ bảo hành của chúng tôi cho sản phẩm {{productName}}.

 {{message}}

 Quý khách vui lòng dành vài phút để đánh giá chất lượng dịch vụ bằng cách nhấn vào đường link dưới đây:

 {{feedbackLink}}

 Ý kiến của Quý khách sẽ giúp chúng tôi cải thiện chất lượng dịch vụ tốt hơn trong tương lai.

 Trân trọng,
 Đội ngũ Dịch vụ Khách hàng',
 TRUE);