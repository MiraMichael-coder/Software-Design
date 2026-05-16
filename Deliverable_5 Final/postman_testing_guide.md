# CRM API - Postman Testing Guide & Samples

This document contains all the exact requests and responses you need to copy into Postman for your project deliverables. You can use these to easily take your screenshots.

---

## 📦 1. Inventory API (Port 8080)

### 1.1 Create a New Product (POST)
**Endpoint:** `http://localhost:8080/api/v1/inventory/products`
**Method:** `POST`
**Headers:** `Content-Type: application/json`

**Request Body (Raw JSON):**
```json
{
  "name": "Wireless Mechanical Keyboard",
  "description": "RGB backlight, hot-swappable switches",
  "priceAmount": 129.50,
  "priceCurrency": "USD",
  "stockQuantity": 50,
  "supplierId": "SUP-01"
}
```

**Expected Response (201 Created):**
```json
{
  "productId": "PROD-100",
  "name": "Wireless Mechanical Keyboard",
  "description": "RGB backlight, hot-swappable switches",
  "unitPrice": {
    "amount": 129.5,
    "currency": "USD"
  },
  "stockQuantity": 50,
  "supplierId": "SUP-01"
}
```

### 1.2 Get All Products (GET)
**Endpoint:** `http://localhost:8080/api/v1/inventory/products`
**Method:** `GET`

**Expected Response (200 OK):**
```json
[
  {
    "productId": "PROD-01",
    "name": "Wireless Mouse",
    "description": "Ergonomic wireless mouse",
    "unitPrice": {
      "amount": 149.99,
      "currency": "EGP"
    },
    "stockQuantity": 20,
    "supplierId": "SUP-01"
  },
  {
    "productId": "PROD-100",
    "name": "Wireless Mechanical Keyboard",
    "description": "RGB backlight, hot-swappable switches",
    "unitPrice": {
      "amount": 129.5,
      "currency": "USD"
    },
    "stockQuantity": 50,
    "supplierId": "SUP-01"
  }
]
```

### 1.3 Update a Product (PUT)
**Endpoint:** `http://localhost:8080/api/v1/inventory/products/PROD-100`
**Method:** `PUT`
**Headers:** `Content-Type: application/json`

**Request Body (Raw JSON):**
```json
{
  "name": "Wireless Mechanical Keyboard v2",
  "basePrice": 115.00,
  "description": "Updated model with better battery",
  "supplierId": "SUP-02"
}
```

**Expected Response (200 OK):**
```json
{
  "productId": "PROD-100",
  "name": "Wireless Mechanical Keyboard v2",
  "description": "Updated model with better battery",
  "unitPrice": {
    "amount": 115.0,
    "currency": "USD"
  },
  "stockQuantity": 50,
  "supplierId": "SUP-02"
}
```

### 1.4 Delete a Product (DELETE)
**Endpoint:** `http://localhost:8080/api/v1/inventory/products/PROD-100`
**Method:** `DELETE`

**Expected Response (200 OK):**
*(Empty response body with HTTP 200 OK status code)*

---

## 🛒 2. Order API (Port 8081)

### 2.1 Create a New Order with Items (POST)
**Endpoint:** `http://localhost:8081/api/v1/orders`
**Method:** `POST`
**Headers:** `Content-Type: application/json`

**Request Body (Raw JSON):**
```json
{
  "customerId": "CUST-001",
  "items": [
    {
      "productId": "PROD-01",
      "quantity": 2,
      "priceAmount": 149.99,
      "priceCurrency": "EGP"
    }
  ]
}
```

**Expected Response (201 Created):**
```json
{
  "orderId": "ORD-100",
  "customerId": "CUST-001",
  "createdAt": "2026-05-15T10:00:00.000",
  "lastUpdatedAt": "2026-05-15T10:00:00.000",
  "status": "Pending",
  "items": [
    {
      "orderItemId": "ORD-100-ITEM-1",
      "productId": "PROD-01",
      "productName": "Wireless Mouse",
      "quantity": 2,
      "unitPrice": {
        "amount": 149.99,
        "currency": "EGP"
      },
      "subtotal": {
        "amount": 299.98,
        "currency": "EGP"
      }
    }
  ],
  "totalAmount": {
    "amount": 299.98,
    "currency": "EGP"
  }
}
```

### 2.2 Update Order Delivery Address (PUT)
**Endpoint:** `http://localhost:8081/api/v1/orders/ORD-100/delivery`
**Method:** `PUT`
**Headers:** `Content-Type: application/json`

**Request Body (Raw JSON):**
```json
{
  "address": "15 Main St, Cairo, Egypt"
}
```

**Expected Response (200 OK):**
```json
{
  "orderId": "ORD-100",
  "customerId": "CUST-001",
  "status": "Pending",
  "delivery": {
    "deliveryId": "DEL-1685458392",
    "orderId": "ORD-100",
    "deliveryAddress": {
      "street": "15 Main St",
      "city": "Cairo",
      "zone": "Egypt"
    },
    "estimatedDeliveryDate": "2026-05-18T10:00:00.000",
    "status": "Scheduled"
  },
  "items": [ ... ],
  "totalAmount": { ... }
}
```

### 2.3 Remove Item from Order (DELETE)
**Endpoint:** `http://localhost:8081/api/v1/orders/ORD-100/items/ORD-100-ITEM-1`
**Method:** `DELETE`

**Expected Response (200 OK):**
*(Returns the updated order object without the deleted item)*
```json
{
  "orderId": "ORD-100",
  "customerId": "CUST-001",
  "status": "Pending",
  "items": [],
  "totalAmount": null
}
```
