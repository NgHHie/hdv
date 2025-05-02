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
("iPhone 15 Pro", "Điện thoại Apple mới nhất với chip A17 Pro", 28990000, 50, "smartphones", DATE_ADD(CURDATE(), INTERVAL 12 MONTH), NOW(), NOW()),
("Samsung Galaxy S23 Ultra", "Flagship Samsung với camera 200MP", 25990000, 30, "smartphones", DATE_ADD(CURDATE(), INTERVAL 12 MONTH), NOW(), NOW()),
("MacBook Pro M3", "Laptop Apple với chip M3 Pro mạnh mẽ", 45990000, 15, "laptops", DATE_ADD(CURDATE(), INTERVAL 24 MONTH), NOW(), NOW()),
("Dell XPS 15", "Laptop cao cấp của Dell với màn hình OLED", 35990000, 20, "laptops", DATE_ADD(CURDATE(), INTERVAL 24 MONTH), NOW(), NOW()),
("iPad Air", "Máy tính bảng mỏng nhẹ của Apple", 16990000, 40, "tablets", DATE_ADD(CURDATE(), INTERVAL 12 MONTH), NOW(), NOW()),
("Samsung Galaxy Tab S9", "Máy tính bảng cao cấp với bút S Pen", 18990000, 25, "tablets", DATE_ADD(CURDATE(), INTERVAL 12 MONTH), NOW(), NOW()),
("Acer Nitro Gaming", "Laptop gaming giá rẻ với GPU mạnh", 19990000, 35, "laptops", DATE_ADD(CURDATE(), INTERVAL 12 MONTH), NOW(), NOW()),
("Xiaomi Redmi Note 12", "Điện thoại tầm trung với pin khủng", 4990000, 60, "smartphones", DATE_ADD(CURDATE(), INTERVAL 6 MONTH), NOW(), NOW()),
("Sony WH-1000XM5", "Tai nghe chống ồn cao cấp", 8490000, 45, "accessories", DATE_ADD(CURDATE(), INTERVAL 12 MONTH), NOW(), NOW()),
("Apple Watch Series 9", "Đồng hồ thông minh thế hệ mới", 10990000, 30, "wearables", DATE_ADD(CURDATE(), INTERVAL 12 MONTH), NOW(), NOW()),
("Google Pixel 8", "Điện thoại Google với camera AI", 17990000, 20, "smartphones", DATE_ADD(CURDATE(), INTERVAL 12 MONTH), NOW(), NOW()),
("ASUS ROG Strix", "Laptop gaming cao cấp", 39990000, 10, "laptops", DATE_ADD(CURDATE(), INTERVAL 24 MONTH), NOW(), NOW()),
("AirPods Pro 2", "Tai nghe true wireless với chống ồn", 5990000, 55, "accessories", DATE_ADD(CURDATE(), INTERVAL 12 MONTH), NOW(), NOW()),
("Microsoft Surface Pro 9", "Máy tính bảng lai laptop", 28990000, 15, "tablets", DATE_ADD(CURDATE(), INTERVAL 12 MONTH), NOW(), NOW()),
("Samsung Galaxy Watch 6", "Đồng hồ thông minh với đo sức khỏe", 6990000, 40, "wearables", DATE_ADD(CURDATE(), INTERVAL 12 MONTH), NOW(), NOW()),
("Logitech MX Master 3S", "Chuột không dây cao cấp", 2490000, 50, "accessories", DATE_ADD(CURDATE(), INTERVAL 24 MONTH), NOW(), NOW()),
("Keychron K3", "Bàn phím cơ không dây", 2990000, 30, "accessories", DATE_ADD(CURDATE(), INTERVAL 12 MONTH), NOW(), NOW()),
("Anker GaN Charger", "Sạc nhanh đa cổng", 1490000, 70, "accessories", DATE_ADD(CURDATE(), INTERVAL 6 MONTH), NOW(), NOW()),
("Canon EOS R6", "Máy ảnh mirrorless full-frame", 54990000, 5, "cameras", DATE_ADD(CURDATE(), INTERVAL 24 MONTH), NOW(), NOW()),
("Sony PlayStation 5", "Máy chơi game thế hệ mới", 13990000, 25, "gaming", DATE_ADD(CURDATE(), INTERVAL 12 MONTH), NOW(), NOW())
');

