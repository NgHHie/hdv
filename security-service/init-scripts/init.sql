-- Create database if it doesn't exist
CREATE DATABASE IF NOT EXISTS service_security;

USE service_security;

-- Create user if it doesn't exist
CREATE USER IF NOT EXISTS 'security_user'@'%' IDENTIFIED BY 'security_pass';
GRANT ALL PRIVILEGES ON service_security.* TO 'security_user'@'%';
FLUSH PRIVILEGES;

-- Bảng permissions
CREATE TABLE IF NOT EXISTS permissions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    path VARCHAR(255) NOT NULL,
    method VARCHAR(10) NOT NULL CHECK (method IN ('GET', 'POST', 'PUT', 'DELETE', 'PATCH')),
    description VARCHAR(255)
);

-- Bảng roles
CREATE TABLE IF NOT EXISTS roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    description VARCHAR(100),
    CONSTRAINT chk_role_name CHECK (name IN ('ROLE_ADMIN', 'ROLE_TECHNICIAN', 'ROLE_CUSTOMER'))
);

-- Bảng role_permissions (nhiều-nhiều giữa roles và permissions)
CREATE TABLE IF NOT EXISTS role_permissions (
    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES permissions(id) ON DELETE CASCADE
);

-- Bảng users
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    password VARCHAR(120) NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    active BOOLEAN DEFAULT TRUE,
    role_id BIGINT,
    FOREIGN KEY (role_id) REFERENCES roles(id),
    UNIQUE (username),
    UNIQUE (email)
);

-- Insert roles
INSERT INTO roles (id, name, description) VALUES 
(1, 'ROLE_ADMIN', 'Administrator'),
(2, 'ROLE_CUSTOMER', 'Customers'),
(3, 'ROLE_TECHNICIAN', 'Technicians');

-- Insert permissions
INSERT INTO permissions (id, name, path, method, description) VALUES
(1, 'read customer', '/api/v1/customer/**', 'GET', 'read customer'),
(2, 'write customer', '/api/v1/customer/**', 'POST', 'write customer'),
(3, 'update customer', '/api/v1/customer/**', 'PUT', 'update customer'),
(4, 'delete customer', '/api/v1/customer/**', 'DELETE', 'delete customer'),
(5, 'read notification', '/api/v1/notifications/**', 'GET', 'read notification'),
(6, 'write notification', '/api/v1/notifications/**', 'POST', 'write notification'),
(7, 'update notification', '/api/v1/notifications/**', 'PUT', 'update notification'),
(8, 'delete notification', '/api/v1/notifications/**', 'DELETE', 'delete notification'),
(9, 'read product', '/api/v1/products/**', 'GET', 'read product'),
(10, 'write product', '/api/v1/products/**', 'POST', 'write product'),
(11, 'update product', '/api/v1/products/**', 'PUT', 'update product'),
(12, 'delete product', '/api/v1/products/**', 'DELETE', 'delete product'),
(13, 'read survey', '/api/v1/surveys/**', 'GET', 'read survey'),
(14, 'write survey', '/api/v1/surveys/**', 'POST', 'write survey'),
(15, 'update survey', '/api/v1/surveys/**', 'PUT', 'update survey'),
(16, 'delete survey', '/api/v1/surveys/**', 'DELETE', 'delete survey'),
(17, 'read technician', '/api/v1/technicians/**', 'GET', 'read technician'),
(18, 'write technician', '/api/v1/technicians/**', 'POST', 'write technician'),
(19, 'update technician', '/api/v1/technicians/**', 'PUT', 'update technician'),
(20, 'delete technician', '/api/v1/technicians/**', 'DELETE', 'delete technician'),
(21, 'read warranty', '/api/v1/warranty/**', 'GET', 'read warranty'),
(22, 'write warranty', '/api/v1/warranty/**', 'POST', 'write warranty'),
(23, 'update warranty', '/api/v1/warranty/**', 'PUT', 'update warranty'),
(24, 'delete warranty', '/api/v1/warranty/**', 'DELETE', 'delete warranty'),
(25, 'read repair', '/api/v1/repairs/**', 'GET', 'read repair'),
(26, 'write repair', '/api/v1/repairs/**', 'POST', 'write repair'),
(27, 'update repair', '/api/v1/repairs/**', 'PUT', 'update repair'),
(28, 'delete repair', '/api/v1/repairs/**', 'DELETE', 'delete repair'),
(29, 'read user', '/api/users/**', 'GET', 'read user'),
(30, 'write user', '/api/users/**', 'POST', 'write user'),
(31, 'update user', '/api/users/**', 'PUT', 'update user'),
(32, 'delete user', '/api/users/**', 'DELETE', 'delete user'),
(33, 'read condition', '/api/conditions/**', 'GET', 'read condition'),
(34, 'write condition', '/api/conditions/**', 'POST', 'write condition'),
(35, 'update condition', '/api/conditions/**', 'PUT', 'update condition'),
(36, 'delete condition', '/api/conditions/**', 'DELETE', 'delete condition');

-- Assign permissions to roles
INSERT INTO role_permissions (role_id, permission_id) VALUES
-- Admin permissions (all)
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7), (1, 8),
(1, 9), (1, 10), (1, 11), (1, 12), (1, 13), (1, 14), (1, 15), (1, 16),
(1, 17), (1, 18), (1, 19), (1, 20), (1, 21), (1, 22), (1, 23), (1, 24),
(1, 25), (1, 26), (1, 27), (1, 28), (1, 29), (1, 30), (1, 31), (1, 32),
(1, 33), (1, 34), (1, 35), (1, 36),

-- Customer permissions
(2, 1), -- read customer
(2, 3), -- update customer
(2, 5), -- read notification
(2, 9), -- read product
(2, 13), -- read survey
(2, 14), -- write survey
(2, 22), -- write warranty

-- Technician permissions
(3, 5), -- read notification
(3, 17), -- read technician
(3, 19), -- update technician
(3, 25), -- read repair
(3, 26), -- write repair
(3, 27), -- update repair
(3, 28); -- delete repair

-- Create an admin user
INSERT INTO users (username, email, password, first_name, last_name, active)
VALUES ('admin', 'admin@example.com', '$2a$10$GckdgpYUMUm5uIm5CKj8heaRQrQUCvmF9VIJd0NIgV5I9LX8MaYvW', 'System', 'Administrator', true);

-- Assign admin role to the admin user
UPDATE users
SET role_id = (
    SELECT id FROM roles WHERE name = 'ROLE_ADMIN'
)
WHERE username = 'admin';