INSERT INTO sizes (size_name) VALUES ('XS'),('S'),('M'),('L'),('XL');

INSERT INTO categories (name) VALUES ('T-Shirts'),('Hoodies'),('Shoes');

INSERT INTO products (name, description, price, category_id) VALUES
('Basic Tee','Cotton T-shirt',19.99,1),
('Zip Hoodie','Warm hoodie',49.99,2),
('Sneakers','Comfortable shoes',79.90,3);

INSERT INTO product_sizes (product_id, size_id, stock) VALUES
(1,2,20),(1,3,25),(1,4,15),
(2,3,10),(2,4,8),
(3,3,12),(3,4,6);
