CREATE DATABASE IF NOT EXISTS service_product; ;

USE service_product;


CREATE TABLE IF NOT EXISTS `products` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` text,
  `price` decimal(19,2) NOT NULL,
  `quantity` int,
  `category` varchar(100),
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `idx_product_category` (`category`),
  INDEX `idx_product_name` (`name`)
);


GRANT ALL PRIVILEGES ON service_product.* TO 'product_user'@'%';
FLUSH PRIVILEGES;

INSERT INTO products (name, description, price, quantity, category, created_at, updated_at) VALUES 
('iPhone 15 Pro', 'Apple\'s latest smartphone with A17 Pro chip', 28990000, 50, 'smartphones', NOW(), NOW()),
('Samsung Galaxy S23 Ultra', 'Samsung flagship with 200MP camera', 25990000, 30, 'smartphones', NOW(), NOW()),
('MacBook Pro M3', 'Apple laptop powered by powerful M3 Pro chip', 45990000, 15, 'laptops', NOW(), NOW()),
('Dell XPS 15', 'Premium Dell laptop with OLED display', 35990000, 20, 'laptops', NOW(), NOW()),
('iPad Air', 'Lightweight and thin Apple tablet', 16990000, 40, 'tablets', NOW(), NOW()),
('Samsung Galaxy Tab S9', 'High-end tablet with S Pen support', 18990000, 25, 'tablets', NOW(), NOW()),
('Acer Nitro Gaming', 'Budget gaming laptop with powerful GPU', 19990000, 35, 'laptops', NOW(), NOW()),
('Xiaomi Redmi Note 12', 'Mid-range phone with massive battery', 4990000, 60, 'smartphones', NOW(), NOW()),
('Sony WH-1000XM5', 'Premium noise-canceling headphones', 8490000, 45, 'accessories', NOW(), NOW()),
('Apple Watch Series 9', 'Next-gen Apple smartwatch', 10990000, 30, 'wearables', NOW(), NOW()),
('Google Pixel 8', 'Google phone with AI-powered camera', 17990000, 20, 'smartphones', NOW(), NOW()),
('ASUS ROG Strix', 'High-performance gaming laptop', 39990000, 10, 'laptops', NOW(), NOW()),
('AirPods Pro 2', 'True wireless earbuds with noise cancellation', 5990000, 55, 'accessories', NOW(), NOW()),
('Microsoft Surface Pro 9', 'Tablet-laptop hybrid from Microsoft', 28990000, 15, 'tablets', NOW(), NOW()),
('Samsung Galaxy Watch 6', 'Smartwatch with health tracking features', 6990000, 40, 'wearables', NOW(), NOW()),
('Logitech MX Master 3S', 'High-end wireless mouse', 2490000, 50, 'accessories', NOW(), NOW()),
('Keychron K3', 'Wireless mechanical keyboard', 2990000, 30, 'accessories', NOW(), NOW()),
('Anker GaN Charger', 'Fast multi-port USB charger', 1490000, 70, 'accessories', NOW(), NOW()),
('Canon EOS R6', 'Full-frame mirrorless camera', 54990000, 5, 'cameras', NOW(), NOW()),
('Sony PlayStation 5', 'Next-gen gaming console', 13990000, 25, 'gaming', NOW(), NOW());
