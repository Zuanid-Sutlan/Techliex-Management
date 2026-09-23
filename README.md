# Techliex Management - Backend Migration & API Specification

This document provides a comprehensive technical overview, database schema, API specification, and migration guide to transition the **Techliex Management** Android application from Firebase Realtime Database to a custom, dedicated backend service.

---

## Table of Contents
1. [System Overview](#system-overview)
2. [Database Schema Specification](#database-schema-specification)
3. [API Route Specifications](#api-route-specifications)
4. [Role-Based Access Control (RBAC) & Business Logic](#role-based-access-control-rbac--business-logic)
5. [Master Backend Generation Prompt](#master-backend-generation-prompt)
6. [Android Client Migration Strategy](#android-client-migration-strategy)

---

## System Overview

Techliex Management is an internal enterprise Android application designed for product hunting, team collaboration, warehouse price management, order processing, and tracking logistics.

### Key Features
* **User & Role Management**: Multi-role system (`Admin`, `Member`, `Warehouse`).
* **Product Hunting**: Research and share product hunt opportunities with team members.
* **Warehouse Management**: Track cost estimates, notes, and status updates for hunted items.
* **Order Processing**: Create orders, upload payment proofs, and manage shipping/tracking details.
* **Dashboard Analytics**: Metrics tracking total products, active orders, and revenue earnings.

---

## Database Schema Specification

Below is the relational database schema design (PostgreSQL / MySQL compatible) required for the custom backend.

### 1. `users` Table
| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | BigInt / Serial | PRIMARY KEY, AUTO_INCREMENT | Unique User ID |
| `username` | VarChar(50) | UNIQUE, NOT NULL, INDEXED | User account login username |
| `password_hash` | VarChar(255) | NOT NULL | Hashed password (bcrypt / Argon2) |
| `name` | VarChar(100) | NOT NULL | Display name |
| `role` | VarChar(20) | NOT NULL, DEFAULT 'Member' | Role (`Admin`, `Member`, `Warehouse`) |
| `is_active` | Boolean | DEFAULT TRUE | Account active/inactive status |
| `created_at` | Timestamp | DEFAULT CURRENT_TIMESTAMP | Record creation timestamp |

### 2. `products` Table
| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | BigInt / Serial | PRIMARY KEY, AUTO_INCREMENT | Unique Product Hunt ID |
| `user_id` | BigInt | FOREIGN KEY -> `users(id)`, NOT NULL | Creator User ID |
| `title` | VarChar(255) | NOT NULL | Product title |
| `description` | Text | NULLABLE | Detailed description / notes |
| `product_image_url` | Text | NOT NULL | Image URL or storage path |
| `source_link` | Text | NOT NULL | Supplier / Source URL |
| `source_price` | Decimal(10,2) | NOT NULL | Purchase cost from source |
| `reference_link` | Text | NOT NULL | Reference listing URL |
| `reference_price` | Decimal(10,2) | NOT NULL | Market / Reference price |
| `warehouse_price` | Decimal(10,2) | DEFAULT 0.00 | Warehouse cost added by Admin/Warehouse |
| `warehouse_note` | Text | NULLABLE | Warehouse notes |
| `created_at` | Timestamp | DEFAULT CURRENT_TIMESTAMP | Creation timestamp |

### 3. `product_shares` Table (Junction Table)
| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | BigInt / Serial | PRIMARY KEY | Junction record ID |
| `product_id` | BigInt | FOREIGN KEY -> `products(id)` ON DELETE CASCADE | Target product |
| `shared_user_id` | BigInt | FOREIGN KEY -> `users(id)` ON DELETE CASCADE | Granted user access |

### 4. `orders` Table
| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | BigInt / Serial | PRIMARY KEY, AUTO_INCREMENT | Unique Order ID |
| `order_date` | VarChar(20) | NOT NULL | Date string (`dd/MM/yyyy`) |
| `user_id` | BigInt | FOREIGN KEY -> `users(id)`, NOT NULL | Ordering member |
| `product_id` | BigInt | FOREIGN KEY -> `products(id)`, NOT NULL | Associated product hunt |
| `address` | Text | NOT NULL | Shipping address |
| `variation_note` | Text | NULLABLE | Notes on color, size, variation |
| `quantity` | Int | NOT NULL | Quantity ordered |
| `listing_price` | Decimal(10,2) | NOT NULL | Selling price |
| `payment_image_url` | Text | NOT NULL | Proof of payment image URL |
| `track_id` | VarChar(100) | DEFAULT '' | Courier tracking number |
| `company` | VarChar(100) | DEFAULT '' | Logistics company name |
| `status` | VarChar(20) | DEFAULT 'Active' | Order state (`Active`, `Shipped`, `Completed`) |
| `created_at` | Timestamp | DEFAULT CURRENT_TIMESTAMP | Placement timestamp |

---

## API Route Specifications

All protected routes require an `Authorization: Bearer <JWT_TOKEN>` HTTP header.

### 1. Authentication Module (`/api/v1/auth`)
* `POST /api/v1/auth/login`
  * **Auth**: Public
  * **Payload**: `{ "username": "john_doe", "password": "password123" }`
  * **Response**: `{ "token": "JWT_TOKEN_HERE", "user": { "id": 1, "username": "john_doe", "name": "John", "role": "Admin", "isActive": true } }`

* `GET /api/v1/auth/me`
  * **Auth**: Protected
  * **Response**: Returns current user profile details.

### 2. User Management Module (`/api/v1/users`)
* `POST /api/v1/users`
  * **Auth**: Admin Only
  * **Payload**: `{ "username": "...", "password": "...", "name": "...", "role": "Member" }`
  * **Response**: Creates new user account.

* `GET /api/v1/users`
  * **Auth**: Protected
  * **Response**: Returns array of all active users.

* `GET /api/v1/users/:username`
  * **Auth**: Protected
  * **Response**: Returns single user record.

* `DELETE /api/v1/users/:username`
  * **Auth**: Admin Only
  * **Response**: Soft deletes or removes user.

### 3. Product Hunt Module (`/api/v1/products`)
* `POST /api/v1/products`
  * **Auth**: Protected
  * **Payload**: Product fields + `shareWithUsernames: ["user1", "user2"]`. Handles image upload or URL.

* `GET /api/v1/products`
  * **Auth**: Protected
  * **Response**: List products based on role visibility rules.

* `GET /api/v1/products/:id`
  * **Auth**: Protected
  * **Response**: Returns product hunt details.

* `PATCH /api/v1/products/:id/warehouse`
  * **Auth**: Admin / Warehouse Only
  * **Payload**: `{ "warehousePrice": 120.00, "warehouseNote": "In Stock" }`

* `DELETE /api/v1/products/:id`
  * **Auth**: Admin Only
  * **Response**: Deletes specified product.

### 4. Orders Module (`/api/v1/orders`)
* `POST /api/v1/orders`
  * **Auth**: Protected
  * **Payload**: Order placement payload + payment proof image URL/data.

* `GET /api/v1/orders`
  * **Auth**: Protected
  * **Response**: List orders according to role scope, sorted by status (`Active` -> `Shipped` -> `Completed`).

* `GET /api/v1/orders/:id`
  * **Auth**: Protected
  * **Response**: Order details.

* `PATCH /api/v1/orders/:id/logistics`
  * **Auth**: Admin Only
  * **Payload**: `{ "trackId": "TRK987654", "company": "FedEx" }`
  * **Behavior**: Sets `trackId`, `company`, and changes status to `"Shipped"`.

* `PATCH /api/v1/orders/:id/complete`
  * **Auth**: Protected
  * **Behavior**: Updates order status to `"Completed"`.

### 5. Dashboard & Upload Modules
* `GET /api/v1/dashboard/stats`
  * **Auth**: Protected
  * **Response**: `{ "productCount": 25, "activeOrdersCount": 8, "totalEarnings": "12500.00" }`

* `POST /api/v1/media/upload`
  * **Auth**: Protected
  * **Payload**: `multipart/form-data` image file.
  * **Response**: `{ "imageUrl": "https://cdn.domain.com/uploads/image_123.jpg" }`

---

## Role-Based Access Control (RBAC) & Business Logic

1. **Admin Permissions**:
   * Access to view ALL products, orders, and dashboard earnings.
   * Exclusive power to manage users, update order logistics (`trackId`, `company`), and delete products/members.
2. **Member Permissions**:
   * View products created by themselves or explicitly shared with them via `shareWith`.
   * View and manage only orders created by themselves.
3. **Warehouse Permissions**:
   * Granted access to update product warehouse costs and notes.
4. **Order State Lifecycle**:
   * `Active` (Default upon placement) -> `Shipped` (Set when Admin adds shipping/tracking info) -> `Completed` (Finalized status).

---

## Master Backend Generation Prompt

You can copy and paste the following prompt into an LLM tool to automatically generate the complete backend service:

```markdown
# MASTER BACKEND CODE GENERATION PROMPT

## Objective
Build a complete, production-ready REST API backend for the Techliex Management system using Node.js (Express/TypeScript), Spring Boot, Ktor, Go, or FastAPI with PostgreSQL database.

## Requirements
1. Implement JWT-based Authentication (`/api/v1/auth/login`, `/api/v1/auth/me`).
2. Implement Users CRUD (`/api/v1/users`) with password hashing (bcrypt).
3. Implement Products API (`/api/v1/products`) with junction table support for product sharing (`shareWith`).
4. Implement Orders API (`/api/v1/orders`) with status transition lifecycle (`Active` -> `Shipped` -> `Completed`).
5. Implement Warehouse Updates (`PATCH /api/v1/products/:id/warehouse`).
6. Implement Logistics Updates (`PATCH /api/v1/orders/:id/logistics`).
7. Implement Dashboard Analytics (`GET /api/v1/dashboard/stats`).
8. Implement File/Media Upload endpoint (`POST /api/v1/media/upload`).
9. Include JWT Auth & RBAC Middleware enforcing Admin, Member, and Warehouse roles.
10. Provide SQL migration scripts and sample seed data.
```

---

## Android Client Migration Strategy

To connect the Android application to the custom backend:

1. **Add Network Dependencies**:
   * Add **Retrofit2** or **Ktor Client** along with **Kotlinx Serialization** to `build.gradle.kts`.
2. **Implement API Interfaces**:
   * Define `AuthApi`, `UserApi`, `ProductApi`, and `OrderApi` interfaces.
3. **Replace Repositories**:
   * Replace `UserRepositoryImpl` and Firebase calls in ViewModels with clean Repository calls interacting with Retrofit APIs.
4. **JWT Storage**:
   * Store the JWT returned during login inside `DataStore` alongside user preferences and attach it via an OkHttp `Interceptor`.
