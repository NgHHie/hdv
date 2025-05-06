CREATE DATABASE IF NOT EXISTS service_product; 

USE service_product;

-- Create product table if it doesn't exist
CREATE TABLE IF NOT EXISTS products (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  description TEXT,
  price DECIMAL(10,2) NOT NULL,
  quantity INT,
  category VARCHAR(100),
  warranty_duration FLOAT,
  serial_number VARCHAR(100) UNIQUE,
  created_at DATETIME,
  updated_at DATETIME
);

-- Get current timestamp for use in all inserts
SET @current_timestamp = NOW();

-- Insert sample electronic products with explicit timestamps
INSERT INTO products (name, description, price, quantity, category, warranty_duration, serial_number, created_at, updated_at) VALUES 
-- Smartphones
('iPhone 14 Pro', 'Latest Apple smartphone with A16 Bionic chip, 48MP camera, and Super Retina XDR display', 999.99, 50, 'Smartphones', 12.0, 'APL-IP14P-1001', @current_timestamp, @current_timestamp),
('Samsung Galaxy S23 Ultra', 'Premium Android smartphone with 200MP camera, S Pen, and Snapdragon 8 Gen 2 processor', 1199.99, 40, 'Smartphones', 12.0, 'SAM-S23U-2001', @current_timestamp, @current_timestamp),
('Google Pixel 7 Pro', 'Google flagship phone with Tensor G2 chip and advanced AI photography features', 899.99, 30, 'Smartphones', 12.0, 'GGL-P7P-3001', @current_timestamp, @current_timestamp),
('Xiaomi 13 Pro', 'Powerful smartphone with Snapdragon 8 Gen 2, Leica optics, and 120W fast charging', 799.99, 25, 'Smartphones', 12.0, 'XIA-13P-4001', @current_timestamp, @current_timestamp),
('OnePlus 11', 'Fast and smooth performance with Snapdragon 8 Gen 2 and Hasselblad camera system', 699.99, 35, 'Smartphones', 12.0, 'ONP-11-5001', @current_timestamp, @current_timestamp),

-- Laptops
('MacBook Pro 16" M2', 'Powerful Apple laptop with M2 Pro/Max chip, Liquid Retina XDR display, and exceptional battery life', 2499.99, 20, 'Laptops', 24.0, 'APL-MBP16-1002', @current_timestamp, @current_timestamp),
('Dell XPS 15', 'Premium Windows laptop with Intel Core i7, NVIDIA RTX graphics, and InfinityEdge display', 1899.99, 15, 'Laptops', 24.0, 'DEL-XPS15-2002', @current_timestamp, @current_timestamp),
('Lenovo ThinkPad X1 Carbon', 'Business laptop with Intel Core i7, impressive battery life, and legendary durability', 1499.99, 25, 'Laptops', 36.0, 'LNV-X1C-3002', @current_timestamp, @current_timestamp),
('HP Spectre x360', 'Convertible laptop with Intel Core i7, OLED display, and elegant gem-cut design', 1399.99, 20, 'Laptops', 24.0, 'HP-SPC360-4002', @current_timestamp, @current_timestamp),
('ASUS ROG Zephyrus G14', 'Powerful gaming laptop with AMD Ryzen 9, NVIDIA RTX graphics in a compact design', 1699.99, 15, 'Laptops', 24.0, 'ASU-ROGZ14-5002', @current_timestamp, @current_timestamp),

-- Tablets
('iPad Pro 12.9"', 'Powerful Apple tablet with M2 chip, Liquid Retina XDR display, and Apple Pencil support', 1099.99, 30, 'Tablets', 12.0, 'APL-IPP129-1003', @current_timestamp, @current_timestamp),
('Samsung Galaxy Tab S8 Ultra', 'Premium Android tablet with 14.6" AMOLED display, S Pen included, and Snapdragon 8 Gen 1', 899.99, 25, 'Tablets', 12.0, 'SAM-GTSU-2003', @current_timestamp, @current_timestamp),
('Microsoft Surface Pro 9', 'Versatile Windows tablet/laptop with Intel Core i5/i7 or Microsoft SQ3 ARM processor', 999.99, 20, 'Tablets', 12.0, 'MSF-SP9-3003', @current_timestamp, @current_timestamp),
('Lenovo Tab P12 Pro', 'Premium Android tablet with 12.6" AMOLED display and Snapdragon 870 processor', 699.99, 15, 'Tablets', 12.0, 'LNV-TP12P-4003', @current_timestamp, @current_timestamp),
('Amazon Fire HD 10', 'Affordable tablet with 10.1" Full HD display and MediaTek octa-core processor', 149.99, 40, 'Tablets', 12.0, 'AMZ-FHD10-5003', @current_timestamp, @current_timestamp),

-- Audio Products
('Sony WH-1000XM5', 'Industry-leading noise cancelling headphones with exceptional sound quality', 399.99, 35, 'Audio', 12.0, 'SNY-WH5-1004', @current_timestamp, @current_timestamp),
('Apple AirPods Pro 2', 'Wireless earbuds with active noise cancellation, spatial audio, and adaptive transparency', 249.99, 45, 'Audio', 12.0, 'APL-APP2-2004', @current_timestamp, @current_timestamp),
('Bose QuietComfort Earbuds II', 'Wireless earbuds with customizable noise cancellation and aware mode', 299.99, 30, 'Audio', 12.0, 'BOS-QCEB2-3004', @current_timestamp, @current_timestamp),
('Sonos Beam (Gen 2)', 'Compact smart soundbar with Dolby Atmos support and voice assistant integration', 449.99, 20, 'Audio', 24.0, 'SON-BM2-4004', @current_timestamp, @current_timestamp),
('JBL Flip 6', 'Portable Bluetooth speaker with powerful sound and IP67 waterproof rating', 129.99, 50, 'Audio', 12.0, 'JBL-FLP6-5004', @current_timestamp, @current_timestamp),

-- Wearables
('Apple Watch Series 8', 'Advanced health features, crack-resistant display, and all-day battery life', 399.99, 40, 'Wearables', 12.0, 'APL-AWS8-1005', @current_timestamp, @current_timestamp),
('Samsung Galaxy Watch 5 Pro', 'Durable smartwatch with advanced health tracking and Google Wear OS', 449.99, 30, 'Wearables', 12.0, 'SAM-GW5P-2005', @current_timestamp, @current_timestamp),
('Garmin Fenix 7', 'Premium multisport GPS watch with advanced training features and solar charging', 699.99, 20, 'Wearables', 12.0, 'GRM-FNX7-3005', @current_timestamp, @current_timestamp),
('Fitbit Sense 2', 'Advanced health smartwatch with stress management and ECG app', 299.99, 35, 'Wearables', 12.0, 'FIT-SNS2-4005', @current_timestamp, @current_timestamp),
('Oura Ring Gen 3', 'Smart ring that tracks sleep, activity, and readiness with precise sensors', 299.99, 15, 'Wearables', 12.0, 'OUR-RG3-5005', @current_timestamp, @current_timestamp),

-- Cameras
('Sony Alpha a7 IV', 'Full-frame mirrorless camera with 33MP resolution and advanced autofocus system', 2499.99, 15, 'Cameras', 24.0, 'SNY-A7IV-1006', @current_timestamp, @current_timestamp),
('Canon EOS R6 Mark II', 'Full-frame mirrorless camera with 24MP resolution and in-body image stabilization', 2399.99, 12, 'Cameras', 24.0, 'CAN-R6M2-2006', @current_timestamp, @current_timestamp),
('Nikon Z6 II', 'Versatile full-frame mirrorless camera with dual processors and 4K60p video', 1999.99, 10, 'Cameras', 24.0, 'NIK-Z6II-3006', @current_timestamp, @current_timestamp),
('Fujifilm X-T5', 'Compact mirrorless camera with 40MP APS-C sensor and classic tactile controls', 1699.99, 18, 'Cameras', 24.0, 'FUJ-XT5-4006', @current_timestamp, @current_timestamp),
('GoPro HERO11 Black', 'Waterproof action camera with 5.3K video, 27MP photos, and HyperSmooth stabilization', 499.99, 25, 'Cameras', 12.0, 'GPR-H11B-5006', @current_timestamp, @current_timestamp),

-- Smart Home
('Amazon Echo Show 10', 'Smart display with motion tracking, premium sound, and Alexa integration', 249.99, 30, 'Smart Home', 12.0, 'AMZ-ES10-1007', @current_timestamp, @current_timestamp),
('Google Nest Hub Max', 'Smart display with Google Assistant, camera, and premium audio', 229.99, 25, 'Smart Home', 12.0, 'GGL-NHM-2007', @current_timestamp, @current_timestamp),
('Philips Hue Starter Kit', 'Smart lighting system with bridge and 3 color bulbs', 199.99, 40, 'Smart Home', 24.0, 'PHL-HSK-3007', @current_timestamp, @current_timestamp),
('Ring Video Doorbell Pro 2', 'Advanced video doorbell with head-to-toe view and 3D motion detection', 249.99, 35, 'Smart Home', 12.0, 'RNG-VDP2-4007', @current_timestamp, @current_timestamp),
('Ecobee SmartThermostat Premium', 'Smart thermostat with voice control, air quality monitor, and smart sensor', 249.99, 20, 'Smart Home', 36.0, 'ECO-STP-5007', @current_timestamp, @current_timestamp),

-- Gaming
('Sony PlayStation 5', 'Next-gen gaming console with ultra-high-speed SSD and ray tracing support', 499.99, 15, 'Gaming', 12.0, 'SNY-PS5-1008', @current_timestamp, @current_timestamp),
('Microsoft Xbox Series X', 'Powerful gaming console with 4K gaming at up to 120 FPS and quick resume', 499.99, 18, 'Gaming', 12.0, 'MSF-XBSX-2008', @current_timestamp, @current_timestamp),
('Nintendo Switch OLED', 'Hybrid gaming console with vibrant 7" OLED screen and enhanced audio', 349.99, 25, 'Gaming', 12.0, 'NIN-SWO-3008', @current_timestamp, @current_timestamp),
('Valve Steam Deck', 'Handheld gaming PC with custom AMD APU and versatile gaming options', 399.99, 10, 'Gaming', 12.0, 'VLV-STD-4008', @current_timestamp, @current_timestamp),
('Meta Quest 2', 'Standalone VR headset with immersive experiences and intuitive controls', 299.99, 20, 'Gaming', 12.0, 'META-QST2-5008', @current_timestamp, @current_timestamp),

-- Computer Accessories
('Logitech MX Master 3S', 'Advanced wireless mouse with ultra-fast scrolling and quiet clicks', 99.99, 50, 'Computer Accessories', 24.0, 'LOG-MXM3S-1009', @current_timestamp, @current_timestamp),
('Keychron Q1 Pro', 'Wireless mechanical keyboard with QMK/VIA support and aluminum body', 199.99, 30, 'Computer Accessories', 12.0, 'KEY-Q1P-2009', @current_timestamp, @current_timestamp),
('Samsung T7 Shield 2TB', 'Rugged portable SSD with IP65 rating and 1,050MB/s transfer speeds', 229.99, 40, 'Computer Accessories', 36.0, 'SAM-T7S2-3009', @current_timestamp, @current_timestamp),
('LG UltraGear 27GP950', '27" 4K gaming monitor with 144Hz refresh rate and NVIDIA G-Sync support', 899.99, 15, 'Computer Accessories', 36.0, 'LG-UG27-4009', @current_timestamp, @current_timestamp),
('Anker 737 Power Bank', '24,000mAh portable battery with 140W output and smart digital display', 149.99, 45, 'Computer Accessories', 24.0, 'ANK-737PB-5009', @current_timestamp, @current_timestamp),

-- TVs
('LG C2 65" OLED TV', '4K OLED TV with perfect blacks, Dolby Vision, and gaming features', 1799.99, 10, 'TVs', 24.0, 'LG-C265-1010', @current_timestamp, @current_timestamp),
('Samsung QN90B 55" QLED TV', 'Neo QLED 4K TV with Mini-LED backlighting and anti-reflection screen', 1499.99, 12, 'TVs', 24.0, 'SAM-QN90B55-2010', @current_timestamp, @current_timestamp),
('Sony A95K 65" QD-OLED TV', 'Quantum Dot OLED TV with exceptional color and contrast', 2999.99, 8, 'TVs', 24.0, 'SNY-A95K65-3010', @current_timestamp, @current_timestamp),
('TCL 6-Series 75" QLED TV', 'Affordable QLED TV with Mini-LED technology and Google TV', 1299.99, 15, 'TVs', 24.0, 'TCL-6S75-4010', @current_timestamp, @current_timestamp),
('Hisense U8H 65" ULED TV', 'Mini-LED TV with quantum dot technology and bright HDR performance', 999.99, 18, 'TVs', 24.0, 'HIS-U8H65-5010', @current_timestamp, @current_timestamp);