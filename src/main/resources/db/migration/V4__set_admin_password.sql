CREATE EXTENSION IF NOT EXISTS pgcrypto;

DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin@store.test') THEN
    INSERT INTO users (name, email, password_hash, role)
    VALUES ('Administrator', 'admin@store.test', crypt('Admin@123', gen_salt('bf')), 'ADMIN');
  ELSE
    UPDATE users
    SET password_hash = crypt('Admin@123', gen_salt('bf')),
        role = 'ADMIN'
    WHERE email = 'admin@store.test';
  END IF;
END
$$;
