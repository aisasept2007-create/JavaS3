CREATE DATABASE IF NOT EXISTS campuskart;
USE campuskart;

CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(60) NOT NULL UNIQUE,
    password_hash CHAR(64) NOT NULL,
    role ENUM('ADMIN','STAFF') NOT NULL
);

CREATE TABLE IF NOT EXISTS products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(30) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(60) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL,
    CHECK (price >= 0),
    CHECK (stock >= 0)
);

CREATE TABLE IF NOT EXISTS sales (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    subtotal DECIMAL(12,2) NOT NULL,
    tax DECIMAL(12,2) NOT NULL,
    total DECIMAL(12,2) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS sale_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    sale_id INT NOT NULL,
    product_id INT NULL,
    product_name VARCHAR(100) NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    amount DECIMAL(12,2) NOT NULL,
    FOREIGN KEY (sale_id) REFERENCES sales(id),
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE SET NULL
);

-- Demo accounts only: change passwords before real deployment.
INSERT IGNORE INTO users(username,password_hash,role) VALUES
('admin',SHA2('admin123',256),'ADMIN'),
('staff',SHA2('staff123',256),'STAFF');

INSERT IGNORE INTO products(code,name,category,price,stock) VALUES
('P01','Pen','Stationery',10.00,50),
('P02','Notebook','Stationery',50.00,30),
('P03','Pencil','Stationery',5.00,40),
('P04','Eraser','Stationery',5.00,25),
('P05','File Folder','Stationery',35.00,8);
