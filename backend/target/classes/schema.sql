-- ============================================================
-- E-Commerce Inventory & Order Management System - Schema
-- ============================================================

CREATE DATABASE IF NOT EXISTS inventory_db;
USE inventory_db;

-- Users / Admins
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    role ENUM('ADMIN', 'MANAGER', 'VIEWER') DEFAULT 'ADMIN',
    avatar_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Categories
CREATE TABLE IF NOT EXISTS categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Products
CREATE TABLE IF NOT EXISTS products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    sku VARCHAR(50) UNIQUE NOT NULL,
    category_id BIGINT,
    price DECIMAL(10, 2) NOT NULL,
    cost_price DECIMAL(10, 2),
    stock_quantity INT NOT NULL DEFAULT 0,
    low_stock_threshold INT DEFAULT 10,
    status ENUM('ACTIVE', 'INACTIVE', 'DISCONTINUED') DEFAULT 'ACTIVE',
    description TEXT,
    image_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL
);

-- Customers
CREATE TABLE IF NOT EXISTS customers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20),
    address TEXT,
    city VARCHAR(100),
    country VARCHAR(100) DEFAULT 'US',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Orders
CREATE TABLE IF NOT EXISTS orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_number VARCHAR(50) UNIQUE NOT NULL,
    customer_id BIGINT,
    status ENUM('PENDING', 'PROCESSING', 'SHIPPED', 'DELIVERED', 'CANCELLED', 'REFUNDED') DEFAULT 'PENDING',
    subtotal DECIMAL(10, 2) NOT NULL DEFAULT 0,
    tax DECIMAL(10, 2) DEFAULT 0,
    shipping_cost DECIMAL(10, 2) DEFAULT 0,
    total_amount DECIMAL(10, 2) NOT NULL DEFAULT 0,
    shipping_address TEXT,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE SET NULL
);

-- Order Items
CREATE TABLE IF NOT EXISTS order_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    product_name VARCHAR(200) NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    subtotal DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE RESTRICT
);

-- Inventory Logs
CREATE TABLE IF NOT EXISTS inventory_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    change_type ENUM('RESTOCK', 'SALE', 'ADJUSTMENT', 'RETURN') NOT NULL,
    quantity_change INT NOT NULL,
    quantity_before INT NOT NULL,
    quantity_after INT NOT NULL,
    reference_id BIGINT,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

-- ============================================================
-- Sample Data
-- ============================================================

INSERT INTO users (username, password, full_name, email, role) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6HABC', 'Alex Johnson', 'admin@inventory.com', 'ADMIN');
-- default password: admin123

INSERT INTO categories (name, description) VALUES
('Electronics', 'Electronic devices and accessories'),
('Clothing', 'Apparel and fashion items'),
('Home & Garden', 'Home decor and garden supplies'),
('Sports', 'Sports equipment and gear'),
('Books', 'Books and educational materials');

INSERT INTO customers (name, email, phone, address, city, country) VALUES
('Sarah Miller', 'sarah@example.com', '+1-555-0101', '123 Oak Street', 'New York', 'US'),
('James Wilson', 'james@example.com', '+1-555-0102', '456 Pine Ave', 'Los Angeles', 'US'),
('Emily Davis', 'emily@example.com', '+1-555-0103', '789 Maple Blvd', 'Chicago', 'US'),
('Michael Brown', 'michael@example.com', '+1-555-0104', '321 Elm Street', 'Houston', 'US'),
('Jessica Taylor', 'jessica@example.com', '+1-555-0105', '654 Cedar Lane', 'Phoenix', 'US');

INSERT INTO products (name, sku, category_id, price, cost_price, stock_quantity, low_stock_threshold, status) VALUES
('iPhone 15 Pro', 'ELEC-001', 1, 1199.99, 900.00, 45, 10, 'ACTIVE'),
('Samsung Galaxy S24', 'ELEC-002', 1, 999.99, 750.00, 32, 10, 'ACTIVE'),
('Sony WH-1000XM5 Headphones', 'ELEC-003', 1, 349.99, 220.00, 8, 10, 'ACTIVE'),
('MacBook Air M3', 'ELEC-004', 1, 1299.99, 950.00, 15, 5, 'ACTIVE'),
('Nike Air Max 270', 'CLTH-001', 2, 159.99, 80.00, 67, 20, 'ACTIVE'),
('Levi''s 501 Jeans', 'CLTH-002', 2, 79.99, 35.00, 5, 15, 'ACTIVE'),
('Adidas Ultraboost', 'CLTH-003', 2, 189.99, 95.00, 23, 15, 'ACTIVE'),
('IKEA KALLAX Shelf', 'HOME-001', 3, 89.99, 45.00, 12, 5, 'ACTIVE'),
('Garden Hose 50ft', 'HOME-002', 3, 34.99, 18.00, 3, 10, 'ACTIVE'),
('Yoga Mat Pro', 'SPRT-001', 4, 49.99, 22.00, 89, 20, 'ACTIVE');
