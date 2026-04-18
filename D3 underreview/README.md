# Deliverable 2 — CRM System: Design Patterns Implementation

## Overview

| # | Pattern | Category | Where Used |
|---|---------|----------|------------|
| 1 | **Singleton** | Creational | `DatabaseConnectionManager` |
| 2 | **Factory Method** | Creational | `AlertController` and `ReportFactory` hierarchies |
| 3 | **Abstract Factory** | Creational | `CommunicationFactory`, `PaymentFactory` |
| 4 | **Decorator** | Structural | `SystemAlert` behavior extensions, `Delivery` status tracking |
| 5 | **Adapter** | Structural | `ErpSupplierAdapter` (ERP system), `StripePaymentAdapter` |
| 6 | **Bridge** | Structural | `Report`/`Renderer` separation, `ChannelNotification` |
| 7 | **Flyweight** | Creational | `ProductFactory` (managing shared Product objects) |

All patterns are integrated into a layered architecture using **Controllers**, **Repositories**, and **Models**.

---

## Project Structure

```
crm-code/
├── README.md                              ← Project documentation
├── pom.xml                                ← Maven configuration (Lombok & Build)
└── src/
    └── com/
        └── crm/
            ├── Main.java                  ← Entry point — runs the test suite
            │
            ├── common/                    ← Shared models & Utility
            │   ├── Employee.java
            │   ├── Money.java 
            │   ├── Address.java
            │   └── enums/                 ← Centralized system enums
            │
            ├── persistence/               ← SINGLETON PATTERN
            │   └── DatabaseConnectionManager.java
            │
            ├── alert/                     ← FACTORY METHOD & DECORATOR
            │   ├── factory/               ← Factory classes
            │   ├── controller/            ← Alert orchestration
            │   ├── model/                 ← Base Alerts & Decorators
            │   └── repository/            ← Alert persistence
            │
            ├── communication/             ← ABSTRACT FACTORY & BRIDGE
            │   ├── factory/               ← Message factories (SMS/Email)
            │   ├── channel/               ← Implementation bridges
            │   └── controller/            ← Communication logic
            │
            ├── customer/                  ← Customer Domain
            │   ├── controller/            ← Segmenation & Complaint logic
            │   ├── model/                 ← Customer & Complaint models
            │   └── repository/            ← Customer persistence
            │
            ├── inventory/                 ← FLYWEIGHT, ADAPTER, & CONTROLLER
            │   ├── controller/            ← Product & Inventory management
            │   ├── model/                 ← Products & PurchaseOrders
            │   ├── service/               ← ERP Adapter services
            │   └── repository/            ← Inventory persistence
            │
            ├── order/                     ← DECORATOR & CONTROLLER
            │   ├── model/                 ← Order & Delivery (Decorated)
            │   └── controller/            ← Order management logic
            │
            ├── payment/                   ← ABSTRACT FACTORY & ADAPTER
            │   ├── factory/               ← Payment method factories
            │   ├── model/                 ← Processors & Stripe Adapter
            │   └── controller/            ← Payment transaction logic
            │
            └── reporting/                 ← FACTORY METHOD & BRIDGE
                ├── factory/               ← Report generation factories
                ├── model/                 ← Report types & Renderers
                └── controller/            ← Report orchestration logic
```

---

## Design Patterns Explained

### 1. Creational Patterns
*   **Singleton (`DatabaseConnectionManager`)**: Ensures a single database connection point.
*   **Factory Method (`AlertFactory`)**: Subclasses decide which specific alert (`LowStock`, `SlaBreach`) to instantiate.
*   **Abstract Factory (`CommunicationFactory`)**: Creates families of related objects (Channels, Notifications) for SMS, Email, or Chat.
*   **Flyweight (`ProductFactory`)**: Reuses existing `Product` objects to reduce memory overhead for shared inventory items.

### 2. Structural Patterns
*   **Decorator (`LoggingAlertDecorator`, `RetryAlertDecorator`, `EscalatingAlertDecorator`, `AlertingDeliveryDecorator`)**: Dynamically adds behavior like logging, notifications, and automated retries to existing objects without creating a complex hierarchy of subclasses.

    #### **Case Study 1: Cross-Cutting Behavior for SystemAlert**
    
    **The Problem (Before Decorator)**
    The CRM needed to add "cross-cutting" behaviors like **Logging** and **Retrying** to various alert types (`LowStockAlert`, `SlaBreachAlert`). Using standard inheritance would lead to a "Class Explosion" of redundant subclasses.
    
    **The Solution (After Decorator)**
    *   **Delegation**: Each decorator handles its specific logic then delegates the notification to the wrapped alert.
    *   **Flexible Composition**: We can mix and match behaviors at runtime (e.g., `new LoggingAlertDecorator(new RetryAlertDecorator(alert))`).
    *   **Clean Core**: Core alerts remain simple and focused only on their specific data.

    #### **Case Study 2: Delivery Status Tracking**
    
    **The Problem (Before Decorator)**
    A standard `Delivery` object only tracks the state of an order. If we wanted to automatically trigger a `DeliveryDelayAlert` when a shipment is delayed, we would have been forced to modify the core `Delivery` class.
    
    **The Solution (After Decorator)**
    *   **Separation of Concerns**: The `AlertingDeliveryDecorator` adds alerting logic while the original `Delivery` class remains responsible only for state.
    *   **Dynamic Behavior**: We can choose to wrap only high-priority deliveries with alerting features at runtime.
    *   **Extensibility**: New behaviors (like logging delivery history) can be added as new decorators without touching existing code.

*   **Adapter (`ErpSupplierAdapter`, `StripePaymentAdapter`)**: Allows the CRM to communicate with external systems (ERP and Payment Gateways) by translating incompatible interfaces into a standardized internal format.

    #### **Case Study 1: External Payment Gateway (Stripe Adapter)**
    
    **The Problem (Before Adapter)**
    Stripe's API expects amounts in **cents** and currency as separate **Strings**. The CRM uses a unified `Money` object and a `processPayment()` method.
    
    **The Solution (After Adapter)**
    *   **Interface Mapping**: The adapter implements our `PaymentProcessor` interface while wrapping the `StripeClient`.
    *   **Data Transformation**: The adapter automatically handles the conversion from `Money` objects to cents and currency codes.
    *   **Decoupling**: The CRM logic remains identical whether we use Stripe or an internal manual processor.

    #### **Case Study 2: ERP System Integration (Supplier Adapter)**
    
    **The Problem (Before Adapter)**
    Our external ERP returns data as raw `String[]` arrays and uses different nomenclature. Directly using this in the CRM would spread ERP-specific logic throughout the codebase.
    
    **The Solution (After Adapter)**
    *   **Translation**: The `ErpSupplierAdapter` maps raw ERP arrays into domain-driven `Supplier` and `PurchaseOrder` objects.
    *   **API Harmonization**: The adapter maps ERP methods like `getErpSupplierData()` to the CRM's `fetchSupplier()` interface.
    *   **Robustness**: If the ERP schema changes, we only need to update the adapter, leaving the CRM's business logic intact.


*   **Bridge (`Report`/`Renderer`, `Notification`/`Channel`)**: Decouples abstraction from implementation, allowing two independent hierarchies to evolve separately without a "Combinatorial Explosion" of classes.

    #### **Case Study 1: Report Generation and Formatting**
    
    **The Problem (Before Bridge)**
    The reporting system was tightly coupled with its output format:
    *   To create an `OrderReport` in both Console and CSV formats, we would have needed separate subclasses: `ConsoleOrderReport` and `CsvOrderReport`.
    *   Adding a new report (e.g., `InventoryReport`) or a new format (e.g., `PdfRenderer`) would cause the classes to grow exponentially (**M reports × N formats**).
    
    **The Solution (After Bridge)**
    We separated the **what** (Report data) from the **how** (Renderer format):
    *   **Delegation**: The `Report` class no longer handles formatting and instead delegates to its assigned `ReportRenderer`.
    *   **Flexible Composition**: We can now mix any report type with any renderer at runtime.
    *   **Reduced Complexity**: We eliminated format-specific subclasses, keeping the codebase clean and manageable.

    #### **Case Study 2: Decoupling Notification from CommunicationChannel**
    
    **The Problem (Before Bridge)**
    The CRM's notification system was highly rigid:
    *   `ChatNotification`, `EmailNotification`, and `SmsNotification` had their "send" logic hard-coded inside them.
    *   This meant that the **message content** was tightly coupled with the **delivery channel**.
    *   **Combinatorial Explosion**: Adding a new "MarketingNotification" that could be sent via both Email and SMS would require creating multiple redundant subclasses.
    
    **The Solution (After Bridge)**
    We decoupled the `Notification` abstraction from its `CommunicationChannel` implementation:
    *   **Delegation**: The `send()` method no longer contains delivery logic and instead delegates to its assigned channel.
    *   **Flexible Composition**: We can now mix any notification type with any channel at runtime.
    *   **Factory Wiring**: Factories inject the matching channel into the notification at the moment of creation.



---

## Expected Output Test Suites
The system executes a unified test suite in `Main.java` demonstrating:
1.  **Singleton Check**: Verifies instance consistency.
2.  **Alert Demo**: Shows Factory creation + nested Decorators (Retry, Log, Escalate).
3.  **Reporting Demo**: Shows Bridge switching between Console and CSV renderers.
4.  **Customer Demo**: Demonstrates Segmentation and Abstract Factory communication.
5.  **Inventory Demo**: Demonstrates Flyweight reuse and automated Inventory Alerts.
6.  **Payment Demo**: Shows Abstract Factory creation and the Stripe Adapter.
7.  **ERP Demo**: Demonstrates the Supplier Adapter importing data to the CRM.
8.  **Delivery Demo**: Shows nested Decorators tracking delivery status.
9.  **Communication Bridge**: Verifies final Bridge implementation for SMS/Email channels.
