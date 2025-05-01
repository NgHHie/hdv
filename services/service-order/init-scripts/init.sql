CREATE DATABASE IF NOT EXISTS service_order;

USE service_order;
DROP TABLE IF EXISTS order_item;
DROP TABLE IF EXISTS orders;

CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    order_number VARCHAR(50) NOT NULL,
    total_amount DECIMAL(12, 2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    order_date DATETIME NOT NULL,
    shipping_address VARCHAR(255) NOT NULL,
    payment_method VARCHAR(50) NOT NULL
);

CREATE TABLE order_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    product_name VARCHAR(100) NOT NULL, 
    product_category VARCHAR(50) NOT NULL,
    price DECIMAL(12, 2) NOT NULL,
    quantity INT NOT NULL,
    warranty_expiration DATE,
    FOREIGN KEY (order_id) REFERENCES orders(id)
);


CREATE USER IF NOT EXISTS 'order_user'@'%' IDENTIFIED BY 'order_pass';
GRANT ALL PRIVILEGES ON service_order.* TO 'order_user'@'%';
FLUSH PRIVILEGES;


INSERT INTO orders (customer_id, order_number, total_amount, status, order_date, shipping_address, payment_method)
VALUES 
    (1, 'ORD-2025-001', 31480000, 'COMPLETED', '2025-04-20 10:30:00', '123 Main St', 'CREDIT_CARD'),
    (2, 'ORD-2025-002', 48980000, 'PROCESSING', '2025-04-21 14:15:00', '456 Oak Ave', 'BANK_TRANSFER'),
    (3, 'ORD-2025-003', 8490000, 'SHIPPED', '2025-04-22 09:45:00', '789 Pine St', 'COD'),
    (1, 'ORD-2025-004', 22980000, 'PENDING', '2025-04-23 16:20:00', '123 Main St', 'MOMO'),
    (3, 'ORD-2025-005', 66480000, 'COMPLETED', '2025-04-24 11:10:00', '789 Pine St', 'CREDIT_CARD');


-- Order 1: ORD-2025-001
INSERT INTO order_item (order_id, product_id, product_name, product_category, price, quantity, warranty_expiration)
VALUES
    (1, 1, 'iPhone 15 Pro', 'smartphones', 28990000, 1, DATE_ADD('2025-04-20', INTERVAL 1 MONTH)),
    (1, 16, 'Logitech MX Master 3S', 'accessories', 2490000, 1, NULL);

-- Order 2: ORD-2025-002
INSERT INTO order_item (order_id, product_id, product_name, product_category, price, quantity, warranty_expiration)
VALUES
    (2, 3, 'MacBook Pro M3', 'laptops', 45990000, 1, DATE_ADD('2025-04-21', INTERVAL 1 MONTH)),
    (2, 17, 'Keychron K3', 'accessories', 2990000, 1, DATE_ADD('2025-04-21', INTERVAL 1 MONTH));

-- Order 3: ORD-2025-003
INSERT INTO order_item (order_id, product_id, product_name, product_category, price, quantity, warranty_expiration)
VALUES
    (3, 9, 'Sony WH-1000XM5', 'accessories', 8490000, 1, DATE_ADD('2025-04-22', INTERVAL 1 MONTH));

-- Order 4: ORD-2025-004
INSERT INTO order_item (order_id, product_id, product_name, product_category, price, quantity, warranty_expiration)
VALUES
    (4, 5, 'iPad Air', 'tablets', 16990000, 1, DATE_ADD('2025-04-23', INTERVAL 1 MONTH)),
    (4, 13, 'AirPods Pro 2', 'accessories', 5990000, 1, NULL);

-- Order 5: ORD-2025-005
INSERT INTO order_item (order_id, product_id, product_name, product_category, price, quantity, warranty_expiration)
VALUES
    (5, 19, 'Canon EOS R6', 'cameras', 54990000, 1, DATE_ADD('2025-04-24', INTERVAL 1 MONTH)),
    (5, 18, 'Anker GaN Charger', 'accessories', 1490000, 1, NULL),
    (5, 10, 'Apple Watch Series 9', 'wearables', 10990000, 1, DATE_ADD('2025-04-24', INTERVAL 1 MONTH));