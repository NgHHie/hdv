CREATE DATABASE IF NOT EXISTS service_product; 

USE service_product;


CREATE TABLE IF NOT EXISTS `products` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` text,
  `price` decimal(19,2) NOT NULL,
  `quantity` int,
  `category` varchar(100),
  `warranty_expiration` date,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `idx_product_category` (`category`),
  INDEX `idx_product_name` (`name`)
);

CREATE USER IF NOT EXISTS 'product_user'@'%' IDENTIFIED BY 'product_pass';
GRANT ALL PRIVILEGES ON service_product.* TO 'product_user'@'%';
FLUSH PRIVILEGES;

INSERT INTO products (name, description, price, quantity, category, warranty_expiration, created_at, updated_at) VALUES 
("iPhone 15 Pro", "Latest Apple phone with A17 Pro chip", 28990000, 50, "smartphones", DATE_ADD(CURDATE(), INTERVAL 12 MONTH), NOW(), NOW()),
("Samsung Galaxy S23 Ultra", "Samsung flagship with 200MP camera", 25990000, 30, "smartphones", DATE_ADD(CURDATE(), INTERVAL 12 MONTH), NOW(), NOW()),
("MacBook Pro M3", "Apple laptop with powerful M3 Pro chip", 45990000, 15, "laptops", DATE_ADD(CURDATE(), INTERVAL 24 MONTH), NOW(), NOW()),
("Dell XPS 15", "Premium Dell laptop with OLED display", 35990000, 20, "laptops", DATE_ADD(CURDATE(), INTERVAL 24 MONTH), NOW(), NOW()),
("iPad Air", "Lightweight Apple tablet", 16990000, 40, "tablets", DATE_ADD(CURDATE(), INTERVAL 12 MONTH), NOW(), NOW()),
("Samsung Galaxy Tab S9", "Premium tablet with S Pen", 18990000, 25, "tablets", DATE_ADD(CURDATE(), INTERVAL 12 MONTH), NOW(), NOW()),
("Acer Nitro Gaming", "Budget gaming laptop with powerful GPU", 19990000, 35, "laptops", DATE_ADD(CURDATE(), INTERVAL 12 MONTH), NOW(), NOW()),
("Xiaomi Redmi Note 12", "Mid-range phone with huge battery", 4990000, 60, "smartphones", DATE_ADD(CURDATE(), INTERVAL 6 MONTH), NOW(), NOW()),
("Sony WH-1000XM5", "Premium noise-cancelling headphones", 8490000, 45, "accessories", DATE_ADD(CURDATE(), INTERVAL 12 MONTH), NOW(), NOW()),
("Apple Watch Series 9", "Next-generation smartwatch", 10990000, 30, "wearables", DATE_ADD(CURDATE(), INTERVAL 12 MONTH), NOW(), NOW()),
("Google Pixel 8", "Google phone with AI camera", 17990000, 20, "smartphones", DATE_ADD(CURDATE(), INTERVAL 12 MONTH), NOW(), NOW()),
("ASUS ROG Strix", "Premium gaming laptop", 39990000, 10, "laptops", DATE_ADD(CURDATE(), INTERVAL 24 MONTH), NOW(), NOW()),
("AirPods Pro 2", "True wireless earbuds with noise cancellation", 5990000, 55, "accessories", DATE_ADD(CURDATE(), INTERVAL 12 MONTH), NOW(), NOW()),
("Microsoft Surface Pro 9", "Tablet-laptop hybrid", 28990000, 15, "tablets", DATE_ADD(CURDATE(), INTERVAL 12 MONTH), NOW(), NOW()),
("Samsung Galaxy Watch 6", "Smartwatch with health monitoring", 6990000, 40, "wearables", DATE_ADD(CURDATE(), INTERVAL 12 MONTH), NOW(), NOW()),
("Logitech MX Master 3S", "Premium wireless mouse", 2490000, 50, "accessories", DATE_ADD(CURDATE(), INTERVAL 24 MONTH), NOW(), NOW()),
("Keychron K3", "Wireless mechanical keyboard", 2990000, 30, "accessories", DATE_ADD(CURDATE(), INTERVAL 12 MONTH), NOW(), NOW()),
("Anker GaN Charger", "Multi-port fast charger", 1490000, 70, "accessories", DATE_ADD(CURDATE(), INTERVAL 6 MONTH), NOW(), NOW()),
("Canon EOS R6", "Full-frame mirrorless camera", 54990000, 5, "cameras", DATE_ADD(CURDATE(), INTERVAL 24 MONTH), NOW(), NOW()),
("Sony PlayStation 5", "Next-generation gaming console", 13990000, 25, "gaming", DATE_ADD(CURDATE(), INTERVAL 12 MONTH), NOW(), NOW())
');

