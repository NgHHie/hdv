CREATE DATABASE IF NOT EXISTS service_customer;


USE service_customer;


CREATE TABLE IF NOT EXISTS customer (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone_number VARCHAR(15),
    address VARCHAR(255)
);

CREATE USER IF NOT EXISTS 'customer_user'@'%' IDENTIFIED BY 'customer_pass';
GRANT ALL PRIVILEGES ON service_customer.* TO 'customer_user'@'%';
FLUSH PRIVILEGES;


INSERT INTO customer (first_name, last_name, email, phone_number, address) VALUES 
    ('John', 'Doe', 'john.doe@example.com', '1234567890', '123 Main St'),
    ('Jane', 'Smith', 'jane.smith@example.com', '0987654321', '456 Oak Ave'),
    ('Michael', 'Johnson', 'michael.johnson@example.com', '5551234567', '789 Pine St');



-- Tạo bảng purchases nếu chưa tồn tại
CREATE TABLE IF NOT EXISTS purchases (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    purchase_date DATETIME NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    invoice_number VARCHAR(50),
    payment_method VARCHAR(50),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customer(id)
);

-- Tạo bảng purchase_items nếu chưa tồn tại
CREATE TABLE IF NOT EXISTS purchase_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    purchase_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    warranty_expiration_date DATE,
    FOREIGN KEY (purchase_id) REFERENCES purchases(id)
);

-- Insert dữ liệu mẫu vào bảng purchases
INSERT INTO purchases (customer_id, purchase_date, total_amount, invoice_number, payment_method, created_at)
VALUES 
    (1, '2023-01-15 10:30:00', 48980000.00, 'INV-20230115-001', 'Credit Card', '2023-01-15 10:30:00'),
    (1, '2023-03-20 14:45:00', 28990000.00, 'INV-20230320-002', 'Cash', '2023-03-20 14:45:00'),
    (2, '2023-02-10 09:15:00', 54480000.00, 'INV-20230210-003', 'Bank Transfer', '2023-02-10 09:15:00'),
    (3, '2023-04-05 16:20:00', 19990000.00, 'INV-20230405-004', 'Credit Card', '2023-04-05 16:20:00'),
    (2, '2023-05-12 11:40:00', 63480000.00, 'INV-20230512-005', 'Mobile Payment', '2023-05-12 11:40:00'),
    (3, '2023-06-18 13:25:00', 17990000.00, 'INV-20230618-006', 'Cash', '2023-06-18 13:25:00'),
    (1, '2023-08-02 10:10:00', 16990000.00, 'INV-20230802-007', 'Credit Card', '2023-08-02 10:10:00'),
    (2, '2023-09-15 15:30:00', 13990000.00, 'INV-20230915-008', 'Bank Transfer', '2023-09-15 15:30:00');

-- Insert dữ liệu mẫu vào bảng purchase_items với tham chiếu đến các sản phẩm thực tế từ service_product
INSERT INTO purchase_items (purchase_id, product_id, quantity, unit_price, warranty_expiration_date)
VALUES 
    -- Đơn hàng 1: MacBook Pro M3 và AirPods Pro 2
    (1, 3, 1, 45990000.00, '2025-01-15'), -- MacBook Pro M3 có bảo hành 2 năm
    (1, 13, 1, 5990000.00, '2024-01-15'), -- AirPods Pro 2 có bảo hành 1 năm
    
    -- Đơn hàng 2: iPhone 15 Pro
    (2, 1, 1, 28990000.00, '2024-03-20'), -- iPhone 15 Pro có bảo hành 1 năm
    
    -- Đơn hàng 3: Dell XPS 15 và Sony WH-1000XM5
    (3, 4, 1, 35990000.00, '2025-02-10'), -- Dell XPS 15 có bảo hành 2 năm
    (3, 9, 1, 8490000.00, '2024-02-10'), -- Sony WH-1000XM5 có bảo hành 1 năm
    (3, 18, 1, 1490000.00, NULL), -- Anker GaN Charger không tính bảo hành
    
    -- Đơn hàng 4: Acer Nitro Gaming
    (4, 7, 1, 19990000.00, '2024-04-05'), -- Acer Nitro Gaming có bảo hành 1 năm
    
    -- Đơn hàng 5: ASUS ROG Strix, Keychron K3 và Logitech MX Master 3S
    (5, 12, 1, 39990000.00, '2025-05-12'), -- ASUS ROG Strix có bảo hành 2 năm
    (5, 17, 1, 2990000.00, '2024-05-12'), -- Keychron K3 có bảo hành 1 năm
    (5, 16, 1, 2490000.00, '2025-05-12'), -- Logitech MX Master 3S có bảo hành 2 năm
    
    -- Đơn hàng 6: Google Pixel 8
    (6, 11, 1, 17990000.00, '2024-06-18'), -- Google Pixel 8 có bảo hành 1 năm
    
    -- Đơn hàng 7: iPad Air
    (7, 5, 1, 16990000.00, '2024-08-02'), -- iPad Air có bảo hành 1 năm
    
    -- Đơn hàng 8: PlayStation 5
    (8, 20, 1, 13990000.00, '2024-09-15'); -- PlayStation 5 có bảo hành 1 năm

-- Tạo view để dễ dàng truy vấn các mặt hàng có bảo hành
CREATE OR REPLACE VIEW view_purchase_items_with_warranty AS
SELECT 
    pi.id,
    pi.purchase_id,
    p.customer_id,
    pi.product_id,
    pi.quantity,
    pi.unit_price,
    pi.warranty_expiration_date,
    CONCAT(c.first_name, ' ', c.last_name) AS customer_name,
    p.purchase_date,
    p.invoice_number
FROM 
    purchase_items pi
JOIN 
    purchases p ON pi.purchase_id = p.id
JOIN 
    customer c ON p.customer_id = c.id
WHERE 
    pi.warranty_expiration_date IS NOT NULL;

-- Cấp quyền cho user
GRANT SELECT, INSERT, UPDATE, DELETE ON purchases TO 'customer_user'@'%';
GRANT SELECT, INSERT, UPDATE, DELETE ON purchase_items TO 'customer_user'@'%';
GRANT SELECT ON view_purchase_items_with_warranty TO 'customer_user'@'%';
FLUSH PRIVILEGES;