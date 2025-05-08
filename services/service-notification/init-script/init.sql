-- -- Create database if it doesn't exist
-- CREATE DATABASE IF NOT EXISTS service_notification;

-- -- Switch to the database
-- USE service_notification;

-- -- Create notification_templates table
-- CREATE TABLE IF NOT EXISTS notification_templates (
--     id INT AUTO_INCREMENT PRIMARY KEY,
--     type VARCHAR(50) NOT NULL,
--     subject VARCHAR(255) NOT NULL,
--     content_template TEXT NOT NULL,
--     is_active BOOLEAN DEFAULT TRUE
-- );

-- -- Create notifications table
-- CREATE TABLE IF NOT EXISTS notifications (
--     id INT AUTO_INCREMENT PRIMARY KEY,
--     customer_id INT,
--     type VARCHAR(50) NOT NULL,
--     warranty_request_id INT,
--     email VARCHAR(255),
--     subject VARCHAR(255),
--     content TEXT,
--     status VARCHAR(20),
--     created_at DATETIME,
--     sent_at DATETIME
-- );

-- -- Insert notification templates
-- INSERT INTO notification_templates (type, subject, content_template, is_active) VALUES
-- -- Warranty creation notification
-- ('WARRANTY_RECEIVED', 'Xác nhận yêu cầu bảo hành',
--  'Kính gửi Quý khách {{customerName}},

--  Chúng tôi xác nhận đã nhận được yêu cầu bảo hành sản phẩm {{productName}} của Quý khách.

--  Mã yêu cầu bảo hành của Quý khách là: {{warrantyRequestId}}. Quý khách có thể sử dụng mã này để theo dõi tiến độ xử lý.

--  Chúng tôi sẽ kiểm tra điều kiện bảo hành và thông báo cho Quý khách trong thời gian sớm nhất.

--  Trân trọng,
--  Đội ngũ Dịch vụ Khách hàng',
--  TRUE),

-- -- Warranty rejection notification
-- ('WARRANTY_REJECTED', 'Thông báo về yêu cầu bảo hành',
--  'Kính gửi Quý khách {{customerName}},

--  Chúng tôi rất tiếc phải thông báo rằng yêu cầu bảo hành của Quý khách không được chấp nhận.

--  Lý do: {{message}}

--  Nếu Quý khách có bất kỳ thắc mắc nào, vui lòng liên hệ với chúng tôi qua số điện thoại: 1900-xxxx.

--  Trân trọng,
--  Đội ngũ Dịch vụ Khách hàng',
--  TRUE),

-- -- Warranty approval notification
-- ('WARRANTY_APPROVED', 'Xác nhận bảo hành sản phẩm - Hướng dẫn gửi sản phẩm',
--  'Kính gửi Quý khách {{customerName}},

--  Yêu cầu bảo hành cho sản phẩm {{productName}} của Quý khách đã được chấp nhận. Sản phẩm của Quý khách hiện đang trong thời hạn bảo hành và đủ điều kiện để được bảo hành theo chính sách của chúng tôi.

--  Quý khách vui lòng gửi sản phẩm về địa chỉ trung tâm bảo hành của chúng tôi:

--  Trung tâm Bảo hành
--  Địa chỉ: 123 Đường Nguyễn Văn A, Quận B, TP.C
--  Điện thoại: (024) xxxxx

--  Vui lòng đóng gói sản phẩm cẩn thận và đính kèm một bản sao của hóa đơn mua hàng (nếu có).

--  Trân trọng,
--  Đội ngũ Dịch vụ Khách hàng',
--  TRUE),

-- -- Product received notification
-- ('PRODUCT_RECEIVED', 'Xác nhận đã nhận sản phẩm bảo hành',
--  'Kính gửi Quý khách {{customerName}},

--  Chúng tôi xác nhận đã nhận được sản phẩm {{productName}} của Quý khách để tiến hành bảo hành.

--  Tình trạng nhận: {{message}}

--  Mã theo dõi bảo hành của Quý khách là: {{warrantyRequestId}}. Chúng tôi sẽ tiến hành kiểm tra sản phẩm và thông báo cho Quý khách về tiến độ sửa chữa.

--  Quý khách có thể kiểm tra trạng thái bảo hành thông qua trang web của chúng tôi hoặc gọi đến số hotline: 1900-xxxx.

--  Trân trọng,
--  Đội ngũ Dịch vụ Khách hàng',
--  TRUE),

-- -- Diagnosis started notification
-- ('DIAGNOSIS_STARTED', 'Thông báo bắt đầu kiểm tra sản phẩm',
--  'Kính gửi Quý khách {{customerName}},

--  Chúng tôi đang tiến hành kiểm tra và chẩn đoán sản phẩm {{productName}} của Quý khách.

--  Kết quả kiểm tra ban đầu: {{message}}

--  Quá trình chẩn đoán có thể mất từ 1-3 ngày làm việc, tùy thuộc vào tình trạng của sản phẩm. Chúng tôi sẽ thông báo cho Quý khách kết quả kiểm tra và các bước tiếp theo.

--  Trân trọng,
--  Đội ngũ Kỹ thuật',
--  TRUE),

-- -- Repair in progress notification
-- ('REPAIR_IN_PROGRESS', 'Cập nhật tiến độ sửa chữa sản phẩm',
--  'Kính gửi Quý khách {{customerName}},

--  Chúng tôi đang tiến hành sửa chữa sản phẩm {{productName}} của Quý khách.

--  Thông tin cập nhật: {{message}}

--  Dự kiến quá trình sửa chữa sẽ hoàn tất trong vòng 3-5 ngày làm việc. Chúng tôi sẽ thông báo cho Quý khách khi sản phẩm đã được sửa chữa xong.

--  Trân trọng,
--  Đội ngũ Kỹ thuật',
--  TRUE),

-- -- Repair completed notification
-- ('REPAIR_COMPLETED', 'Thông báo hoàn tất sửa chữa sản phẩm',
--  'Kính gửi Quý khách {{customerName}},

--  Chúng tôi vui mừng thông báo sản phẩm {{productName}} của Quý khách đã được sửa chữa hoàn tất.

--  Chi tiết sửa chữa: {{message}}

--  Sản phẩm của Quý khách đã được kiểm tra chất lượng và hoạt động tốt. Chúng tôi đang chuẩn bị gửi trả sản phẩm cho Quý khách.

--  Trân trọng,
--  Đội ngũ Dịch vụ Khách hàng',
--  TRUE),

-- -- Product shipping notification
-- ('PRODUCT_SHIPPING', 'Thông báo gửi trả sản phẩm bảo hành',
--  'Kính gửi Quý khách {{customerName}},

--  Sản phẩm {{productName}} đã bảo hành của Quý khách đang được vận chuyển về địa chỉ đăng ký.

--  Thông tin vận chuyển: {{message}}

--  Mã vận đơn: {{trackingNumber}}

--  Dự kiến sản phẩm sẽ được giao đến Quý khách trong vòng 2-3 ngày làm việc. Quý khách có thể theo dõi trạng thái vận chuyển thông qua mã vận đơn ở trên.

--  Trân trọng,
--  Đội ngũ Dịch vụ Khách hàng',
--  TRUE),

-- -- Product delivered notification
-- ('PRODUCT_DELIVERED', 'Xác nhận đã giao sản phẩm bảo hành',
--  'Kính gửi Quý khách {{customerName}},

--  Sản phẩm {{productName}} đã bảo hành của Quý khách đã được giao thành công.

--  Thông tin bảo hành: {{message}}

--  Quý khách vui lòng kiểm tra sản phẩm và cho chúng tôi biết nếu có bất kỳ vấn đề nào. Chúng tôi rất mong nhận được phản hồi về chất lượng dịch vụ bảo hành từ Quý khách.

--  Trân trọng,
--  Đội ngũ Dịch vụ Khách hàng',
--  TRUE),

-- -- Feedback request notification
-- ('FEEDBACK_REQUEST', 'Đánh giá dịch vụ bảo hành',
--  'Kính gửi Quý khách {{customerName}},

--  Cảm ơn Quý khách đã sử dụng dịch vụ bảo hành của chúng tôi cho sản phẩm {{productName}}.

--  {{message}}

--  Quý khách vui lòng dành vài phút để đánh giá chất lượng dịch vụ bằng cách nhấn vào đường link dưới đây:

--  {{feedbackLink}}

--  Ý kiến của Quý khách sẽ giúp chúng tôi cải thiện chất lượng dịch vụ tốt hơn trong tương lai.

--  Trân trọng,
--  Đội ngũ Dịch vụ Khách hàng',
--  TRUE);

-- Create database if it doesn't exist
CREATE DATABASE IF NOT EXISTS service_notification;

-- Switch to the database
USE service_notification;

-- Create notification_templates table
CREATE TABLE IF NOT EXISTS notification_templates (
    id INT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(50) NOT NULL,
    subject VARCHAR(255) NOT NULL,
    content_template TEXT NOT NULL,
    is_active BOOLEAN DEFAULT TRUE
);

-- Create notifications table
CREATE TABLE IF NOT EXISTS notifications (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT,
    type VARCHAR(50) NOT NULL,
    warranty_request_id INT,
    email VARCHAR(255),
    subject VARCHAR(255),
    content TEXT,
    status VARCHAR(20),
    created_at DATETIME,
    sent_at DATETIME
);

-- Insert notification templates
INSERT INTO notification_templates (type, subject, content_template, is_active) VALUES
-- Warranty creation notification
('WARRANTY_RECEIVED', 'Xac nhan yeu cau bao hanh',
 'Kinh gui Quy khach {{customerName}},

 Chung toi xac nhan da nhan duoc yeu cau bao hanh san pham {{productName}} cua Quy khach.

 Ma yeu cau bao hanh cua Quy khach la: {{warrantyRequestId}}. Quy khach co the su dung ma nay de theo doi tien do xu ly.

 Chung toi se kiem tra dieu kien bao hanh va thong bao cho Quy khach trong thoi gian som nhat.

 Tran trong,
 Doi ngu Dich vu Khach hang',
 TRUE),

-- Warranty rejection notification
('WARRANTY_REJECTED', 'Thong bao ve yeu cau bao hanh',
 'Kinh gui Quy khach {{customerName}},

 Chung toi rat tiec phai thong bao rang yeu cau bao hanh cua Quy khach khong duoc chap nhan.

 Ly do: {{message}}

 Neu Quy khach co bat ky thac mac nao, vui long lien he voi chung toi qua so dien thoai: 1900-xxxx.

 Tran trong,
 Doi ngu Dich vu Khach hang',
 TRUE),

-- Warranty approval notification
('WARRANTY_APPROVED', 'Xac nhan bao hanh san pham - Huong dan gui san pham',
 'Kinh gui Quy khach {{customerName}},

 Yeu cau bao hanh cho san pham {{productName}} cua Quy khach da duoc chap nhan. San pham cua Quy khach hien dang trong thoi han bao hanh va du dieu kien de duoc bao hanh theo chinh sach cua chung toi.

 Quy khach vui long gui san pham ve dia chi trung tam bao hanh cua chung toi:

 Trung tam Bao hanh
 Dia chi: 123 Duong Nguyen Van A, Quan B, TP.C
 Dien thoai: (024) xxxxx

 Vui long dong goi san pham can than va dinh kem mot ban sao cua hoa don mua hang (neu co).

 Tran trong,
 Doi ngu Dich vu Khach hang',
 TRUE),

-- Product received notification
('PRODUCT_RECEIVED', 'Xac nhan da nhan san pham bao hanh',
 'Kinh gui Quy khach {{customerName}},

 Chung toi xac nhan da nhan duoc san pham {{productName}} cua Quy khach de tien hanh bao hanh.

 Tinh trang nhan: {{message}}

 Ma theo doi bao hanh cua Quy khach la: {{warrantyRequestId}}. Chung toi se tien hanh kiem tra san pham va thong bao cho Quy khach ve tien do sua chua.

 Quy khach co the kiem tra trang thai bao hanh thong qua trang web cua chung toi hoac goi den so hotline: 1900-xxxx.

 Tran trong,
 Doi ngu Dich vu Khach hang',
 TRUE),

-- Diagnosis started notification
('DIAGNOSIS_STARTED', 'Thong bao bat dau kiem tra san pham',
 'Kinh gui Quy khach {{customerName}},

 Chung toi dang tien hanh kiem tra va chan doan san pham {{productName}} cua Quy khach.

 Ket qua kiem tra ban dau: {{message}}

 Qua trinh chan doan co the mat tu 1-3 ngay lam viec, tuy thuoc vao tinh trang cua san pham. Chung toi se thong bao cho Quy khach ket qua kiem tra va cac buoc tiep theo.

 Tran trong,
 Doi ngu Ky thuat',
 TRUE),

-- Repair in progress notification
('REPAIR_IN_PROGRESS', 'Cap nhat tien do sua chua san pham',
 'Kinh gui Quy khach {{customerName}},

 Chung toi dang tien hanh sua chua san pham {{productName}} cua Quy khach.

 Thong tin cap nhat: {{message}}

 Du kien qua trinh sua chua se hoan tat trong vong 3-5 ngay lam viec. Chung toi se thong bao cho Quy khach khi san pham da duoc sua chua xong.

 Tran trong,
 Doi ngu Ky thuat',
 TRUE),

-- Repair completed notification
('REPAIR_COMPLETED', 'Thong bao hoan tat sua chua san pham',
 'Kinh gui Quy khach {{customerName}},

 Chung toi vui mung thong bao san pham {{productName}} cua Quy khach da duoc sua chua hoan tat.

 Chi tiet sua chua: {{message}}

 San pham cua Quy khach da duoc kiem tra chat luong va hoat dong tot. Chung toi dang chuan bi gui tra san pham cho Quy khach.

 Tran trong,
 Doi ngu Dich vu Khach hang',
 TRUE),

-- Product shipping notification
('PRODUCT_SHIPPING', 'Thong bao gui tra san pham bao hanh',
 'Kinh gui Quy khach {{customerName}},

 San pham {{productName}} da bao hanh cua Quy khach dang duoc van chuyen ve dia chi dang ky.

 Thong tin van chuyen: {{message}}

 Ma van don: {{trackingNumber}}

 Du kien san pham se duoc giao den Quy khach trong vong 2-3 ngay lam viec. Quy khach co the theo doi trang thai van chuyen thong qua ma van don o tren.

 Tran trong,
 Doi ngu Dich vu Khach hang',
 TRUE),

-- Product delivered notification
('PRODUCT_DELIVERED', 'Xac nhan da giao san pham bao hanh',
 'Kinh gui Quy khach {{customerName}},

 San pham {{productName}} da bao hanh cua Quy khach da duoc giao thanh cong.

 Thong tin bao hanh: {{message}}

 Quy khach vui long kiem tra san pham va cho chung toi biet neu co bat ky van de nao. Chung toi rat mong nhan duoc phan hoi ve chat luong dich vu bao hanh tu Quy khach.

 Tran trong,
 Doi ngu Dich vu Khach hang',
 TRUE),

-- Feedback request notification
('FEEDBACK_REQUEST', 'Danh gia dich vu bao hanh',
 'Kinh gui Quy khach {{customerName}},

 Cam on Quy khach da su dung dich vu bao hanh cua chung toi cho san pham {{productName}}.

 {{message}}

 Quy khach vui long danh vai phut de danh gia chat luong dich vu bang cach nhan vao duong link duoi day:

 {{feedbackLink}}

 Y kien cua Quy khach se giup chung toi cai thien chat luong dich vu tot hon trong tuong lai.

 Tran trong,
 Doi ngu Dich vu Khach hang',
 TRUE);