-- =====================================================
-- MIGRATION 001: INITIAL SCHEMA FOR AWS RDS
-- =====================================================
-- Date: 2025-09-01
-- Description: Creation of initial schema optimized for AWS RDS MySQL
-- Version: 1.0.0

-- Configurations for AWS RDS
SET sql_mode = 'STRICT_TRANS_TABLES,NO_ZERO_DATE,NO_ZERO_IN_DATE,ERROR_FOR_DIVISION_BY_ZERO';
SET foreign_key_checks = 1;

-- =====================================================
-- TABLE: customers
-- =====================================================
CREATE TABLE IF NOT EXISTS customers (
    cpf VARCHAR(11) NOT NULL PRIMARY KEY COMMENT 'Customer CPF (numbers only)',
    name VARCHAR(255) NOT NULL COMMENT 'Customer full name',
    email VARCHAR(255) NOT NULL UNIQUE COMMENT 'Unique customer email',
    phone VARCHAR(20) NULL COMMENT 'Contact phone number',
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation date',
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update date',
    
    -- Performance indexes
    INDEX idx_customer_email (email),
    INDEX idx_customer_name (name),
    INDEX idx_customer_created_date (created_date)
) ENGINE=InnoDB 
  DEFAULT CHARSET=utf8mb4 
  COLLATE=utf8mb4_unicode_ci
  COMMENT='System customers table';

-- =====================================================
-- TABLE: products
-- =====================================================
CREATE TABLE IF NOT EXISTS products (
    id VARCHAR(255) NOT NULL PRIMARY KEY COMMENT 'Unique product ID',
    name VARCHAR(255) NOT NULL COMMENT 'Product name',
    description TEXT NULL COMMENT 'Detailed product description',
    price DECIMAL(10,2) NOT NULL CHECK (price >= 0) COMMENT 'Product price (minimum 0)',
    category ENUM('DRINK', 'DESSERT', 'SNACK', 'SIDE_DISH') NOT NULL COMMENT 'Product category',
    is_active BOOLEAN DEFAULT TRUE COMMENT 'Indicates if product is active',
    stock_quantity INT DEFAULT 0 CHECK (stock_quantity >= 0) COMMENT 'Stock quantity',
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation date',
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update date',
    
    -- Performance indexes
    INDEX idx_product_category (category),
    INDEX idx_product_active (is_active),
    INDEX idx_product_price (price),
    INDEX idx_product_created_date (created_date)
) ENGINE=InnoDB 
  DEFAULT CHARSET=utf8mb4 
  COLLATE=utf8mb4_unicode_ci
  COMMENT='Menu products table';

-- =====================================================
-- TABLE: orders
-- =====================================================
CREATE TABLE IF NOT EXISTS orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'Unique order ID',
    
    -- Colunas da OrderEntity.java
    customer_cpf VARCHAR(11) NULL COMMENT 'Customer CPF (Optional). Usando NULL para Orders de clientes nao identificados',
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Order date and time',
    
    -- ENUMs (Verifique se esses valores de ENUM correspondem exatamente aos seus Java Enums)
    status ENUM('WAITING_PAYMENT', 'RECEIVED', 'IN_PREPARATION', 'READY', 'FINISHED', 'CANCELLED') 
           NOT NULL DEFAULT 'WAITING_PAYMENT' COMMENT 'Current order status',
    payment_status ENUM('PENDING', 'PAID', 'FAILED', 'REFUNDED') 
                    NOT NULL DEFAULT 'PENDING' COMMENT 'Payment status',
                    
    qr_code_data TEXT NULL COMMENT 'QR Code data for payment',
    payment_id VARCHAR(255) NULL UNIQUE COMMENT 'Payment gateway ID',
    total_price DECIMAL(10,2) NOT NULL DEFAULT 0.00 CHECK (total_price >= 0) COMMENT 'Total order price',
    
    -- BaseEntity fields
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation date',
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update date',
    
    -- Referential integrity constraints
    CONSTRAINT fk_order_customer 
        FOREIGN KEY (customer_cpf) 
        REFERENCES customers(cpf) 
        ON DELETE SET NULL 
        ON UPDATE CASCADE,
    
    -- Performance indexes
    INDEX idx_order_status (status),
    INDEX idx_order_payment_status (payment_status),
    INDEX idx_order_date (order_date)
) ENGINE=InnoDB 
  DEFAULT CHARSET=utf8mb4 
  COLLATE=utf8mb4_unicode_ci
  COMMENT='Main orders table';

-- =====================================================
-- TABLE: order_items
-- =====================================================
CREATE TABLE IF NOT EXISTS order_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'Unique order item ID',
    order_id BIGINT NOT NULL COMMENT 'Order ID',
    product_id VARCHAR(255) NOT NULL COMMENT 'Product ID',
    quantity INT NOT NULL CHECK (quantity > 0) COMMENT 'Item quantity',
    item_price DECIMAL(10,2) NOT NULL CHECK (item_price >= 0) COMMENT 'Unit price at purchase time',
    subtotal DECIMAL(10,2) GENERATED ALWAYS AS (quantity * item_price) STORED COMMENT 'Automatically calculated subtotal',
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation date',
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update date',
    
    -- Referential integrity constraints
    CONSTRAINT fk_order_items_order 
        FOREIGN KEY (order_id) 
        REFERENCES orders(id) 
        ON DELETE CASCADE 
        ON UPDATE CASCADE,
    CONSTRAINT fk_order_items_product 
        FOREIGN KEY (product_id) 
        REFERENCES products(id) 
        ON DELETE RESTRICT 
        ON UPDATE CASCADE,
    
    -- Performance indexes
    INDEX idx_order_items_order (order_id),
    INDEX idx_order_items_product (product_id),
    INDEX idx_order_items_created_date (created_date)
) ENGINE=InnoDB 
  DEFAULT CHARSET=utf8mb4 
  COLLATE=utf8mb4_unicode_ci
  COMMENT='Order items table';

-- =====================================================
-- TABLE: payment_logs
-- =====================================================
CREATE TABLE IF NOT EXISTS payment_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'Unique log ID',
    order_id BIGINT NOT NULL COMMENT 'Order ID',
    payment_id VARCHAR(255) NOT NULL COMMENT 'Payment gateway ID',
    status ENUM('PENDING', 'PAID', 'FAILED', 'REFUNDED') NOT NULL COMMENT 'Payment status',
    amount DECIMAL(10,2) NOT NULL COMMENT 'Payment amount',
    gateway_response TEXT NULL COMMENT 'Complete gateway response',
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Log creation date',
    
    -- Referential integrity constraints
    CONSTRAINT fk_payment_logs_order 
        FOREIGN KEY (order_id) 
        REFERENCES orders(id) 
        ON DELETE CASCADE 
        ON UPDATE CASCADE,
    
    -- Performance indexes
    INDEX idx_payment_logs_order (order_id),
    INDEX idx_payment_logs_payment_id (payment_id),
    INDEX idx_payment_logs_status (status),
    INDEX idx_payment_logs_created_date (created_date)
) ENGINE=InnoDB 
  DEFAULT CHARSET=utf8mb4 
  COLLATE=utf8mb4_unicode_ci
  COMMENT='Payment transaction logs for auditing';
