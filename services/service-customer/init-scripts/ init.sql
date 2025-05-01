CREATE DATABASE IF NOT EXISTS service_customer;


USE service_customer;


CREATE TABLE IF NOT EXISTS customer (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone_number VARCHAR(15),
    address VARCHAR(255)
);


GRANT ALL PRIVILEGES ON service_customer.* TO 'customer_user'@'%';
FLUSH PRIVILEGES;


INSERT INTO customer (first_name, last_name, email, phone_number, address) VALUES 
    ('John', 'Doe', 'john.doe@example.com', '1234567890', '123 Main St'),
    ('Jane', 'Smith', 'jane.smith@example.com', '0987654321', '456 Oak Ave'),
    ('Michael', 'Johnson', 'michael.johnson@example.com', '5551234567', '789 Pine St');