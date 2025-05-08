-- Tạo database nếu chưa tồn tại
CREATE DATABASE IF NOT EXISTS service_repair;

USE service_repair;

-- Create repair_requests table first (parent table)
CREATE TABLE IF NOT EXISTS repair_requests (
    id SERIAL PRIMARY KEY,
    warranty_id INTEGER,
    customer_id INTEGER,
    product_id INTEGER,
    issue_description TEXT,
    image_urls TEXT,
    status VARCHAR(50),
    repair_notes TEXT,
    technician_id INTEGER,
    start_date TIMESTAMP,
    end_date TIMESTAMP,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    repair_cost DECIMAL(10, 2),
    within_warranty BOOLEAN
);

-- Create repair_parts table with foreign key to repair_requests
CREATE TABLE IF NOT EXISTS repair_parts (
    id SERIAL PRIMARY KEY,
    repair_id INTEGER REFERENCES repair_requests(id),
    part_name VARCHAR(255),
    part_number VARCHAR(50),
    description TEXT,
    is_warranty_replacement BOOLEAN
);

-- Create repair_status_history table
CREATE TABLE IF NOT EXISTS repair_status_history (
    id SERIAL PRIMARY KEY,
    repair_id INTEGER REFERENCES repair_requests(id),
    status VARCHAR(50),
    notes TEXT,
    created_by VARCHAR(255),
    created_at TIMESTAMP
);

-- Create repair_actions table
CREATE TABLE IF NOT EXISTS repair_actions (
    id SERIAL PRIMARY KEY,
    repair_id INTEGER REFERENCES repair_requests(id),
    action_type VARCHAR(100),
    description TEXT,
    performed_by INTEGER,
    performed_at TIMESTAMP
);

-- Insert parts data with a reference to the repair request
INSERT INTO repair_parts (
    part_name, part_number, description, is_warranty_replacement
) VALUES 
( 'Display Screen', 'SCR-LCD-101', 'LCD display replacement for Samsung Galaxy S21', true),
('Battery', 'BAT-S21-4000', 'Original battery for Samsung Galaxy S21', true),
( 'Power Button', 'BTN-PWR-IP12', 'Power button with flex cable for iPhone 12', false),
( 'Motherboard', 'MB-DELL-XPS15', 'Motherboard for Dell XPS 15 9570', true),
( 'Heat Sink', 'HS-DELL-9570', 'Copper heat sink with fan for Dell XPS', true),
( 'Camera Module', 'CAM-IP13-PRO', 'Rear camera module for iPhone 13 Pro', false),
( 'Speaker', 'SPK-BT-S10', 'Replacement speaker for Samsung S10', true),
( 'Charging Port', 'USB-C-S10', 'USB-C charging port with flex cable', true),
( 'Touch Screen Digitizer', 'DIG-IP11-BLK', 'Touch screen digitizer for iPhone 11 - Black', false),
( 'SSD Drive', 'SSD-512-970EVO', 'Samsung 970 EVO 512GB NVMe SSD', true),
( 'WiFi Antenna', 'ANT-WIFI-MB2019', 'WiFi antenna for MacBook Pro 2019', false),
( 'OLED Panel', 'OLED-IP12-PRO', 'OLED panel for iPhone 12 Pro', true),
( 'Logic Board', 'LB-MBA-2020', 'Logic board for MacBook Air 2020', true);