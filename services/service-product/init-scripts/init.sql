CREATE DATABASE IF NOT EXISTS products CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;


USE products;

-- Tạo bảng products nếu chưa tồn tại
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- -- Xóa dữ liệu hiện có (nếu có)
-- DELETE FROM products;

-- Thêm dữ liệu mẫu cho bảng products
INSERT INTO products (name, description, price, quantity, category, created_at, updated_at) VALUES 
('iPhone 15 Pro', 'Điện thoại Apple mới nhất với chip A17 Pro', 28990000, 50, 'smartphones', NOW(), NOW()),
('Samsung Galaxy S23 Ultra', 'Flagship Samsung với camera 200MP', 25990000, 30, 'smartphones', NOW(), NOW()),
('MacBook Pro M3', 'Laptop Apple với chip M3 Pro mạnh mẽ', 45990000, 15, 'laptops', NOW(), NOW()),
('Dell XPS 15', 'Laptop cao cấp của Dell với màn hình OLED', 35990000, 20, 'laptops', NOW(), NOW()),
('iPad Air', 'Máy tính bảng mỏng nhẹ của Apple', 16990000, 40, 'tablets', NOW(), NOW()),
('Samsung Galaxy Tab S9', 'Máy tính bảng cao cấp với bút S Pen', 18990000, 25, 'tablets', NOW(), NOW()),
('Acer Nitro Gaming', 'Laptop gaming giá rẻ với GPU mạnh', 19990000, 35, 'laptops', NOW(), NOW()),
('Xiaomi Redmi Note 12', 'Điện thoại tầm trung với pin khủng', 4990000, 60, 'smartphones', NOW(), NOW()),
('Sony WH-1000XM5', 'Tai nghe chống ồn cao cấp', 8490000, 45, 'accessories', NOW(), NOW()),
('Apple Watch Series 9', 'Đồng hồ thông minh thế hệ mới', 10990000, 30, 'wearables', NOW(), NOW()),
('Google Pixel 8', 'Điện thoại Google với camera AI', 17990000, 20, 'smartphones', NOW(), NOW()),
('ASUS ROG Strix', 'Laptop gaming cao cấp', 39990000, 10, 'laptops', NOW(), NOW()),
('AirPods Pro 2', 'Tai nghe true wireless với chống ồn', 5990000, 55, 'accessories', NOW(), NOW()),
('Microsoft Surface Pro 9', 'Máy tính bảng lai laptop', 28990000, 15, 'tablets', NOW(), NOW()),
('Samsung Galaxy Watch 6', 'Đồng hồ thông minh với đo sức khỏe', 6990000, 40, 'wearables', NOW(), NOW()),
('Logitech MX Master 3S', 'Chuột không dây cao cấp', 2490000, 50, 'accessories', NOW(), NOW()),
('Keychron K3', 'Bàn phím cơ không dây', 2990000, 30, 'accessories', NOW(), NOW()),
('Anker GaN Charger', 'Sạc nhanh đa cổng', 1490000, 70, 'accessories', NOW(), NOW()),
('Canon EOS R6', 'Máy ảnh mirrorless full-frame', 54990000, 5, 'cameras', NOW(), NOW()),
('Sony PlayStation 5', 'Máy chơi game thế hệ mới', 13990000, 25, 'gaming', NOW(), NOW());