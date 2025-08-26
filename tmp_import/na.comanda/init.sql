CREATE TABLE IF NOT EXISTS customers (
    cpf VARCHAR(255) PRIMARY KEY, 
    name VARCHAR(255),            
    email VARCHAR(255),           
    `created-date` TIMESTAMP,     
    `updated-date` TIMESTAMP    
);

CREATE TABLE IF NOT EXISTS products (
    id VARCHAR(255) PRIMARY KEY,       
    name VARCHAR(255),                 
    description TEXT,                  
    price DECIMAL(10, 2),              
    category ENUM('DRINK', 'DESSERT', 'SNACK', 'SIDE_DISH'),               
    `created-date` TIMESTAMP,          
    `updated-date` TIMESTAMP           
);

INSERT INTO customers (cpf, name, email, `created-date`, `updated-date`) VALUES
('12345678901', 'Joao da Silva', 'joao.silva@email.com', NOW(), NOW()),
('98765432109', 'Maria Souza', 'maria.souza@email.com', NOW(), NOW()),
('45678912302', 'Carlos Pereira', 'carlos.pereira@email.com', NOW(), NOW());

INSERT INTO products (id, name, description, price, category, `created-date`, `updated-date`) VALUES
('1', 'Hamburguer Classico', 'Pao, carne, queijo, alface, tomate', 15.50, 'SNACK', NOW(), NOW()),
('2', 'Batata Frita Media', 'Porcao media de batata frita', 8.00, 'SIDE_DISH', NOW(), NOW()),
('3', 'Refrigerante Cola', 'Refrigerante sabor cola 350ml', 5.00, 'DRINK', NOW(), NOW()),
('4', 'Sundae de Chocolate', 'Sundae com calda de chocolate', 10.00, 'DESSERT', NOW(), NOW());