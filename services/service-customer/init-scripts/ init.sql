CREATE DATABASE IF NOT EXISTS service_customer;


USE service_customer;




CREATE USER IF NOT EXISTS 'customer_user'@'%' IDENTIFIED BY 'customer_pass';
GRANT ALL PRIVILEGES ON service_customer.* TO 'customer_user'@'%';
FLUSH PRIVILEGES;


-- Create tables if they don't exist
CREATE TABLE IF NOT EXISTS customer (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone_number VARCHAR(15),
    address VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS purchases (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    purchase_date DATETIME NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    invoice_number VARCHAR(50),
    payment_method VARCHAR(50),
    created_at DATETIME,
    FOREIGN KEY (customer_id) REFERENCES customer(id)
);

CREATE TABLE IF NOT EXISTS purchase_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    purchase_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (purchase_id) REFERENCES purchases(id)
);

-- Set current timestamp
SET @current_timestamp = NOW();

-- Insert sample customers
INSERT INTO customer (first_name, last_name, email, phone_number, address) VALUES 
('Hoang Hiep', 'Nguyen', 'hiep2003ka@gmail.com', '+1-202-555-0123', '123 Main Street, New York, NY 10001'),
('Tuan Dat', 'Trinh Vinh', 'trinhvinhtuandat05102003@gmail.com', '+1-303-555-0456', '456 Park Avenue, Chicago, IL 60601'),
('Michael', 'Williams', 'michael.williams@example.com', '+1-415-555-0789', '789 Ocean Drive, San Francisco, CA 94107'),
('Sophia', 'Brown', 'sophia.brown@example.com', '+1-617-555-0321', '321 Maple Road, Boston, MA 02108'),
('Daniel', 'Jones', 'daniel.jones@example.com', '+1-713-555-0654', '654 Pine Street, Houston, TX 77002'),
('Olivia', 'Garcia', 'olivia.garcia@example.com', '+1-305-555-0987', '987 Palm Boulevard, Miami, FL 33101'),
('William', 'Martinez', 'william.martinez@example.com', '+1-206-555-0147', '147 Cedar Lane, Seattle, WA 98101'),
('Ava', 'Rodriguez', 'ava.rodriguez@example.com', '+1-702-555-0258', '258 Desert Road, Las Vegas, NV 89101'),
('James', 'Wilson', 'james.wilson@example.com', '+1-404-555-0369', '369 Peach Street, Atlanta, GA 30301'),
('Isabella', 'Anderson', 'isabella.anderson@example.com', '+1-901-555-0741', '741 River Road, Memphis, TN 38103');

-- Insert sample purchases
INSERT INTO purchases (customer_id, purchase_date, total_amount, invoice_number, payment_method, created_at) VALUES
-- Customer 1 purchases
(1, DATE_SUB(@current_timestamp, INTERVAL 2 DAY), 3299.97, 'INV-2023-1001', 'Credit Card', @current_timestamp),
(1, DATE_SUB(@current_timestamp, INTERVAL 30 DAY), 1499.99, 'INV-2023-0875', 'PayPal', @current_timestamp),

-- Customer 2 purchases
(2, DATE_SUB(@current_timestamp, INTERVAL 5 DAY), 2399.98, 'INV-2023-0990', 'Credit Card', @current_timestamp),
(2, DATE_SUB(@current_timestamp, INTERVAL 45 DAY), 799.99, 'INV-2023-0822', 'Debit Card', @current_timestamp),

-- Customer 3 purchases
(3, DATE_SUB(@current_timestamp, INTERVAL 7 DAY), 549.98, 'INV-2023-0972', 'Credit Card', @current_timestamp),
(3, DATE_SUB(@current_timestamp, INTERVAL 60 DAY), 2699.99, 'INV-2023-0775', 'Bank Transfer', @current_timestamp),

-- Customer 4 purchases
(4, DATE_SUB(@current_timestamp, INTERVAL 3 DAY), 1099.98, 'INV-2023-0997', 'Credit Card', @current_timestamp),

-- Customer 5 purchases
(5, DATE_SUB(@current_timestamp, INTERVAL 10 DAY), 3499.99, 'INV-2023-0950', 'Finance Plan', @current_timestamp),
(5, DATE_SUB(@current_timestamp, INTERVAL 75 DAY), 649.99, 'INV-2023-0722', 'PayPal', @current_timestamp),

-- Customer 6 purchases
(6, DATE_SUB(@current_timestamp, INTERVAL 15 DAY), 899.97, 'INV-2023-0925', 'Credit Card', @current_timestamp),

-- Customer 7 purchases
(7, DATE_SUB(@current_timestamp, INTERVAL 1 DAY), 4999.99, 'INV-2023-1010', 'Finance Plan', @current_timestamp),
(7, DATE_SUB(@current_timestamp, INTERVAL 90 DAY), 1299.99, 'INV-2023-0685', 'Credit Card', @current_timestamp),

-- Customer 8 purchases
(8, DATE_SUB(@current_timestamp, INTERVAL 4 DAY), 749.99, 'INV-2023-0992', 'PayPal', @current_timestamp),

-- Customer 9 purchases
(9, DATE_SUB(@current_timestamp, INTERVAL 8 DAY), 2199.98, 'INV-2023-0965', 'Credit Card', @current_timestamp),
(9, DATE_SUB(@current_timestamp, INTERVAL 100 DAY), 499.99, 'INV-2023-0650', 'Debit Card', @current_timestamp),

-- Customer 10 purchases
(10, DATE_SUB(@current_timestamp, INTERVAL 6 DAY), 1799.99, 'INV-2023-0980', 'Credit Card', @current_timestamp);

-- Insert sample purchase items
INSERT INTO purchase_items (purchase_id, product_id, quantity, unit_price) VALUES
-- Items for purchase 1 (Customer 1)
(1, 2, 1, 1199.99),  -- Samsung Galaxy S23 Ultra
(1, 6, 1, 2499.99),  -- MacBook Pro 16" M2
(1, 21, 1, 399.99),  -- Apple Watch Series 8

-- Items for purchase 2 (Customer 1)
(2, 9, 1, 1499.99),  -- HP Spectre x360

-- Items for purchase 3 (Customer 2)
(3, 7, 1, 1899.99),  -- Dell XPS 15
(3, 18, 1, 499.99),  -- Sony WH-1000XM5

-- Items for purchase 4 (Customer 2)
(4, 4, 1, 799.99),   -- Xiaomi 13 Pro

-- Items for purchase 5 (Customer 3)
(5, 22, 1, 449.99),  -- Samsung Galaxy Watch 5 Pro
(5, 32, 1, 99.99),   -- Logitech MX Master 3S

-- Items for purchase 6 (Customer 3)
(6, 31, 1, 2699.99), -- Sony Alpha a7 IV

-- Items for purchase 7 (Customer 4)
(7, 12, 1, 1099.99), -- iPad Pro 12.9"

-- Items for purchase 8 (Customer 5)
(8, 46, 1, 3499.99), -- LG C2 65" OLED TV

-- Items for purchase 9 (Customer 5)
(9, 25, 1, 649.99),  -- GoPro HERO11 Black

-- Items for purchase 10 (Customer 6)
(10, 19, 1, 299.99), -- Bose QuietComfort Earbuds II
(10, 20, 1, 449.99), -- Sonos Beam (Gen 2)
(10, 33, 1, 149.99), -- Anker 737 Power Bank

-- Items for purchase 11 (Customer 7)
(11, 41, 1, 4999.99), -- Sony PlayStation 5 (Special Bundle)

-- Items for purchase 12 (Customer 7)
(12, 48, 1, 1299.99), -- TCL 6-Series 75" QLED TV

-- Items for purchase 13 (Customer 8)
(13, 3, 1, 749.99),   -- OnePlus 11 (Discounted)

-- Items for purchase 14 (Customer 9)
(14, 8, 1, 1499.99),  -- Lenovo ThinkPad X1 Carbon
(14, 17, 1, 699.99),  -- Garmin Fenix 7

-- Items for purchase 15 (Customer 9)
(15, 41, 1, 499.99),  -- Sony PlayStation 5

-- Items for purchase 16 (Customer 10)
(16, 50, 1, 1799.99); -- Hisense U8H 65" ULED TV


GRANT SELECT, INSERT, UPDATE, DELETE ON purchases TO 'customer_user'@'%';
GRANT SELECT, INSERT, UPDATE, DELETE ON purchase_items TO 'customer_user'@'%';
FLUSH PRIVILEGES;