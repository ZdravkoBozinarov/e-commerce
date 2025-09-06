INSERT INTO users (name, email, password_hash, role)
SELECT 'Administrator', 'admin@store.test', '<PASTE_BCRYPT_HASH_HERE>', 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin@store.test');