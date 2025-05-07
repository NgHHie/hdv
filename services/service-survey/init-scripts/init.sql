-- Tạo database nếu chưa tồn tại
CREATE DATABASE IF NOT EXISTS service_survey;

USE service_survey;

-- Tạo bảng surveys (khảo sát)
CREATE TABLE IF NOT EXISTS surveys (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    active BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tạo bảng questions (câu hỏi)
CREATE TABLE IF NOT EXISTS questions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    survey_id BIGINT NOT NULL,
    question_text TEXT NOT NULL,
    question_order INT NOT NULL,
    required BOOLEAN DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (survey_id) REFERENCES surveys(id) ON DELETE CASCADE
);

-- Tạo bảng responses (phản hồi khảo sát)
CREATE TABLE IF NOT EXISTS responses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    survey_id BIGINT NOT NULL,
    customer_id BIGINT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (survey_id) REFERENCES surveys(id) ON DELETE CASCADE
);

-- Tạo bảng response_answers (câu trả lời cho phản hồi)
CREATE TABLE IF NOT EXISTS response_answers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    response_id BIGINT NOT NULL,
    question_id BIGINT NOT NULL,
    answer_text TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (response_id) REFERENCES responses(id) ON DELETE CASCADE,
    FOREIGN KEY (question_id) REFERENCES questions(id) ON DELETE CASCADE
);

-- Tạo một số dữ liệu mẫu
INSERT INTO surveys (title, description, active) VALUES
('Khảo sát phản hồi dịch vụ sửa chữa', 'Giúp chúng tôi cải thiện dịch vụ sửa chữa bằng cách chia sẻ trải nghiệm của bạn', true),
('Khảo sát mức độ hài lòng của khách hàng', 'Chúng tôi đánh giá cao phản hồi của bạn về dịch vụ tổng thể của chúng tôi', true);

-- Tạo câu hỏi mẫu cho khảo sát phản hồi dịch vụ sửa chữa
INSERT INTO questions (survey_id, question_text, question_order, required) VALUES
(1, 'Bạn đánh giá chất lượng dịch vụ sửa chữa của chúng tôi như thế nào?', 1, true),
(1, 'Sản phẩm của bạn có được sửa chữa trong thời gian dự kiến không?', 2, true),
(1, 'Bạn hài lòng với cách giao tiếp trong quá trình sửa chữa như thế nào?', 3, true),
(1, 'Bạn đánh giá tính chuyên nghiệp của kỹ thuật viên của chúng tôi như thế nào?', 4, true),
(1, 'Việc sửa chữa có giải quyết vấn đề của bạn hoàn toàn không?', 5, true),
(1, 'Khía cạnh nào của dịch vụ sửa chữa của chúng tôi có thể được cải thiện?', 6, false);

-- Tạo câu hỏi mẫu cho khảo sát mức độ hài lòng của khách hàng
INSERT INTO questions (survey_id, question_text, question_order, required) VALUES
(2, 'Bạn hài lòng với sản phẩm của chúng tôi đến mức nào?', 1, true),
(2, 'Bạn đánh giá dịch vụ khách hàng của chúng tôi như thế nào?', 2, true),
(2, 'Lý do chính bạn chọn sản phẩm của chúng tôi là gì?', 3, true),
(2, 'Bạn có khả năng sẽ mua lại từ chúng tôi không?', 4, true),
(2, 'Bạn có đề xuất cải tiến nào không?', 5, false);