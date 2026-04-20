# CRM Main.java - Documentation & Pattern Walkthrough

## Overview
`Main.java` serves as an interactive console-based test layer for the entire CRM system. It provides menu-driven access to all subsystems (Customer Support, Inventory, Orders & Payments, Reporting) and demonstrates the correct application of multiple design patterns.

---

## Architecture

### Bootstrap Flow
```
main()
  ├── setupSharedObjects()        // Initialize shared domain objects
  ├── bootstrapControllers()      // Wire up all controllers and repositories
  ├── seedSampleData()            // Populate repositories with test data
  └── runInteractiveConsole()     // Start the menu-driven interface
```

---

## Design Patterns Demonstrated

### 1. **Singleton Pattern**
**Location**: `Main.java:133-135` via `InventoryEventManager.getInstance()`
```java
InventoryEventManager.getInstance().register(inventoryAlertController);
InventoryEventManager.getInstance().register(inventoryController);
InventoryEventManager.getInstance().register(supplierController);
```
**Purpose**: Ensures only one `InventoryEventManager` exists to coordinate stock change notifications across the system.

---

### 2. **Strategy Pattern** (SLA Calculation)
**Location**: Customer Support → Create Complaint (Menu option 5)
**Classes**: `SlaCalculator` interface with implementations:
- `StandardSlaCalculator`
- `PriorityBasedSlaCalculator` 
- `VipSlaCalculator`

```java
// User selects strategy at runtime
switch (slaChoice) {
    case 1 -> calculator = new StandardSlaCalculator();
    case 2 -> calculator = new VipSlaCalculator();
    case 3 -> calculator = new PriorityBasedSlaCalculator(24);
}
Complaint complaint = new Complaint(complaintId, priority, calculator);
```
**Purpose**: Allows interchangeable algorithms for calculating SLA deadlines based on customer priority.

---

### 3. **Adapter Pattern** (ERP Integration)
**Location**: Inventory → Fetch Supplier from ERP (Menu option 5)
**Classes**: `ErpSupplierAdapter`, `ErpSystem`

```java
// Adapt external ERP data to internal Supplier model
public void importFromErp(String supplierId) {
    // ERP returns raw data → Adapter transforms to Supplier object
    Supplier supplier = erpAdapter.fetchSupplier(supplierId);
    supplierRepository.save(supplierId, supplier);
}
```
**Purpose**: Converts incompatible ERP supplier data into the CRM's `Supplier` model without changing either system.

---

### 4. **Mediator Pattern** (Order Fulfillment)
**Location**: Orders & Payments → Checkout Flow (Menu option 3)
**Class**: `OrderFulfillmentMediator`

```java
mediator.processCheckout(order, paymentTx, customer, delivery, paymentFactory, commFactory);
```
**Purpose**: Centralizes complex checkout coordination between Order, Payment, Customer, Delivery, and Communication subsystems, preventing tight coupling.

---

### 5. **Bridge Pattern** (Report Rendering)
**Location**: Reports Menu
**Abstraction**: `Report`  
**Implementor**: `ReportRenderer` (ConsoleReportRenderer, CsvReportRenderer)

```java
ReportFactory factory = selectReportFactory(reportType, renderer);
Report report = factory.createReport("RPT-" + System.currentTimeMillis());
report.generate();  // Rendered according to selected strategy (Console/CSV)
```
**Purpose**: Decouples report generation from rendering format, allowing any report type to be rendered via any output format.

---

### 6. **Factory Method / Abstract Factory** (Payment & Communication)
**Location**: Checkout Flow, Send Message
**Payment Factories**: `CardPaymentFactory`, `CodPaymentFactory`, `StripePaymentFactory`
**Communication Factories**: `SMSFactory`, `EmailFactory`, `ChatFactory`

```java
// Abstract Factory for Payments
PaymentFactory paymentFactory = selectPaymentFactory(method);
PaymentTransaction paymentTx = new PaymentTransaction(...);

// Abstract Factory for Communications
CommunicationFactory commFactory = selectCommunicationFactory();
communicationController.sendMessage(commFactory, customer.getEmail(), messageText);
```
**Purpose**: Creates families of related objects (payment processors, communication channels) without specifying concrete classes.

---

### 7. **Decorator Pattern** (Alert System)
**Location**: Demo Suite → `testAlertSystem()`
**Classes**: `LoggingAlertDecorator`, `RetryAlertDecorator`, `EscalatingAlertDecorator`

```java
SystemAlert decorated = new LoggingAlertDecorator(
    new RetryAlertDecorator(
        new EscalatingAlertController(...), 3),
    System.out::println);
```
**Purpose**: Adds responsibilities to alerts dynamically (logging, retry logic, escalation) without subclassing.

---

### 8. **Observer Pattern** (Stock Monitoring)
**Location**: `Product.java`, `InventoryEventManager`
**Subjects**: `Product` (notifies on stock change)
**Observers**: `InventoryAlertController`, `InventoryController`, `SupplierController`

```java
// Product notifies observers when stock changes
public void decreaseStock(int amount) {
    this.stockQuantity -= amount;
    InventoryEventManager.getInstance().notifyStockChanged(this);
}
```
**Purpose**: Automatic reaction to stock changes - alerts fire and records update when inventory levels change.

---

### 9. **Iterator Pattern** (Order Items)
**Location**: Orders & Payments → View Order Items (Menu option 5)
**Classes**: `HVOrderitem` (custom iterator)

```java
// Standard Java Iterator
Iterator<OrderItem> itemIterator = order.getItems().iterator();
while (itemIterator.hasNext()) { ... }

// Custom High-Value Iterator (filters by price threshold)
Iterator<OrderItem> hvIterator = order.highValueItemIterator(threshold);
while (hvIterator.hasNext()) { ... }
```
**Purpose**: Sequential access to order items; custom iterator adds filtering capability without exposing internal collection structure.

---

### 10. **Flyweight Pattern** (Product Management)
**Location**: `seedSampleData()` via `ProductFactory.getOrCreate()`
**Class**: `ProductFactory`

```java
// Ensures product instances are shared/reused
Product product1 = ProductFactory.getOrCreate("PROD-01", "Wireless Mouse", ...);
```
**Purpose**: Minimizes memory usage by sharing product instances across the system.

---

## Subsystem Navigation

### Main Menu Structure
```
1) Customer Support View
   1. Search for customer
   2. Edit customer info
   3. View customer complaints & orders
   4. Escalate complaint
   5. Create new complaint (Strategy Pattern)
   6. Send message to customer
   7. Show alerts

2) Inventory Subsystem View
   1. View inventory records
   2. Create purchase order
   3. View all purchase orders
   4. View all suppliers
   5. Fetch supplier data from ERP (Adapter Pattern)
   6. Manually deduct stock
   7. Increase stock
   8. Update product details
   9. Show low stock alerts (Observer Pattern)

3) Orders & Payments
   1. Display customer order history
   2. Show payment details
   3. Checkout flow (Mediator Pattern)
   4. Update order status based on payment status
   5. View order items (Iterator Pattern)

4) Reports (Bridge Pattern)
   1. Customer summary report
   2. Order report  
   3. Weekly delivery report
   (Select Console or CSV renderer)

5) Run Full Demo Suite
   - Executes all pattern demonstrations programmatically
```

---

## Sample Data Seeded

| Entity | Count | IDs |
|--------|-------|-----|
| Customers | 5 | CUST-001 to CUST-005 |
| Products | 6 | PROD-01 to PROD-06 |
| Inventory Records | 6 | REC-001 to REC-006 |
| Suppliers | 3 | SUP-01 to SUP-03 |
| Purchase Orders | 5 | PO-001 to PO-005 |
| Orders | 5 | ORD-001 to ORD-005 |
| Payments | 5 | TXN-001 to TXN-005 |
| Complaints | 4 | CMP-001 to CMP-004 |
| Alerts | 3 | ALT-001, ALT-002, ALT-SLA-001 |

---

## Usage Examples

### Test the Strategy Pattern (SLA)
1. Select `1) Customer Support View`
2. Select `5) Create new complaint`
3. Enter complaint ID: `CMP-TEST-01`
4. Select SLA Strategy: `2) VIP (4 hours)` or `3) Priority-based`
5. System calculates deadline using selected strategy

### Test the Adapter Pattern (ERP)
1. Select `2) Inventory Subsystem View`
2. Select `5) Fetch supplier data from ERP (Adapter)`
3. Enter Supplier ID: `SUP-ERP-99`
4. System calls `supplierController.importFromErp()` which uses `ErpSupplierAdapter`

### Test the Iterator Pattern
1. Select `3) Orders & Payments`
2. Select `5) View order items (Iterator pattern)`
3. Enter Order ID: `ORD-001`
4. System shows all items, then prompts for threshold
5. Enter threshold: `200`
6. System filters high-value items using `HVOrderitem` iterator

---

## Controller Architecture

All operations go through controllers (no direct model access):

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│  CustomerController  │────→│  CustomerRepository  │────→│  Customer (Model)│
├─────────────────┤     ├─────────────────┤     ├─────────────────┤
│  OrderController     │────→│  OrderRepository     │────→│  Order (Model)   │
├─────────────────┤     ├─────────────────┤     ├─────────────────┤
│  InventoryController │────→│  InventoryRecordRepo │────→│  InventoryRecord│
└─────────────────┘     └─────────────────┘     └─────────────────┘
```

---

## File Location
`e:/uni/year 3/semester 2/SW2/Project Delevirables/crm code/src/com/crm/Main.java`

---

## Summary

This Main.java file demonstrates professional application of 10+ design patterns in a realistic CRM context:

- **Creational**: Factory Method, Abstract Factory, Singleton, Flyweight
- **Structural**: Adapter, Bridge, Decorator
- **Behavioral**: Strategy, Mediator, Observer, Iterator

Each pattern is integrated into the console UI for hands-on testing and verification.
