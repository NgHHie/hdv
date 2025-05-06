CREATE DATABASE IF NOT EXISTS service_survey;

USE service_survey;

-- Create users table
CREATE USER IF NOT EXISTS 'survey_user'@'%' IDENTIFIED BY 'survey_pass';
GRANT ALL PRIVILEGES ON service_survey.* TO 'survey_user'@'%';
FLUSH PRIVILEGES;

-- Create example surveys
INSERT INTO surveys (survey_type, title, description, is_active, created_at, updated_at) VALUES
('REPAIR_FEEDBACK', 'Repair Service Feedback', 'Help us improve our repair service by sharing your experience', true, NOW(), NOW()),
('WARRANTY_FEEDBACK', 'Warranty Service Feedback', 'Share your thoughts about our warranty service', true, NOW(), NOW()),
('CUSTOMER_SATISFACTION', 'Customer Satisfaction Survey', 'We value your feedback about our overall service', true, NOW(), NOW());

-- Create example questions for Repair Service Feedback
INSERT INTO survey_questions (survey_id, question_text, question_type, required, display_order) VALUES
(1, 'How would you rate the overall quality of our repair service?', 'RATING', true, 1),
(1, 'Was your product repaired within the expected timeframe?', 'YES_NO', true, 2),
(1, 'How satisfied are you with the communication during the repair process?', 'RATING', true, 3),
(1, 'How would you rate the professionalism of our technician?', 'RATING', true, 4),
(1, 'Did the repair resolve your issue completely?', 'YES_NO', true, 5),
(1, 'What aspects of our repair service could be improved?', 'TEXTAREA', false, 6),
(1, 'How likely are you to recommend our repair service to others?', 'RATING', true, 7);

-- Create example questions for Warranty Service Feedback
INSERT INTO survey_questions (survey_id, question_text, question_type, required, display_order) VALUES
(2, 'How would you rate the warranty claim process?', 'RATING', true, 1),
(2, 'Was your warranty claim handled efficiently?', 'YES_NO', true, 2),
(2, 'How satisfied are you with the time taken to process your warranty claim?', 'RATING', true, 3),
(2, 'Did you receive clear information about the warranty process?', 'YES_NO', true, 4),
(2, 'What could we improve about our warranty service?', 'TEXTAREA', false, 5),
(2, 'How likely are you to purchase extended warranty for future products?', 'RATING', false, 6);

-- Create example questions for Customer Satisfaction Survey
INSERT INTO survey_questions (survey_id, question_text, question_type, required, display_order) VALUES
(3, 'How satisfied are you with our products?', 'RATING', true, 1),
(3, 'How would you rate our customer service?', 'RATING', true, 2),
(3, 'What is the primary reason you chose our products?', 'SINGLE_CHOICE', true, 3),
(3, 'Which of the following features are important to you?', 'MULTIPLE_CHOICE', false, 4),
(3, 'How likely are you to purchase from us again?', 'RATING', true, 5),
(3, 'Would you recommend our products to friends or colleagues?', 'YES_NO', true, 6),
(3, 'Do you have any suggestions for improvement?', 'TEXTAREA', false, 7);

-- Create options for single/multiple choice questions
INSERT INTO question_options (question_id, option_text, display_order) VALUES
(10, 'Quality', 1),
(10, 'Price', 2),
(10, 'Brand reputation', 3),
(10, 'Customer service', 4),
(10, 'Warranty', 5),
(11, 'Durability', 1),
(11, 'Performance', 2),
(11, 'Design', 3),
(11, 'Ease of use', 4),
(11, 'Price', 5),
(11, 'Warranty coverage', 6);