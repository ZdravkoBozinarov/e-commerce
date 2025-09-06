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