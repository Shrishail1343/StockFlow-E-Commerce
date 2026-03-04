# 📦 StockFlow — E-Commerce Inventory & Order Management System

A full-stack inventory management system with a modern SaaS-inspired UI, Spring Boot REST API, MySQL database, and vanilla JS frontend.

---

## 🚀 Tech Stack

| Layer | Technology |
|-------|-----------|
| Backend | Spring Boot 3.2, Spring Security, JPA/Hibernate |
| Database | MySQL 8.0 |
| Auth | JWT (JSON Web Tokens) |
| Frontend | HTML5, CSS3 (Flexbox/Grid), Vanilla JavaScript |
| Charts | Chart.js 4.4 |

---

## 📁 Project Structure

```
ecommerce-system/
├── backend/
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/inventory/
│       │   ├── InventoryManagementApplication.java
│       │   ├── config/
│       │   │   ├── SecurityConfig.java       # JWT + CORS security
│       │   │   ├── JwtUtil.java              # Token generation & validation
│       │   │   └── JwtAuthFilter.java        # Request filter
│       │   ├── controller/
│       │   │   ├── AuthController.java       # POST /auth/login
│       │   │   ├── ProductController.java    # CRUD /products
│       │   │   ├── OrderController.java      # CRUD /orders
│       │   │   └── DashboardController.java  # GET /dashboard/stats
│       │   ├── service/
│       │   │   ├── AuthService.java
│       │   │   ├── ProductService.java       # Business logic + stock management
│       │   │   ├── OrderService.java         # Order processing + stock deduction
│       │   │   ├── DashboardService.java
│       │   │   ├── InventoryLogService.java  # Audit trail
│       │   │   └── CustomUserDetailsService.java
│       │   ├── model/                        # JPA Entities
│       │   │   ├── User.java
│       │   │   ├── Product.java
│       │   │   ├── Category.java
│       │   │   ├── Customer.java
│       │   │   ├── Order.java
│       │   │   ├── OrderItem.java
│       │   │   └── InventoryLog.java
│       │   ├── repository/                   # Spring Data JPA repos
│       │   │   ├── UserRepository.java
│       │   │   ├── ProductRepository.java    # Custom JPQL queries
│       │   │   ├── OrderRepository.java
│       │   │   ├── CategoryRepository.java
│       │   │   └── CustomerRepository.java
│       │   ├── dto/                          # Data Transfer Objects
│       │   └── exception/
│       │       └── ResourceNotFoundException.java
│       └── resources/
│           ├── application.properties
│           └── schema.sql                    # DB schema + sample data
│
└── frontend/
    ├── login.html          # Authentication page
    ├── dashboard.html      # Main dashboard with charts
    ├── products.html       # Product CRUD management
    ├── orders.html         # Order tracking & status updates
    ├── inventory.html      # Stock monitoring & restocking
    ├── css/
    │   └── dashboard.css   # Complete design system (1 file)
    └── js/
        ├── api.js          # Fetch-based API client
        └── utils.js        # Auth, dark mode, toasts
```

---

## ⚙️ Backend Setup

### Prerequisites
- Java 17+
- Maven 3.8+
- MySQL 8.0+

### 1. Database Setup
```sql
-- Run in MySQL
CREATE DATABASE inventory_db;
-- Then run schema.sql from resources/
```

### 2. Configure application.properties
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/inventory_db
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

### 3. Run the backend
```bash
cd backend
mvn spring-boot:run
# API available at http://localhost:8080/api
```

---

## 🎨 Frontend Setup

No build step required — pure HTML/CSS/JS.

### Option 1: VS Code Live Server
```
Open frontend/ in VS Code → Right-click login.html → "Open with Live Server"
```

### Option 2: Python HTTP Server
```bash
cd frontend
python3 -m http.server 5500
# Open http://localhost:5500/login.html
```

### Demo Credentials
```
Username: admin
Password: admin123
```

> **Note:** The frontend works in **Demo Mode** even without the backend running. All data is pre-populated for preview.

---

## 🔌 REST API Endpoints

### Authentication
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/login` | Login → returns JWT token |
| GET | `/api/auth/me` | Get current user |

### Products
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/products` | List with search/filter/pagination |
| GET | `/api/products/{id}` | Get by ID |
| POST | `/api/products` | Create product |
| PUT | `/api/products/{id}` | Update product |
| DELETE | `/api/products/{id}` | Delete product |
| GET | `/api/products/low-stock` | Get low stock items |
| PATCH | `/api/products/{id}/stock` | Update stock quantity |
| GET | `/api/products/stats` | Product statistics |

### Orders
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/orders` | List with filter/pagination |
| GET | `/api/orders/{id}` | Get order details |
| POST | `/api/orders` | Create order (auto-decrements stock) |
| PATCH | `/api/orders/{id}/status` | Update order status |
| GET | `/api/orders/stats` | Order statistics |
| GET | `/api/orders/revenue/monthly` | Monthly revenue data |

### Dashboard
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/dashboard/stats` | All KPI stats |
| GET | `/api/dashboard/recent-orders` | Last 5 orders |
| GET | `/api/dashboard/top-products` | Top selling products |

---

## ✨ Key Features

### UI Design
- **SaaS-inspired** dark sidebar + light content area
- **Syne** (display) + **DM Sans** (body) font pairing
- Soft card shadows, 12px rounded corners
- Smooth CSS transitions on all interactions
- Full **dark mode** with `localStorage` persistence
- Fully **responsive** (desktop + tablet + mobile)

### Functional
- ✅ JWT-based admin authentication
- ✅ Product CRUD with real-time search & filtering
- ✅ Order management with status workflow
- ✅ Stock auto-decrements on order creation
- ✅ Stock restores on order cancellation
- ✅ Low stock alerts with visual indicators
- ✅ Revenue chart (Chart.js line graph)
- ✅ Order status donut chart
- ✅ Toast notifications for all actions
- ✅ Loading spinners for async operations
- ✅ Paginated tables with sort controls

---

## 🎨 Color System

```css
--primary: #2563eb;    /* Blue — CTAs, active states */
--accent:  #00d4aa;    /* Teal — highlights, logo */
--green:   #10b981;    /* Success, in-stock */
--orange:  #f97316;    /* Warning, low stock */
--red:     #ef4444;    /* Error, critical stock */
--yellow:  #f59e0b;    /* Pending status */
--purple:  #8b5cf6;    /* Shipped status */
```

---

## 📦 Adding More Features

### Add a new page
1. Copy `products.html` structure (sidebar + topbar already templated)
2. Add nav link to all pages
3. Create API endpoint in Spring Boot
4. Call via `api.get()` / `api.post()` in your script

### Add Chart.js chart
```javascript
new Chart(ctx, {
    type: 'bar',
    data: { labels: [...], datasets: [{ data: [...] }] },
    options: { responsive: true, plugins: { legend: { display: false } } }
});
```
