CREATE DATABASE IF NOT EXISTS womenshop;
USE womenshop;

CREATE TABLE IF NOT EXISTS clothing (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    price_buy DOUBLE NOT NULL CHECK (price_buy >= 0),
    price_sell DOUBLE NOT NULL CHECK (price_sell >= 0 AND price_sell >= price_buy),
    discount_price DOUBLE DEFAULT 0 CHECK (discount_price >= 0),
    stock INT DEFAULT 0 CHECK (stock >= 0),
    size INT NOT NULL CHECK (size >= 34 AND size <= 54)
);

CREATE TABLE IF NOT EXISTS shoes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    price_buy DOUBLE NOT NULL CHECK (price_buy >= 0),
    price_sell DOUBLE NOT NULL CHECK (price_sell >= 0 AND price_sell >= price_buy),
    discount_price DOUBLE DEFAULT 0 CHECK (discount_price >= 0),
    stock INT DEFAULT 0 CHECK (stock >= 0),
    size INT NOT NULL CHECK (size >= 36 AND size <= 50)
);

CREATE TABLE IF NOT EXISTS accessory (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    price_buy DOUBLE NOT NULL CHECK (price_buy >= 0),
    price_sell DOUBLE NOT NULL CHECK (price_sell >= 0 AND price_sell >= price_buy),
    discount_price DOUBLE DEFAULT 0 CHECK (discount_price >= 0),
    stock INT DEFAULT 0 CHECK (stock >= 0),
    size INT NULL 
);

INSERT INTO clothing (name, price_buy, price_sell, discount_price, stock, size) 
VALUES ('Dress 1', 70, 100, 0, 0, 38);

INSERT INTO clothing (name, price_buy, price_sell, discount_price, stock, size) 
VALUES ('Dress 2', 90, 120, 0, 0, 40);

INSERT INTO shoes (name, price_buy, price_sell, discount_price, stock, size) 
VALUES ('Shoe 1', 30, 50, 0, 0, 38);

INSERT INTO shoes (name, price_buy, price_sell, discount_price, stock, size) 
VALUES ('Shoe 2', 50, 70, 0, 0, 40);

INSERT INTO accessory (name, price_buy, price_sell, discount_price, stock, size) 
VALUES ('Accessory 1', 20, 30, 0, 0, NULL);

INSERT INTO accessory (name, price_buy, price_sell, discount_price, stock, size) 
VALUES ('Accessory 2', 30, 40, 0, 0, NULL);

SELECT 'CLOTHING' as Category, id, name, price_buy, price_sell, stock, size FROM clothing
UNION ALL
SELECT 'SHOES' as Category, id, name, price_buy, price_sell, stock, size FROM shoes
UNION ALL
SELECT 'ACCESSORY' as Category, id, name, price_buy, price_sell, stock, size FROM accessory;