
create database IF NOT EXISTS service_condition;
use service_condition;

-- Create warranty_conditions table
CREATE TABLE IF NOT EXISTS warranty_conditions (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  description TEXT,
  is_active BOOLEAN
);

-- Create warranty_condition_results table
CREATE TABLE IF NOT EXISTS warranty_condition_results (
  id INT AUTO_INCREMENT PRIMARY KEY,
  warranty_request_id INT,
  condition_id INT,
  passed BOOLEAN NOT NULL,
  notes TEXT,
  evaluated_by VARCHAR(255),
  evaluated_at DATETIME,
  FOREIGN KEY (condition_id) REFERENCES warranty_conditions(id)
);

-- Insert warranty conditions data
INSERT INTO warranty_conditions (id, name, description, is_active) VALUES
(1, 'No Physical Damage', 'Product shows no signs of physical damage such as cracks, dents, or broken parts', 1),
(2, 'No Water Damage', 'Product shows no signs of liquid damage or water contact', 1),
(3, 'No Unauthorized Repairs', 'Product has not been opened or repaired by unauthorized personnel', 1),
(4, 'Used According to Manual', 'Product has been used in accordance with the user manual', 1),
(5, 'Original Components', 'All components are original and no third-party replacements have been used', 1);

-- Note: The dump doesn't contain any warranty_condition_results data
-- If you have any, they would be inserted here
-- INSERT INTO warranty_condition_results (id, warranty_request_id, condition_id, passed, notes, evaluated_by, evaluated_at) VALUES
-- ...

-- Set auto increment values
ALTER TABLE warranty_conditions AUTO_INCREMENT = 6;
ALTER TABLE warranty_condition_results AUTO_INCREMENT = 1;

-- Add index to warranty_request_id for better performance
ALTER TABLE warranty_condition_results ADD INDEX idx_warranty_request_id (warranty_request_id);