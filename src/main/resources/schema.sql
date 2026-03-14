-- 1. Farmers Table (With Login Credentials)
CREATE TABLE IF NOT EXISTS farmers (
    farmer_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    email VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    nic VARCHAR(20) NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL 
);

-- 2. Tractor Drivers Table (With Quantity & Pricing)
CREATE TABLE IF NOT EXISTS tractor_drivers (
    tractor_driver_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    machine_quantity INT DEFAULT 1,
    price_per_acre DECIMAL(10, 2), 
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

-- 3. Harvester Drivers Table (With Availability & Pricing)
CREATE TABLE IF NOT EXISTS harvester_drivers (
    harvester_driver_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    available_machines INT DEFAULT 1,
    price_per_acre DECIMAL(10, 2),
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

-- 4. Fertilizer Suppliers Table (With Stock & Price per Liter)
CREATE TABLE IF NOT EXISTS fertilizer_suppliers (
    supplier_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    fertilizer_type VARCHAR(100),
    stock_quantity_liters INT,
    price_per_liter DECIMAL(10, 2),
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

-- 5. Provider Schedules (For Drivers/Suppliers to set available dates)
CREATE TABLE IF NOT EXISTS provider_schedules (
    schedule_id INT AUTO_INCREMENT PRIMARY KEY,
    provider_type VARCHAR(20) NOT NULL, -- 'TRACTOR', 'HARVESTER', or 'FERTILIZER'
    provider_id INT NOT NULL, 
    available_date DATE NOT NULL,
    is_booked BOOLEAN DEFAULT FALSE -- FALSE = Available, TRUE = Booked
);

-- 6. Service Booking Table (Links everything together)
CREATE TABLE IF NOT EXISTS service_booking (
    booking_id INT AUTO_INCREMENT PRIMARY KEY,
    farmer_id INT NOT NULL,
    service_type VARCHAR(20) NOT NULL, -- 'TRACTOR', 'HARVESTER', or 'FERTILIZER'
    provider_id INT NOT NULL, 
    booking_date DATE NOT NULL,
    booking_time TIME,
    total_cost DECIMAL(10, 2),
    status VARCHAR(20) DEFAULT 'Pending', -- Pending, Accepted, Completed, Cancelled
    CONSTRAINT fk_farmer FOREIGN KEY (farmer_id) REFERENCES farmers(farmer_id) ON DELETE CASCADE
);
