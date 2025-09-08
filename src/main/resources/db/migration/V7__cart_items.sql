CREATE TABLE cart_items (
  id SERIAL PRIMARY KEY,
  user_id INT NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
  product_id INT NOT NULL REFERENCES products(product_id),
  qty INT NOT NULL CHECK (qty > 0),
  UNIQUE(user_id, product_id)
);
