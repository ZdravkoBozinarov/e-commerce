DO $$
DECLARE
  uid INT;
  pid_tee INT;      price_tee NUMERIC(10,2);
  pid_hood INT;     price_hood NUMERIC(10,2);
  pid_sneaks INT;   price_sneaks NUMERIC(10,2);
  sid_s INT; sid_m INT; sid_l INT;
  o1 INT; o2 INT; o3 INT; o4 INT;
BEGIN
  -- Use the admin as purchaser (or change the email to any existing user)
  SELECT user_id INTO uid FROM users WHERE email = 'admin@store.test' LIMIT 1;

  -- Product ids + current prices
  SELECT product_id, price INTO pid_tee,    price_tee    FROM products WHERE name = 'Basic Tee';
  SELECT product_id, price INTO pid_hood,   price_hood   FROM products WHERE name = 'Zip Hoodie';
  SELECT product_id, price INTO pid_sneaks, price_sneaks FROM products WHERE name = 'Sneakers';

  -- Size ids
  SELECT size_id INTO sid_s FROM sizes WHERE size_name = 'S';
  SELECT size_id INTO sid_m FROM sizes WHERE size_name = 'M';
  SELECT size_id INTO sid_l FROM sizes WHERE size_name = 'L';

  -- Orders across different months (so Monthly Revenue has data)
  INSERT INTO orders(user_id, order_date, status) VALUES (uid, '2025-06-05', 'PAID') RETURNING order_id INTO o1;
  INSERT INTO orders(user_id, order_date, status) VALUES (uid, '2025-07-12', 'PAID') RETURNING order_id INTO o2;
  INSERT INTO orders(user_id, order_date, status) VALUES (uid, '2025-08-20', 'PAID') RETURNING order_id INTO o3;
  INSERT INTO orders(user_id, order_date, status) VALUES (uid, '2025-09-03', 'PAID') RETURNING order_id INTO o4;

  -- Line items (trigger will decrement stock in product_sizes)
  INSERT INTO order_items(order_id, product_id, size_id, quantity, price) VALUES
    (o1, pid_tee,    sid_m, 3, price_tee),
    (o1, pid_sneaks, sid_l, 2, price_sneaks),

    (o2, pid_hood,   sid_m, 1, price_hood),
    (o2, pid_sneaks, sid_l, 1, price_sneaks),

    (o3, pid_tee,    sid_s, 5, price_tee),
    (o3, pid_hood,   sid_l, 1, price_hood),

    (o4, pid_sneaks, sid_l, 3, price_sneaks),
    (o4, pid_tee,    sid_m, 2, price_tee);
END $$;
