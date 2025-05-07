-- Create database if it doesn't exist
CREATE DATABASE IF NOT EXISTS service_security;

USE service_security;

-- Create user if it doesn't exist
CREATE USER IF NOT EXISTS 'security_user'@'%' IDENTIFIED BY 'security_pass';
GRANT ALL PRIVILEGES ON service_security.* TO 'security_user'@'%';
FLUSH PRIVILEGES;

-- Insert roles
INSERT INTO roles (name, description) VALUES 
('ROLE_ADMIN', 'Administrator with full access to all features'),
('ROLE_TECHNICIAN', 'Technician with access to repair services'),
('ROLE_CUSTOMER', 'Customer with limited access to view products and place orders');

-- Insert permissions for Admin role
INSERT INTO permissions (name, path, method, description) VALUES
-- Admin permissions for User Management
('admin_users_read', '/api/users/**', 'GET', 'View all users'),
('admin_users_create', '/api/users', 'POST', 'Create users'),
('admin_users_update', '/api/users/**', 'PUT', 'Update any user'),
('admin_users_delete', '/api/users/**', 'DELETE', 'Delete users'),

-- Admin permissions for Role and Permission Management
('admin_roles_manage', '/api/permission/**', 'GET', 'View all roles and permissions'),
('admin_roles_create', '/api/permission', 'POST', 'Create roles and permissions'),
('admin_roles_update', '/api/permission/**', 'PUT', 'Update roles and permissions'),
('admin_roles_delete', '/api/permission/**', 'DELETE', 'Delete roles and permissions'),

-- Admin permissions for Customer Management
('admin_customers_read', '/api/v1/customers/**', 'GET', 'View all customers'),
('admin_customers_create', '/api/v1/customers', 'POST', 'Create customers'),
('admin_customers_update', '/api/v1/customers/**', 'PUT', 'Update any customer'),
('admin_customers_delete', '/api/v1/customers/**', 'DELETE', 'Delete customers'),

-- Admin permissions for Product Management
('admin_products_read', '/api/v1/products/**', 'GET', 'View all products'),
('admin_products_create', '/api/v1/products', 'POST', 'Create products'),
('admin_products_update', '/api/v1/products/**', 'PUT', 'Update products'),
('admin_products_delete', '/api/v1/products/**', 'DELETE', 'Delete products'),

-- Admin permissions for Technician Management
('admin_technicians_read', '/api/v1/technicians/**', 'GET', 'View all technicians'),
('admin_technicians_create', '/api/v1/technicians', 'POST', 'Create technicians'),
('admin_technicians_update', '/api/v1/technicians/**', 'PUT', 'Update technicians'),
('admin_technicians_delete', '/api/v1/technicians/**', 'DELETE', 'Delete technicians'),

-- Admin permissions for Repair Management
('admin_repairs_read', '/api/v1/repairs/**', 'GET', 'View all repairs'),
('admin_repairs_create', '/api/v1/repairs', 'POST', 'Create repairs'),
('admin_repairs_update', '/api/v1/repairs/**', 'PUT', 'Update repairs'),
('admin_repairs_delete', '/api/v1/repairs/**', 'DELETE', 'Delete repairs'),

-- Admin permissions for Warranty Management
('admin_warranty_read', '/api/v1/warranty/**', 'GET', 'View all warranties'),
('admin_warranty_create', '/api/v1/warranty', 'POST', 'Create warranties'),
('admin_warranty_update', '/api/v1/warranty/**', 'PUT', 'Update warranties'),
('admin_warranty_delete', '/api/v1/warranty/**', 'DELETE', 'Delete warranties'),

-- Admin permissions for notification Management
('admin_notifications_read', '/api/v1/notifications/**', 'GET', 'View all notifications'),
('admin_notifications_create', '/api/v1/notifications', 'POST', 'Create notifications'),
('admin_notifications_update', '/api/v1/notifications/**', 'PUT', 'Update notifications'),
('admin_notifications_delete', '/api/v1/notifications/**', 'DELETE', 'Delete notifications'),

-- Admin permissions for Survey Management
('admin_surveys_read', '/api/v1/surveys/**', 'GET', 'View all surveys'),
('admin_surveys_create', '/api/v1/surveys', 'POST', 'Create surveys'),
('admin_surveys_update', '/api/v1/surveys/**', 'PUT', 'Update surveys'),
('admin_surveys_delete', '/api/v1/surveys/**', 'DELETE', 'Delete surveys');

-- Insert permissions for Technician role
INSERT INTO permissions (name, path, method, description) VALUES
-- Technician permissions for own profile
('tech_profile_read', '/api/users/{id}', 'GET', 'View own profile'),
('tech_profile_update', '/api/users/{id}', 'PUT', 'Update own profile'),

-- Technician permissions for Customer info (read-only)
('tech_customers_read', '/api/v1/customers/{id}', 'GET', 'View customer details'),

-- Technician permissions for Product info (read-only)
('tech_products_read', '/api/v1/products/**', 'GET', 'View all products'),

-- Technician permissions for Repair Management
('tech_repairs_read', '/api/v1/repairs/**', 'GET', 'View all repairs'),
('tech_repairs_update', '/api/v1/repairs/{id}/**', 'PUT', 'Update repair status'),
('tech_repairs_update_status', '/api/v1/repairs/{id}/next', 'POST', 'Move repair to next status'),
('tech_repairs_parts', '/api/v1/repairs/{id}/parts', 'POST', 'Add parts to repair'),
('tech_repairs_actions', '/api/v1/repairs/{id}/actions', 'POST', 'Add actions to repair'),

-- Technician permissions for Warranty info (read-only)
('tech_warranty_read', '/api/v1/warranty/**', 'GET', 'View warranty details');

-- Insert permissions for Customer role
INSERT INTO permissions (name, path, method, description) VALUES
-- Customer permissions for own profile
('customer_profile_read', '/api/users/{id}', 'GET', 'View own profile'),
('customer_profile_update', '/api/users/{id}', 'PUT', 'Update own profile'),

-- Customer permissions for Product browsing
('customer_products_read', '/api/v1/products/**', 'GET', 'View all products'),

-- Customer permissions for Repair Management
('customer_repairs_read', '/api/v1/repairs/customer/{customerId}', 'GET', 'View own repairs'),
('customer_repairs_create', '/api/v1/repairs', 'POST', 'Create repair request'),
('customer_repairs_cancel', '/api/v1/repairs/{id}/cancel', 'POST', 'Cancel own repair request'),

-- Customer permissions for Warranty Management
('customer_warranty_read', '/api/v1/warranty/customer/{customerId}', 'GET', 'View own warranties'),
('customer_warranty_create', '/api/v1/warranty/requests', 'POST', 'Create warranty request'),

-- Customer permissions for survey responses
('customer_survey_respond', '/api/v1/surveys/survey-responses', 'POST', 'Submit survey responses'),
('customer_survey_view', '/api/v1/surveys/**', 'GET', 'View available surveys');

-- Assign permissions to Admin role
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.name = 'ROLE_ADMIN'
AND p.name LIKE 'admin%';

-- Assign permissions to Technician role
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.name = 'ROLE_TECHNICIAN'
AND p.name LIKE 'tech%';

-- Assign permissions to Customer role
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.name = 'ROLE_CUSTOMER'
AND p.name LIKE 'customer%';

-- Create an admin user
INSERT INTO users (username, email, password, first_name, last_name, active)
VALUES ('admin', 'admin@example.com', '$2a$10$GckdgpYUMUm5uIm5CKj8heaRQrQUCvmF9VIJd0NIgV5I9LX8MaYvW', 'System', 'Administrator', true);

-- Assign admin role to the admin user
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u, roles r
WHERE u.username = 'admin'
AND r.name = 'ROLE_ADMIN';