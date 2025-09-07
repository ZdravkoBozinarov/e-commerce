# 🛍️ Online Clothing Store – Database Project

## 📖 Overview
This is an academic project for the **Databases** course, developed with **PostgreSQL** and **Spring Boot**.

The system simulates an online clothing store where:
- Users can register, log in, browse products, place orders, and write reviews.
- Administrators have extended access to **Statistics Dashboard** to view advanced reports.

The project emphasizes **database design and SQL skills** while demonstrating clean backend architecture with Spring Boot.

---

## 🎯 Learning Objectives
This project demonstrates:
- ER modeling and normalization
- SQL schema design (DDL)
- CRUD operations (DML)
- JOINs, Aggregations, Subqueries
- Views, Triggers, Stored Procedures
- Integration with **Spring Boot (JdbcTemplate + raw SQL)**
- **Controller → Service → Repository** layered architecture
- Basic frontend implementation with **Thymeleaf + Bootstrap + Chart.js**

---

## 🗂️ ER Diagram
Key relationships:
- A user can have many orders
- A product can have multiple sizes (M:N relationship)
- An order contains multiple products
- A product can have multiple reviews

*(Diagram recommended via dbdiagram.io or draw.io)*

---

## 🛠️ SQL Schema (PostgreSQL)

```sql
CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE categories (
    category_id SERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE products (
    product_id SERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    price NUMERIC(10,2) NOT NULL,
    category_id INT REFERENCES categories(category_id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE sizes (
    size_id SERIAL PRIMARY KEY,
    size_name VARCHAR(10) UNIQUE NOT NULL
);

CREATE TABLE product_sizes (
    product_id INT REFERENCES products(product_id),
    size_id INT REFERENCES sizes(size_id),
    stock INT DEFAULT 0,
    PRIMARY KEY (product_id, size_id)
);

CREATE TABLE orders (
    order_id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(user_id),
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'PENDING'
);

CREATE TABLE order_items (
    order_item_id SERIAL PRIMARY KEY,
    order_id INT REFERENCES orders(order_id),
    product_id INT REFERENCES products(product_id),
    size_id INT REFERENCES sizes(size_id),
    quantity INT NOT NULL,
    price NUMERIC(10,2) NOT NULL
);

CREATE TABLE payments (
    payment_id SERIAL PRIMARY KEY,
    order_id INT REFERENCES orders(order_id),
    amount NUMERIC(10,2) NOT NULL,
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    method VARCHAR(50)
);

CREATE TABLE reviews (
    review_id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(user_id),
    product_id INT REFERENCES products(product_id),
    rating INT CHECK (rating BETWEEN 1 AND 5),
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```
---
# 💻 Application Features
## 🔑 Authentication & Roles
- User registration and login system
- Passwords stored securely (hashed)
- Role-based navigation:
- USER → Home, Products, Cart, Orders
- ADMIN → Home, Products, Orders, Statistics

## 🛒 User Capabilities
- Browse products by categories
- Add products to cart and complete checkout
- View personal order history
- Write reviews with ratings

## 🛠️ Admin Capabilities
- Access to Statistics Dashboard
- Run and display advanced SQL queries:
- Top 5 best-selling products
- Top customers by orders
- Monthly revenue reports
- Low-stock product alerts
---
## 🖼️ Frontend (Thymeleaf + Bootstrap)
- Login/Register → secure user authentication
- Products Page → product list with images, description, sizes, price, Add to Cart
- Cart Page → manage items, update quantities, checkout
- Orders Page → display user’s past orders with status
- Admin Dashboard → tables & charts (powered by Chart.js) for statistics
# 🏗️ Backend Architecture
## 📂 Package Structure
```
src/main/java/com/example/clothingstore
│
├── controller
│   ├── AuthController.java
│   ├── ProductController.java
│   ├── OrderController.java
│   └── AdminController.java
│
├── service
│   ├── AuthService.java
│   ├── ProductService.java
│   ├── OrderService.java
│   └── AdminService.java
│
├── repository
│   ├── UserRepository.java
│   ├── ProductRepository.java
│   ├── OrderRepository.java
│   └── AdminRepository.java
│
├── model
│   ├── User.java
│   ├── Product.java
│   ├── Order.java
│   ├── Review.java
│   └── Category.java
│
└── config
    ├── SecurityConfig.java
    └── DatabaseConfig.java
```
---
## 📦 Tech Stack
- Backend: Spring Boot (Web, JDBC, Security, Validation, Lombok)
- Frontend: Thymeleaf, Bootstrap, Chart.js
- Database: PostgreSQL
- Build Tool: Maven

> Status: actively developed — this README includes both **what’s done** and a detailed **roadmap**.

---

## Table of Contents
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Database Schema](#database-schema)
- [Getting Started](#getting-started)
- [Migrations & Demo Data](#migrations--demo-data)
- [Current Features (Done)](#current-features-done)
- [Admin Account](#admin-account)
- [Project TODO / Roadmap](#project-todo--roadmap)
- [DB Highlights to Demonstrate](#db-highlights-to-demonstrate)
- [Testing Ideas](#testing-ideas)
- [Notes](#notes)
- [Suggested Demo Script](#suggested-demo-script)

---

## Tech Stack

- **Language:** Java 21
- **Framework:** Spring Boot 3.5 (Web, Security, Validation, Thymeleaf)
- **Data Access:** Spring JDBC (`JdbcTemplate`) — **no JPA for analytics**
- **DB:** PostgreSQL 15 (Docker)
- **Migrations:** Flyway 11 (`flyway-core` + `flyway-database-postgresql`)
- **UI:** Thymeleaf + Bootstrap 5 + Chart.js
- **Auth:** Spring Security (BCrypt), roles: `USER`, `ADMIN`
- **Dev/Infra:** Docker Compose (Postgres + optional pgAdmin)

---

## Architecture

**Controller → Service → Repository** layering (no business logic in controllers).

```
controller/
  HomeController
  AuthController            (REST)
  AuthViewController        (MVC)
  AdminController           (MVC: statistics)

service/
  AuthService
  AdminService

repository/
  UserRepository            (JdbcTemplate)
  AdminRepository           (raw SQL reporting)

dto/
  RegisterForm
  ProductSalesDTO
  MonthlyRevenueDTO
  LowStockDTO

config/
  SecurityConfig            (form login + guards)
```

Views: `index.html`, `login.html`, `register.html`, `statistics.html` (Bootstrap, Thymeleaf, role-aware navbar).

---

## Database Schema

Core tables:

- `users` (name, email, password_hash, role, created_at)
- `categories`, `products` (price, category_id, created_at)
- `sizes` and **junction** `product_sizes(product_id, size_id, stock)`
- `orders` and `order_items` (quantity, price)
- `payments` (amount, method)
- `reviews` (rating 1–5, comment)

**Trigger:** `trg_decrease_stock` with `decrease_stock()` → decrements `product_sizes.stock` automatically when a new `order_items` row is inserted.

---

## Getting Started

### 1) Run Postgres (Docker Compose)

```bash
docker compose up -d
# exposes postgres on localhost:5432; adjust compose if needed
```

### 2) Configure App (already set by default)

`src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/clothing_store
spring.datasource.username=admin
spring.datasource.password=secret

spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
```

### 3) Start the App

```bash
./mvnw spring-boot:run
```

Open:
- App: <http://localhost:8080>
- Register: <http://localhost:8080/register>
- Login: <http://localhost:8080/login>
- Admin Statistics: <http://localhost:8080/admin/statistics> (ADMIN only)

---

## Migrations & Demo Data

All migrations live in `src/main/resources/db/migration/`:

- **V1__schema.sql** — base schema + trigger
- **V2__seed.sql** — sizes, categories, products, stock
- **V3__admin_user.sql / V4__set_admin_password.sql** — ensures an admin user exists with a valid BCrypt password
- **V5__demo_orders.sql** — demo orders & order_items across months (drives statistics)

> If you previously created tables manually, you can enable `spring.flyway.baseline-on-migrate=true` temporarily.

---

## Current Features (Done)

- ✅ **Auth**: registration (server-side validation), custom login page, BCrypt hashing
- ✅ **RBAC**: `USER`/`ADMIN`, role-aware navbar and views
- ✅ **Home page**: Bootstrap-styled, role-aware CTAs
- ✅ **Admin statistics** (raw SQL via `JdbcTemplate`):
    - Top sellers (aggregate quantities) — table + **bar chart**
    - Monthly revenue (grouped by month) — table + **line chart**
    - Low stock alert (threshold parameter) — table
- ✅ **Demo data** (orders, items) to make statistics non-empty
- ✅ **Database trigger** to decrement stock automatically

Screenshots (example): _see `/admin/statistics` with seeded data_.

---

## Admin Account

- **Default admin email:** `admin@store.test`
- **Password:** whichever you set in migration (`V4__set_admin_password.sql`) or via a bootstrap routine.
- Reset quickly by generating a BCrypt hash and updating `users.password_hash`:

```sql
UPDATE users
SET password_hash = '<PASTE_BCRYPT_HASH>'
WHERE email = 'admin@store.test';
```

---

## Project TODO / Roadmap

### 0) Foundations / Environment
- [x] PostgreSQL 15 via Docker Compose (+ optional pgAdmin)
- [x] Spring Boot 3.5 project (Web, Security, JDBC, Validation, Thymeleaf, Lombok)
- [x] Flyway migrations (V1–V5)
- [x] App config for datasource/Flyway
- [x] Index page with Bootstrap styling

### 1) Security & Auth
- [x] Controller → Service → Repository layering
- [x] Password hashing (BCrypt)
- [x] Registration form (DTO validation + errors)
- [x] Custom login page + form login
- [x] Role-based access (`USER`, `ADMIN`)
- [ ] **Enable CSRF** (currently disabled; re-enable + include hidden tokens in forms)
- [ ] Password reset (token table + email link)
- [ ] Email verification on signup (optional)
- [ ] Session management / remember-me
- [ ] Login rate limiting (basic in-memory or bucket4j)

### 2) Domain / Database
- [x] Users / Roles
- [x] Catalog: Categories, Products, Sizes, Product–Size stock (M:N), seed data
- [x] Orders / Order Items / Payments
- [x] Reviews
- [x] Stock trigger on order items
- [ ] Addresses (`user_addresses` with default flags)
- [ ] Shipping methods (flat / weight / free over threshold)
- [ ] Coupons/Promotions (percent/fixed; validity windows, usage limits)
- [ ] Wishlist (user ↔ product)
- [ ] Product images (main + gallery)
- [ ] Search indexes (BTREE/GIN) for performance

### 3) Backend — User Features (Raw SQL)
- [ ] **Catalog**
    - [ ] List products (pagination, sort: price/date/popularity)
    - [ ] Filters (category, size, price range, full-text name/desc)
    - [ ] Product details (images, sizes & stock, reviews)
- [ ] **Cart**
    - [ ] `carts` + `cart_items` (per user; guest cart optional)
    - [ ] Add/Remove/Update; **validate stock**
- [ ] **Checkout**
    - [ ] Address + shipping + mock payment
    - [ ] **Transaction**: create `orders` + `order_items` atomically
    - [ ] Trigger handles stock; rollback on failure
    - [ ] Confirmation page
- [ ] **Payments (mock)**
    - [ ] Payment form (Luhn check) + record in `payments` → mark order `PAID`
- [ ] **My Orders**
    - [ ] Orders list (status, total, date)
    - [ ] Order details (items, prices, shipping)
- [ ] **Reviews**
    - [ ] Add a review (purchased products only)
    - [ ] Edit/Delete (policy + admin override)
    - [ ] Aggregate rating on product page

### 4) Backend — Admin Features (Raw SQL)
- [x] Statistics page (tables + Chart.js)
- [ ] Product CRUD (create/edit/delete)
- [ ] Manage images, categories, size stock
- [ ] Orders management (search, status pipeline)
- [ ] Refund flow (reverse payment + restock)
- [ ] Users management (list, toggle roles, disable/enable)
- [ ] Reports:
    - [ ] Revenue by category
    - [ ] Average order value (AOV)
    - [ ] Repeat customers & simple LTV
    - [ ] Best vs worst reviewed (avg rating, count)

### 5) Frontend (Thymeleaf + Bootstrap)
- [x] Base styling for `index`, `login`, `register`
- [x] Admin statistics (tables + charts)
- [ ] Products grid (filters, sorting)
- [ ] Product details (gallery, size picker, add-to-cart)
- [ ] Cart page (edit quantities, totals)
- [ ] Checkout (address, shipping, payment)
- [ ] My Orders (list + details)
- [ ] Reviews UI
- [ ] Admin CRUD screens
- [ ] Reusable fragments (navbar/footer/layout)
- [ ] Client-side validation (optional)

### 6) Performance & SQL Quality
- [ ] **Indexes**
    - [ ] `CREATE INDEX idx_products_category ON products(category_id);`
    - [ ] `CREATE INDEX idx_order_items_product ON order_items(product_id);`
    - [ ] `CREATE INDEX idx_orders_user_date ON orders(user_id, order_date);`
    - [ ] Full-text example:  
      `CREATE INDEX idx_products_name_gin ON products USING gin (to_tsvector('simple', name));`
    - [ ] `CREATE INDEX idx_reviews_product ON reviews(product_id);`
- [ ] **EXPLAIN/ANALYZE** docs (screenshots, before/after index)
- [ ] **Views / Materialized Views** (e.g., `monthly_revenue_mv`)
- [ ] **Window functions** (top-N per category, running totals)
- [ ] **Transactions & isolation** notes/demos
- [ ] **Constraints** documented and tested
- [ ] **Audit triggers**:
    - [ ] `order_status_audit` (order_id, old/new status, changed_by, changed_at)
    - [ ] `inventory_audit` for stock changes

### 7) Observability, Logs & Auditing
- [ ] Request logging
- [ ] Curated SQL logs (JDBC DEBUG + sample queries)
- [ ] Error pages (4xx/5xx)

### 8) Emails & Notifications (Optional)
- [ ] Local mail (MailDev container)
- [ ] Emails: order confirmation, status updates, password reset, low stock alerts

### 9) Testing & Quality
- [ ] Unit tests (services, validators)
- [ ] **Integration tests** with **Testcontainers (PostgreSQL)**:
    - [ ] Registration & login
    - [ ] Cart → checkout transaction → stock decrement
    - [ ] Admin stats queries return expected values
- [ ] Migration test: apply Flyway to fresh container, assert schema
- [ ] Simple UI tests (Thymeleaf renders, auth guards)

### 10) DevOps & Delivery
- [x] Compose for Postgres 15 (+ pgAdmin)
- [ ] Profiles (`dev`, `prod`) — logging, CSRF, CORS, debug flags
- [ ] Container image via Buildpacks or Dockerfile
- [ ] CI (GitHub Actions): build, test, Flyway validate
- [ ] Backup scripts (pg_dump) + restore instructions

### 11) Documentation
- [x] Setup (Docker + app config)
- [x] Admin seeding/password guidance
- [x] Statistics screenshots
- [ ] Full ER diagram (dbdiagram.io / draw.io)
- [ ] “How to run” scripts/Makefile
- [ ] “DB learning highlights” (triggers, joins, aggregates, windows, MV, indexes, EXPLAIN)
- [ ] Security notes (hashing, CSRF)
- [ ] Demo script for presentation

---

## DB Highlights to Demonstrate

- **Trigger** `trg_decrease_stock`: reduces stock on each `order_items` insert.
- **Analytics** queries with joins, aggregations, date truncation.
- **Migrations**: schema evolution with Flyway (idempotent seeds, conditional inserts).
- **Constraints**: FKs, uniques, checks (e.g., rating BETWEEN 1 AND 5).
- **Indexes/EXPLAIN** (to be added) — showcase performance improvements.
- **Transactions**: checkout will wrap inserts in a single transaction.

---
## Suggested Demo Script

1. **Register** a new user and **login**; show role-based navbar.
2. Switch to **admin** (`admin@store.test`) and open **/admin/statistics**.
3. Show **Top Sellers** and **Monthly Revenue** (tables + charts) — explain raw SQL in repo.
4. Insert another order in pgAdmin; **refresh** to see charts update.
5. Highlight the **stock trigger** by inserting an `order_items` row and showing decreased stock.
6. Outline the **roadmap** items (cart, checkout, reviews, indexes, EXPLAIN plan), pointing to this README.

---

Happy building! 🚀
