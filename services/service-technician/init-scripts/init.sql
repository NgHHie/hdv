CREATE DATABASE IF NOT EXISTS service_technician;

USE service_technician;

CREATE TABLE IF NOT EXISTS technicians (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20),
    specialization VARCHAR(100),
    years_of_experience INT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE USER IF NOT EXISTS 'technician_user'@'%' IDENTIFIED BY 'technician_pass';
GRANT ALL PRIVILEGES ON service_technician.* TO 'technician_user'@'%';
FLUSH PRIVILEGES;

-- Insert sample data
INSERT INTO technicians (name, email, phone, specialization, years_of_experience, is_active)
VALUES 
    ('John Smith', 'john.smith@example.com', '1234567890', 'Electronics Repair', 5, true),
    ('Jane Doe', 'jane.doe@example.com', '0987654321', 'Computer Repair', 7, true),
    ('Bob Johnson', 'bob.johnson@example.com', '5551234567', 'Mobile Phone Repair', 3, true),
    ('Alice Williams', 'alice.williams@example.com', '5559876543', 'Audio Equipment Repair', 10, true),
    ('David Brown', 'david.brown@example.com', '4445556666', 'Camera Repair', 8, false);