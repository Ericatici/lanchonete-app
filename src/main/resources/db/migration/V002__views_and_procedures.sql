-- =====================================================
-- MIGRATION 002: VIEWS AND STORED PROCEDURES
-- =====================================================
-- Date: 2025-09-01
-- Description: Creation of views and procedures for query optimization
-- Version: 1.0.0

USE lanchonetenacomanda;

-- =====================================================
-- VIEWS FOR FREQUENT QUERIES
-- =====================================================

-- View for daily sales reports
CREATE OR REPLACE VIEW v_daily_sales AS
SELECT 
    DATE(o.order_date) as sale_date,
    COUNT(o.id) as total_orders,
    SUM(o.total_price) as total_revenue,
    AVG(o.total_price) as average_order_value,
    COUNT(DISTINCT o.customer_cpf) as unique_customers,
    SUM(CASE WHEN o.status = 'FINISHED' THEN 1 ELSE 0 END) as completed_orders,
    SUM(CASE WHEN o.status = 'CANCELLED' THEN 1 ELSE 0 END) as cancelled_orders
FROM orders o
WHERE o.order_date >= DATE_SUB(CURRENT_DATE, INTERVAL 30 DAY)
GROUP BY DATE(o.order_date)
ORDER BY sale_date DESC;

-- View for best selling products
CREATE OR REPLACE VIEW v_best_selling_products AS
SELECT 
    p.id,
    p.name,
    p.category,
    p.price as current_price,
    SUM(oi.quantity) as total_sold,
    SUM(oi.subtotal) as total_revenue,
    AVG(oi.item_price) as average_sale_price,
    COUNT(DISTINCT oi.order_id) as total_orders
FROM products p
JOIN order_items oi ON p.id = oi.product_id
JOIN orders o ON oi.order_id = o.id
WHERE o.status IN ('READY', 'FINISHED')
  AND o.order_date >= DATE_SUB(CURRENT_DATE, INTERVAL 30 DAY)
GROUP BY p.id, p.name, p.category, p.price
ORDER BY total_sold DESC;

-- View for most active customers
CREATE OR REPLACE VIEW v_active_customers AS
SELECT 
    c.cpf,
    c.name,
    c.email,
    COUNT(o.id) as total_orders,
    SUM(o.total_price) as total_spent,
    AVG(o.total_price) as average_order_value,
    MAX(o.order_date) as last_order_date,
    MIN(o.order_date) as first_order_date
FROM customers c
JOIN orders o ON c.cpf = o.customer_cpf
WHERE o.status IN ('READY', 'FINISHED')
GROUP BY c.cpf, c.name, c.email
ORDER BY total_spent DESC;

-- View for order status summary
CREATE OR REPLACE VIEW v_order_status_summary AS
SELECT 
    o.status,
    o.payment_status,
    COUNT(*) as order_count,
    SUM(o.total_price) as total_value,
    AVG(o.total_price) as average_value,
    MIN(o.order_date) as oldest_order,
    MAX(o.order_date) as newest_order
FROM orders o
WHERE o.order_date >= DATE_SUB(CURRENT_DATE, INTERVAL 7 DAY)
GROUP BY o.status, o.payment_status
ORDER BY o.status, o.payment_status;

-- View for inventory analysis
CREATE OR REPLACE VIEW v_inventory_analysis AS
SELECT 
    p.id,
    p.name,
    p.category,
    p.stock_quantity,
    p.is_active,
    COALESCE(SUM(oi.quantity), 0) as total_sold_last_30_days,
    CASE 
        WHEN p.stock_quantity = 0 THEN 'OUT_OF_STOCK'
        WHEN p.stock_quantity <= 10 THEN 'LOW_STOCK'
        WHEN p.stock_quantity <= 50 THEN 'MEDIUM_STOCK'
        ELSE 'HIGH_STOCK'
    END as stock_status
FROM products p
LEFT JOIN order_items oi ON p.id = oi.product_id
LEFT JOIN orders o ON oi.order_id = o.id 
    AND o.status IN ('READY', 'FINISHED')
    AND o.order_date >= DATE_SUB(CURRENT_DATE, INTERVAL 30 DAY)
GROUP BY p.id, p.name, p.category, p.stock_quantity, p.is_active
ORDER BY stock_quantity ASC;

-- =====================================================
-- STORED PROCEDURES
-- =====================================================

DELIMITER //

-- Procedure to calculate order total
CREATE PROCEDURE CalculateOrderTotal(IN p_order_id BIGINT)
BEGIN
    DECLARE v_total DECIMAL(10,2) DEFAULT 0;
    DECLARE v_order_exists INT DEFAULT 0;
    
    -- Check if order exists
    SELECT COUNT(*) INTO v_order_exists
    FROM orders 
    WHERE id = p_order_id;
    
    IF v_order_exists = 0 THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Order not found';
    END IF;
    
    -- Calculate total
    SELECT COALESCE(SUM(subtotal), 0) INTO v_total
    FROM order_items 
    WHERE order_id = p_order_id;
    
    -- Update order total
    UPDATE orders 
    SET total_price = v_total, 
        updated_date = CURRENT_TIMESTAMP
    WHERE id = p_order_id;
    
    SELECT CONCAT('Order ', p_order_id, ' total updated to $ ', v_total) as message;
END //

-- Procedure to check product stock
CREATE PROCEDURE CheckProductStock(
    IN p_product_id VARCHAR(255), 
    IN p_quantity INT,
    OUT p_available BOOLEAN,
    OUT p_current_stock INT
)
BEGIN
    DECLARE v_stock INT DEFAULT 0;
    DECLARE v_is_active BOOLEAN DEFAULT FALSE;
    
    -- Check product stock and status
    SELECT stock_quantity, is_active 
    INTO v_stock, v_is_active
    FROM products 
    WHERE id = p_product_id;
    
    -- Check if product exists
    IF v_stock IS NULL THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Product not found';
    END IF;
    
    -- Check if product is active
    IF NOT v_is_active THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Product is not active';
    END IF;
    
    -- Check availability
    SET p_current_stock = v_stock;
    SET p_available = (v_stock >= p_quantity);
END //

-- Procedure to process order
CREATE PROCEDURE ProcessOrder(
    IN p_customer_cpf VARCHAR(11),
    IN p_notes TEXT,
    OUT p_order_id BIGINT
)
BEGIN
    DECLARE v_customer_exists INT DEFAULT 0;
    
    -- Check if customer exists
    SELECT COUNT(*) INTO v_customer_exists
    FROM customers 
    WHERE cpf = p_customer_cpf;
    
    IF v_customer_exists = 0 THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Customer not found';
    END IF;
    
    -- Create order
    INSERT INTO orders (customer_cpf, notes, status, payment_status)
    VALUES (p_customer_cpf, p_notes, 'WAITING_PAYMENT', 'PENDING');
    
    SET p_order_id = LAST_INSERT_ID();
    
    SELECT CONCAT('Order ', p_order_id, ' created successfully') as message;
END //

-- Procedure to add item to order
CREATE PROCEDURE AddItemToOrder(
    IN p_order_id BIGINT,
    IN p_product_id VARCHAR(255),
    IN p_quantity INT
)
BEGIN
    DECLARE v_order_exists INT DEFAULT 0;
    DECLARE v_product_exists INT DEFAULT 0;
    DECLARE v_current_price DECIMAL(10,2);
    DECLARE v_available BOOLEAN;
    DECLARE v_current_stock INT;
    
    -- Check if order exists
    SELECT COUNT(*) INTO v_order_exists
    FROM orders 
    WHERE id = p_order_id;
    
    IF v_order_exists = 0 THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Order not found';
    END IF;
    
    -- Check stock
    CALL CheckProductStock(p_product_id, p_quantity, v_available, v_current_stock);
    
    IF NOT v_available THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = CONCAT('Insufficient stock. Available: ', v_current_stock, ', Requested: ', p_quantity);
    END IF;
    
    -- Get current product price
    SELECT price INTO v_current_price
    FROM products 
    WHERE id = p_product_id;
    
    -- Add item to order
    INSERT INTO order_items (order_id, product_id, quantity, item_price)
    VALUES (p_order_id, p_product_id, p_quantity, v_current_price);
    
    -- Recalculate order total
    CALL CalculateOrderTotal(p_order_id);
    
    SELECT CONCAT('Item added to order ', p_order_id) as message;
END //

-- Procedure for sales report by period
CREATE PROCEDURE SalesReport(
    IN p_start_date DATE,
    IN p_end_date DATE
)
BEGIN
    SELECT 
        DATE(o.order_date) as sale_date,
        COUNT(o.id) as total_orders,
        SUM(o.total_price) as total_revenue,
        AVG(o.total_price) as average_order_value,
        COUNT(DISTINCT o.customer_cpf) as unique_customers,
        SUM(CASE WHEN o.status = 'FINISHED' THEN 1 ELSE 0 END) as completed_orders,
        SUM(CASE WHEN o.status = 'CANCELLED' THEN 1 ELSE 0 END) as cancelled_orders,
        ROUND(
            (SUM(CASE WHEN o.status = 'FINISHED' THEN 1 ELSE 0 END) * 100.0 / COUNT(o.id)), 2
        ) as completion_rate
    FROM orders o
    WHERE DATE(o.order_date) BETWEEN p_start_date AND p_end_date
    GROUP BY DATE(o.order_date)
    ORDER BY sale_date DESC;
END //

DELIMITER ;

-- =====================================================
-- TRIGGERS FOR AUDITING AND CONSISTENCY
-- =====================================================

-- Trigger to update stock when item is added to order
DELIMITER //
CREATE TRIGGER tr_order_item_insert
AFTER INSERT ON order_items
FOR EACH ROW
BEGIN
    UPDATE products 
    SET stock_quantity = stock_quantity - NEW.quantity,
        updated_date = CURRENT_TIMESTAMP
    WHERE id = NEW.product_id;
END //

-- Trigger to restore stock when item is removed from order
CREATE TRIGGER tr_order_item_delete
AFTER DELETE ON order_items
FOR EACH ROW
BEGIN
    UPDATE products 
    SET stock_quantity = stock_quantity + OLD.quantity,
        updated_date = CURRENT_TIMESTAMP
    WHERE id = OLD.product_id;
END //

-- Trigger to update stock when quantity is changed
CREATE TRIGGER tr_order_item_update
AFTER UPDATE ON order_items
FOR EACH ROW
BEGIN
    DECLARE v_quantity_diff INT;
    SET v_quantity_diff = NEW.quantity - OLD.quantity;
    
    UPDATE products 
    SET stock_quantity = stock_quantity - v_quantity_diff,
        updated_date = CURRENT_TIMESTAMP
    WHERE id = NEW.product_id;
END //

-- Trigger for order status change logging
CREATE TRIGGER tr_order_status_update
AFTER UPDATE ON orders
FOR EACH ROW
BEGIN
    IF OLD.status != NEW.status OR OLD.payment_status != NEW.payment_status THEN
        INSERT INTO payment_logs (order_id, payment_id, status, amount, gateway_response)
        VALUES (
            NEW.id, 
            COALESCE(NEW.payment_id, CONCAT('ORDER_', NEW.id)), 
            NEW.payment_status, 
            NEW.total_price,
            CONCAT('{"old_status":"', OLD.status, '","new_status":"', NEW.status, '","old_payment_status":"', OLD.payment_status, '","new_payment_status":"', NEW.payment_status, '"}')
        );
    END IF;
END //

DELIMITER ;

-- =====================================================
-- PERFORMANCE CONFIGURATIONS
-- =====================================================

-- Timeout configurations for procedures
SET GLOBAL wait_timeout = 28800;
SET GLOBAL interactive_timeout = 28800;
