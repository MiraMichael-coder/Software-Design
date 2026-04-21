package com.crm;

import com.crm.alert.controller.*;
import com.crm.alert.model.*;
import com.crm.alert.repository.*;
import com.crm.common.*;
import com.crm.common.enums.*;
import com.crm.communication.*;
import com.crm.communication.channel.Email.EmailChannel;
import com.crm.communication.channel.SMS.SMSChannel;

import com.crm.communication.controller.*;
import com.crm.communication.providers.*;
import com.crm.customer.SlaCalculator.*;
import com.crm.customer.controller.*;
import com.crm.customer.model.*;
import com.crm.customer.policies.ActivityBasedSegmentation;
import com.crm.customer.policies.GeographicSegmentation;
import com.crm.customer.policies.SegmentationPolicy;
import com.crm.customer.policies.SpendingBasedSegmentation;
import com.crm.customer.repository.*;

import com.crm.inventory.controller.*;

import com.crm.inventory.event.*;
import com.crm.inventory.model.*;
import com.crm.inventory.model.Supplier;

import com.crm.inventory.repository.*;
import com.crm.inventory.service.*;
import com.crm.order.controller.*;
import com.crm.order.model.*;
import com.crm.order.repository.*;

import com.crm.payment.controller.*;
import com.crm.payment.providers.*;
import com.crm.payment.model.*;
import com.crm.payment.repository.*;

import com.crm.persistence.*;

import com.crm.reporting.controller.*;
import com.crm.reporting.generators.*;
import com.crm.reporting.model.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class Main {
        private static Customer sharedCustomer;
        private static Product sharedProduct;
        private static Product sharedProduct2;
        private static Product sharedProduct3;
        private static Product sharedProduct4;
        private static Employee sharedEmployee;
        private static String sharedSupplierId = "SUP-01";

        private static final Scanner scanner = new Scanner(System.in);
        // All controllers and repos
        private static CustomerRepository customerRepository;
        private static ComplaintRepository complaintRepository;
        private static OrderRepository orderRepository;
        private static PaymentRepository paymentRepository;
        private static RefundRepository refundRepository;
        private static ProductRepository productRepository;
        private static InventoryRecordRepository inventoryRecordRepository;
        private static SupplierRepository supplierRepository;
        private static PurchaseOrderRepository purchaseOrderRepository;
        private static AlertRepository alertRepository;

        private static CustomerController customerController;
        private static ComplaintController complaintController;
        private static OrderController orderController;
        private static PaymentController paymentController;
        private static ProductController productController;
        private static InventoryController inventoryController;
        private static SupplierController supplierController;
        private static PurchaseOrderController purchaseOrderController;
        private static ReportController reportController;
        private static CommunicationController communicationController;
        private static InventoryAlertController inventoryAlertController;
        private static SlaBreachAlertController slaBreachAlertController;

        private static OrderFulfillment orderFulfillmentMediator;
        private static ComplaintEscalationHandler complaintEscalationMediator;

        private static final Set<String> knownComplaintIds = new HashSet<>();
        private static final Set<String> knownAlertIds = new HashSet<>();

        public static void main(String[] args) {
                setupSharedObjects();
                bootstrapControllers();
                seedSampleData();
                runInteractiveConsole();
        }

        // all initizallization for controllers and repos
        private static void bootstrapControllers() {
                customerRepository = new CustomerRepository();
                complaintRepository = new ComplaintRepository();
                orderRepository = new OrderRepository();
                paymentRepository = new PaymentRepository();
                refundRepository = new RefundRepository();
                productRepository = new ProductRepository();
                inventoryRecordRepository = new InventoryRecordRepository();
                supplierRepository = new SupplierRepository();
                purchaseOrderRepository = new PurchaseOrderRepository();
                alertRepository = new AlertRepository();

                customerController = new CustomerController(customerRepository);
                complaintController = new ComplaintController(complaintRepository);
                orderController = new OrderController(orderRepository);
                paymentController = new PaymentController(paymentRepository, refundRepository);
                productController = new ProductController(productRepository);
                inventoryController = new InventoryController(inventoryRecordRepository);
                supplierController = new SupplierController(supplierRepository, new ErpSupplierConnector(),
                                purchaseOrderRepository);
                purchaseOrderController = new PurchaseOrderController(purchaseOrderRepository, productRepository);
                reportController = new ReportController();
                communicationController = new CommunicationController();
                inventoryAlertController = new InventoryAlertController(alertRepository);
                slaBreachAlertController = new SlaBreachAlertController(alertRepository);

                complaintEscalationMediator = new ComplaintEscalationHandlerService(
                                complaintController,
                                slaBreachAlertController,
                                communicationController);
                complaintController.setMediator(complaintEscalationMediator);

                orderFulfillmentMediator = new OrderFulfillment(
                                orderController,
                                paymentController,
                                productController,
                                communicationController);

                InventoryEventManager.getInstance().register(inventoryAlertController);
                InventoryEventManager.getInstance().register(inventoryController);
                InventoryEventManager.getInstance().register(supplierController);
        }

        // static data for testing
        private static void seedSampleData() {
                System.out.println("[Main] Seeding sample data across all repositories...");

                // ========== CUSTOMERS ==========
                Customer customer1 = new Customer("CUST-001",
                                "Ahmed Samy", "ahmed.samy@example.com", "01012345678",
                                2800, LocalDateTime.now().minusDays(21), "Cairo", CustomerStatus.Active);

                Customer customer2 = new Customer("CUST-002", "Manar Yousry", "manar.yous@test.com", "01098765432",
                                1500, LocalDateTime.now().minusDays(80), "Alexandria", CustomerStatus.Active);

                Customer customer3 = new Customer("CUST-003", "Hossam Hassan", "hossam.hassan@test.com", "01123456789",
                                3200, LocalDateTime.now(), "Cairo", CustomerStatus.Active);

                Customer customer4 = new Customer("CUST-004", "Mohamed Ali", "mohamed.ali@test.com", "01234567890",
                                900, LocalDateTime.now(), "Cairo", CustomerStatus.Active);

                Customer customer5 = new Customer("CUST-005", "Carol Magdy", "carol.magdy@test.com", "01523456764",
                                2100, LocalDateTime.now().minusDays(40), "Cairo", CustomerStatus.Active);

                customer1.setRegion("Cairo");
                customer2.setRegion("Alexandria");
                customer3.setRegion("Alexandria");
                customer4.setRegion("Cairo");
                customer5.setRegion("Cairo");

                customerController.createCustomer(customer1.getCustomerId(), customer1);
                customerController.createCustomer(customer2.getCustomerId(), customer2);
                customerController.createCustomer(customer3.getCustomerId(), customer3);
                customerController.createCustomer(customer4.getCustomerId(), customer4);
                customerController.createCustomer(customer5.getCustomerId(), customer5);

                sharedCustomer = customer1;
                System.out.println("  - Created 5 customers");

                // ========== PRODUCTS ==========
                Product product1 = ProductCreator.getOrCreate("PROD-01", "Wireless Mouse",
                                "Ergonomic wireless mouse with 2.4GHz connectivity", new Money(149.99, "EGP"), 50,
                                "SUP-01");
                Product product2 = ProductCreator.getOrCreate("PROD-02", "Mechanical Keyboard",
                                "RGB backlit mechanical keyboard with blue switches", new Money(499.99, "EGP"), 30,
                                "SUP-01");
                Product product3 = ProductCreator.getOrCreate("PROD-03", "USB-C Hub",
                                "7-in-1 USB-C hub with HDMI, USB 3.0, and card reader", new Money(299.99, "EGP"), 25,
                                "SUP-02");
                Product product4 = ProductCreator.getOrCreate("PROD-04", "Webcam 4K",
                                "Ultra HD 4K webcam with auto-focus and noise cancellation", new Money(799.99, "EGP"),
                                15, "SUP-02");
                Product product5 = ProductCreator.getOrCreate("PROD-05", "Monitor Stand",
                                "Adjustable aluminum monitor stand with cable management", new Money(199.99, "EGP"), 40,
                                "SUP-03");
                Product product6 = ProductCreator.getOrCreate("PROD-06", "Desk Lamp LED",
                                "Smart LED desk lamp with wireless charging base", new Money(249.99, "EGP"), 20,
                                "SUP-03");

                productController.createProduct(product1);
                productController.createProduct(product2);
                productController.createProduct(product3);
                productController.createProduct(product4);
                productController.createProduct(product5);
                productController.createProduct(product6);

                sharedProduct = product1;
                sharedProduct2 = product2;
                sharedProduct3 = product3;
                sharedProduct4 = product4;

                System.out.println("  - Created 6 products");

                // ========== INVENTORY RECORDS ==========
                InventoryRecord record1 = new InventoryRecord("REC-001", product1.getProductId(),
                                "Main Warehouse", product1.getStockQuantity(), 10);
                InventoryRecord record2 = new InventoryRecord("REC-002", product2.getProductId(),
                                "Main Warehouse", product2.getStockQuantity(), 5);
                InventoryRecord record3 = new InventoryRecord("REC-003", product3.getProductId(),
                                "Secondary Warehouse", product3.getStockQuantity(), 8);
                InventoryRecord record4 = new InventoryRecord("REC-004", product4.getProductId(),
                                "Main Warehouse", product4.getStockQuantity(), 3);
                InventoryRecord record5 = new InventoryRecord("REC-005", product5.getProductId(),
                                "Secondary Warehouse", product5.getStockQuantity(), 12);
                InventoryRecord record6 = new InventoryRecord("REC-006", product6.getProductId(),
                                "Main Warehouse", product6.getStockQuantity(), 6);

                inventoryController.createInventoryRecord(record1);
                inventoryController.createInventoryRecord(record2);
                inventoryController.createInventoryRecord(record3);
                inventoryController.createInventoryRecord(record4);
                inventoryController.createInventoryRecord(record5);
                inventoryController.createInventoryRecord(record6);
                System.out.println("  - Created 6 inventory records");

                // ========== SUPPLIERS ==========
                Address ad1 = new Address("Nozha", "Cairo", "Madinet Nasr ");
                Address ad2 = new Address("Zamalek", "Cairo", "Cairo");
                Address ad3 = new Address("Maadi", "Cairo", "Cairo");

                Supplier supplier1 = new Supplier("SUP-01", "TechSource Electronics", "Ahmed Hassan",
                                "contact@techsource.com", "+20 2 12345678", ad1);
                Supplier supplier2 = new Supplier("SUP-02", "Global Computer Parts", "Maria Gonzalez",
                                "sales@globalcp.com", "+20 2 87654321", ad2);
                Supplier supplier3 = new Supplier("SUP-03", "Office Furniture Pro", "David Chen", "info@officepro.com",
                                "+20 2 11223344", ad3);

                supplierController.createSupplier(supplier1);
                supplierController.createSupplier(supplier2);
                supplierController.createSupplier(supplier3);

                System.out.println("  - Created 3 suppliers");

                // ========== PURCHASE ORDERS ==========
                PurchaseOrder po1 = new PurchaseOrder("PO-001", "SUP-01", "PROD-01", 100, new Money(8000.0, "EGP"));
                PurchaseOrder po2 = new PurchaseOrder("PO-002", "SUP-01", "PROD-02", 50, new Money(20000.0, "EGP"));
                PurchaseOrder po3 = new PurchaseOrder("PO-003", "SUP-02", "PROD-03", 40, new Money(8000.0, "EGP"));
                PurchaseOrder po4 = new PurchaseOrder("PO-004", "SUP-02", "PROD-04", 25, new Money(15000.0, "EGP"));
                PurchaseOrder po5 = new PurchaseOrder("PO-005", "SUP-03", "PROD-05", 60, new Money(7200.0, "EGP"));

                purchaseOrderController.createPurchaseOrder(po1);
                purchaseOrderController.createPurchaseOrder(po2);
                purchaseOrderController.createPurchaseOrder(po3);
                purchaseOrderController.createPurchaseOrder(po4);
                purchaseOrderController.createPurchaseOrder(po5);
                System.out.println("  - Created 5 purchase orders");

                // ========== ORDERS ==========
                Order order1 = new Order("ORD-001", customer1.getCustomerId());
                order1.addItem(new OrderItem("ITM-001", product1, 2, product1.getUnitPrice()));
                order1.addItem(new OrderItem("ITM-002", product2, 1, product2.getUnitPrice()));
                orderController.createOrder(order1);

                Order order2 = new Order("ORD-002", customer2.getCustomerId());
                order2.addItem(new OrderItem("ITM-003", product3, 1, product3.getUnitPrice()));
                orderController.createOrder(order2);

                Order order3 = new Order("ORD-003", customer3.getCustomerId());
                order3.addItem(new OrderItem("ITM-004", product4, 2, product4.getUnitPrice()));
                order3.addItem(new OrderItem("ITM-005", product5, 1, product5.getUnitPrice()));
                orderController.createOrder(order3);

                Order order4 = new Order("ORD-004", customer1.getCustomerId());
                order4.addItem(new OrderItem("ITM-006", product6, 3, product6.getUnitPrice()));
                orderController.createOrder(order4);

                Order order5 = new Order("ORD-005", customer4.getCustomerId());
                order5.addItem(new OrderItem("ITM-007", product1, 1, product1.getUnitPrice()));
                order5.addItem(new OrderItem("ITM-008", product3, 2, product3.getUnitPrice()));
                orderController.createOrder(order5);
                System.out.println("  - Created 5 orders with items");

                // ========== PAYMENTS ==========
                PaymentTransaction payment1 = new PaymentTransaction("TXN-001", "ORD-001", new Money(799.97, "EGP"),
                                PaymentMethodType.Card);
                payment1.markCompleted();
                paymentRepository.save(payment1.getPaymentId(), payment1);
                orderController.attachPayment("ORD-001", payment1);
                orderController.updateStatus("ORD-001", com.crm.common.enums.OrderStatus.Processing);

                PaymentTransaction payment2 = new PaymentTransaction("TXN-002", "ORD-002", new Money(299.99, "EGP"),
                                PaymentMethodType.CashOnDelivery);
                payment2.markCompleted();
                paymentRepository.save(payment2.getPaymentId(), payment2);
                orderController.attachPayment("ORD-002", payment2);
                orderController.updateStatus("ORD-002", com.crm.common.enums.OrderStatus.Delivered);

                PaymentTransaction payment3 = new PaymentTransaction("TXN-003", "ORD-003", new Money(1799.97, "EGP"),
                                PaymentMethodType.Card);
                paymentRepository.save(payment3.getPaymentId(), payment3);
                orderController.attachPayment("ORD-003", payment3);

                PaymentTransaction payment4 = new PaymentTransaction("TXN-004", "ORD-004", new Money(749.97, "EGP"),
                                PaymentMethodType.Card);
                payment4.markFailed();
                paymentRepository.save(payment4.getPaymentId(), payment4);
                orderController.attachPayment("ORD-004", payment4);
                orderController.updateStatus("ORD-004", com.crm.common.enums.OrderStatus.Cancelled);

                PaymentTransaction payment5 = new PaymentTransaction("TXN-005", "ORD-005", new Money(749.97, "EGP"),
                                PaymentMethodType.CashOnDelivery);
                payment5.markCompleted();
                paymentRepository.save(payment5.getPaymentId(), payment5);
                orderController.attachPayment("ORD-005", payment5);
                orderController.updateStatus("ORD-005", com.crm.common.enums.OrderStatus.Dispatched);

                System.out.println("  - Created 5 payment transactions");

                // ========== COMPLAINTS ==========
                Complaint complaint1 = new Complaint("CMP-001", "high", new StandardSlaCalculator());
                Complaint complaint2 = new Complaint("CMP-002", "critical", new PriorityBasedSlaCalculator(24));
                complaint2.setSlaDeadline(LocalDateTime.now().minusDays(5)); // INTENTIONALLY PRE-BREACHED
                Complaint complaint3 = new Complaint("CMP-003", "medium", new StandardSlaCalculator());
                Complaint complaint4 = new Complaint("CMP-004", "low", new VipSlaCalculator());

                complaintController.createComplaint(complaint1);
                complaintController.createComplaint(complaint2);
                complaintController.createComplaint(complaint3);
                complaintController.createComplaint(complaint4);

                knownComplaintIds.add(complaint1.getComplaintId());
                knownComplaintIds.add(complaint2.getComplaintId());
                knownComplaintIds.add(complaint3.getComplaintId());
                knownComplaintIds.add(complaint4.getComplaintId());

                System.out.println("  - Created 4 complaints");

                // ========== ALERTS ==========
                SystemAlert lowStockAlert1 = inventoryAlertController.processAlert("ALT-001", "PROD-04");
                SystemAlert lowStockAlert2 = inventoryAlertController.processAlert("ALT-002", "PROD-06");
                SystemAlert slaAlert = slaBreachAlertController.processAlert("ALT-SLA-001", "CMP-002");

                knownAlertIds.add(lowStockAlert1.getAlertId());
                knownAlertIds.add(lowStockAlert2.getAlertId());
                knownAlertIds.add(slaAlert.getAlertId());

                System.out.println("  - Created 3 alerts");

                System.out.println("====================\n");
                System.out.println("Sample data seeding complete!\n");
        }

        // ==================== Main menu ====================
        private static void runInteractiveConsole() {
                boolean running = true;
                while (running) {
                        System.out.println("\n==================== CRM MAIN MENU ====================");
                        System.out.println("1) Customer Support View");
                        System.out.println("2) Inventory Subsystem View");
                        System.out.println("3) Orders & Payments");
                        System.out.println("4) Reports");
                        System.out.println("5) Run Full Demo"); // old main
                        System.out.println("0) Exit");

                        int choice = readInt("Select an option: ");
                        switch (choice) {
                                case 1 -> customerSupportMenu();
                                case 2 -> inventoryMenu();
                                case 3 -> ordersPaymentsMenu();
                                case 4 -> reportsMenu();
                                case 5 -> runDemoSuite();
                                case 0 -> running = false;
                                default -> System.out.println("Invalid option.");
                        }
                }
                System.out.println("Shutting down CRM console.");
        }

        // shows all alerts id saved
        // ==================== Common functions ====================
        private static void showKnownAlerts() {
                if (knownAlertIds.isEmpty()) {
                        System.out.println("No alerts recorded in this session.");
                        return;
                }
                System.out.println("Alerts:");
                for (String alertId : knownAlertIds) {
                        SystemAlert a = alertRepository.findById(alertId);
                        if (a == null) {
                                continue;
                        }
                        System.out.println("- " + a.getAlertId() + " | Severity: " + a.getSeverity() + " | Resolved: "
                                        + a.isResolved() + " | CreatedAt: " + a.getFormattedCreatedAt());
                }
        }

        // ==================== Customer ====================
        private static void customerSupportMenu() {

                boolean running = true;
                while (running) {
                        System.out.println("\n-------------------- Customer Support --------------------");
                        System.out.println("1) Search for customer");
                        System.out.println("2) Edit customer info");
                        System.out.println("3) View customer complaints & orders");
                        System.out.println("4) Escalate complaint");
                        System.out.println("5) Create new complaint");
                        System.out.println("6) Send message to customer");
                        System.out.println("7) Show alerts");
                        System.out.println("8) Test customer segmentation");
                        System.out.println("9) Trigger Priority SLA Breach Alarm");
                        System.out.println("0) Back");

                        int choice = readInt("Select an option: ");
                        switch (choice) {
                                case 1 -> supportSearchCustomer();
                                case 2 -> supportEditCustomer();
                                case 3 -> supportViewComplaintsOrders();
                                case 4 -> supportEscalateComplaint();
                                case 5 -> supportCreateComplaint();
                                case 6 -> supportSendMessage();
                                case 7 -> showKnownAlerts();
                                case 8 -> supportTestSegmentation();
                                case 9 -> supportCreateDecoratedSlaAlert();
                                case 0 -> running = false;
                                default -> System.out.println("Invalid option.");
                        }
                }
        }

        private static void supportSearchCustomer() {
                String customerId = readString("Enter Customer ID: ");
                Customer customer = customerController.retrieveCustomer(customerId);
                if (customer == null) {
                        System.out.println("Customer not found.");
                        return;
                }
                System.out.println("Customer found: " + customer.getCustomerId() + " | " + customer.getName() + " | "
                                + customer.getEmail() + " | " + customer.getPhone());
        }

        private static void supportEditCustomer() {
                String customerId = readString("Enter Customer ID to edit: ");
                Customer customer = customerController.retrieveCustomer(customerId);
                if (customer == null) {
                        System.out.println("Customer not found.");
                        return;
                }

                System.out.println("Editing customer: " + customer.getName());
                String name = readStringAllowEmpty("New name (leave empty to keep): ");
                String email = readStringAllowEmpty("New email (leave empty to keep): ");
                String phone = readStringAllowEmpty("New phone (leave empty to keep): ");

                if (!name.isEmpty()) {
                        customer.setName(name);
                }
                if (!email.isEmpty()) {
                        customer.setEmail(email);
                }
                if (!phone.isEmpty()) {
                        customer.setPhone(phone);
                }
                customerController.updateCustomer(customerId, customer);
                System.out.println("Customer updated successfully.");
        }

        private static void supportViewComplaintsOrders() {
                String customerId = readString("Enter Customer ID: ");
                Customer customer = customerController.retrieveCustomer(customerId);
                if (customer == null) {
                        System.out.println("Customer not found.");
                        return;
                }

                System.out.println("\nCustomer: " + customer.getCustomerId() + " | " + customer.getName());
                System.out.println("\nComplaints:");
                if (knownComplaintIds.isEmpty()) {
                        System.out.println("No complaints recorded in this session.");
                } else {
                        for (String complaintId : knownComplaintIds) {
                                Complaint c = complaintController.getComplaint(complaintId);
                                if (c == null) {
                                        continue;
                                }
                                System.out.println("- " + c.getComplaintId() + " | Priority: " + c.getPriority()
                                                + " | Status: " + c.getStatus() + " | SLA: " + c.getSlaDeadline());
                        }
                }

                System.out.println("\nOrders:");
                Collection<Order> orders = orderRepository.findAll();
                boolean found = false;
                for (Order o : orders) {
                        if (o != null && customerId.equals(o.getCustomerId())) {
                                found = true;
                                System.out.println("- " + o.getOrderId() + " | Status: " + o.getStatus() + " | Total: "
                                                + o.getTotalAmount().getAmount() + " "
                                                + o.getTotalAmount().getCurrency()
                                                + " | PaymentId: " + o.getPaymentId());
                        }
                }
                if (!found) {
                        System.out.println("No orders found for this customer.");
                }
        }

        private static void supportEscalateComplaint() {
                System.out.println("\n[Auto-Check] Scanning for breached SLA complaints....");
                for (String cid : knownComplaintIds) {
                        Complaint c = complaintController.getComplaint(cid);
                        if (c != null && c.isSlaBreached()
                                        && c.getStatus() != ComplaintStatus.Resolved) {
                                Customer cust = customerController.retrieveCustomer(sharedCustomer.getCustomerId());
                                complaintController.detectSlaBreach(c, cust, sharedEmployee,
                                                new SmsChannelProvider());

                                String alertId = "ALT-SLA-" + cid + "-" + System.currentTimeMillis();
                                SystemAlert alert = slaBreachAlertController.processAlert(alertId, cid);
                                knownAlertIds.add(alert.getAlertId());
                        }
                }
                System.out.println("\nChecking complaints manually....");
                String complaintId = readString("Enter Complaint ID to escalate: ");
                Complaint complaint = complaintController.getComplaint(complaintId);
                if (complaint == null) {
                        System.out.println("Complaint not found.");
                        return;
                }
                Customer customer = customerController.retrieveCustomer(sharedCustomer.getCustomerId());
                if (complaintController.isSlaBreached(complaint)) {

                        CommunicationChannelProvider commProvider = selectCommunicationChannelProvider();
                        // checking if comaplint is breached and not resolved
                        complaintController.detectSlaBreach(complaint, customer, sharedEmployee, commProvider);

                        String alertId = "ALT-SLA-" + complaintId + "-" + System.currentTimeMillis();
                        SystemAlert alert = slaBreachAlertController.processAlert(alertId, complaintId);
                        knownAlertIds.add(alert.getAlertId());
                        System.out.println("Escalation flow completed.");

                } else if (complaint.getStatus() == ComplaintStatus.Resolved) {
                        System.out.println("Complaint is already resolved.");
                } else {
                        System.out.println("Complaint ID: " + complaintId + "For customer" + customer.getName()
                                        + " Status is " + complaint.getStatus());
                }
        }

        private static void supportCreateComplaint() {
                String complaintId = readString("Complaint ID: ");
                String priority = readString("Priority (critical/high/medium/low): ");
                SlaCalculator calculator = selectSlaStrategy();
                Complaint complaint = new Complaint(complaintId, priority, calculator);
                complaintController.createComplaint(complaint);
                knownComplaintIds.add(complaintId);
                System.out.println("Complaint created: " + complaint.getComplaintId() + " | SLA: "
                                + complaint.getSlaDeadline());
        }

        private static void supportSendMessage() {
                String customerId = readString("Customer ID: ");
                Customer customer = customerController.retrieveCustomer(customerId);
                if (customer == null) {
                        System.out.println("Customer not found.");
                        return;
                }
                CommunicationChannelProvider factory = selectCommunicationChannelProvider();
                String messageText = readString("Message text: ");
                communicationController.sendMessage(factory, customer.getEmail(), messageText);
                System.out.println("Message dispatched via " + factory.getClass().getSimpleName() + ".");
        }

        private static void supportTestSegmentation() {
                System.out.println("\n--- Testing Customer Segmentation Strategies ---");
                System.out.println("Select segmentation strategy:");
                System.out.println("1) Geographic (by region)");
                System.out.println("2) Spending-based (by total spending threshold)");
                System.out.println("3) Activity-based (by days since last activity)");

                int choice = readInt("Choose: ");
                SegmentationPolicy policy;
                String policyName;

                switch (choice) {
                        case 1 -> {
                                String region = readString("Enter target region (Cairo, Alexandria ...): ");
                                policy = new GeographicSegmentation(region);
                                policyName = "Geographic (Region: " + region + ")";
                        }
                        case 2 -> {
                                double threshold = readDouble("Enter minimum spending threshold: ");
                                policy = new SpendingBasedSegmentation(threshold);
                                policyName = "Spending-based (Min: " + threshold + ")";
                        }
                        case 3 -> {
                                int days = readInt("Enter max inactivity days: ");
                                policy = new ActivityBasedSegmentation(days);
                                policyName = "Activity-based (Max days: " + days + ")";
                        }
                        default -> {
                                System.out.println("Invalid option. Using geographic segmentation with 'Cairo'.");
                                policy = new GeographicSegmentation("Cairo");
                                policyName = "Geographic (Region: Cairo)";
                        }
                }

                System.out.println("\n--- Evaluating Customers Against: " + policyName + " ---");
                Collection<Customer> customers = customerRepository.findAll();
                int matchCount = 0;
                for (Customer c : customers) {
                        boolean belongs = policy.evaluate(c);
                        System.out.println("- " + c.getCustomerId() + " | " + c.getName() + " | Region: "
                                        + c.getRegion() + " | Spending: " + c.getTotalSpending()
                                        + " | Last Activity: " + c.getLastActivityDate()
                                        + " => " + (belongs ? "IN SEGMENT" : "NOT IN SEGMENT"));
                        if (belongs) {
                                matchCount++;
                        }
                }
                System.out.println("Total customers in segment: " + matchCount + "/" + customers.size());
        }

        private static void supportCreateDecoratedSlaAlert() {
                String complaintId = readString("Enter Complaint ID for SLA alert: ");
                String alertId = "ALT-SLA-DEC-" + complaintId + "-" + System.currentTimeMillis();

                System.out.println("\n--- Triggering Priority SLA Alarm (Logging + Escalation + Retry) ---");
                SystemAlert decoratedSla = new RetryingAlert(
                                new EscalatingAlert(
                                                new LoggedAlert(
                                                                slaBreachAlertController.processAlert(alertId,
                                                                                complaintId))),
                                3);
                knownAlertIds.add(decoratedSla.getAlertId());
                System.out.println("Triggering decorated alert notification...");
                decoratedSla.notifyTarget();
                System.out.println("Priority SLA alarm triggered successfully.");
        }

        // ==================== Inventory ====================
        private static void inventoryMenu() {
                boolean running = true;
                while (running) {
                        System.out.println("\n-------------------- Inventory Subsystem --------------------");
                        System.out.println("1) View inventory records");
                        System.out.println("2) Create purchase order");
                        System.out.println("3) View all purchase orders");
                        System.out.println("4) View all suppliers");
                        System.out.println("5) Check External ERP for Supplier Updates");
                        System.out.println("6) Manually deduct stock");
                        System.out.println("7) Increase stock");
                        System.out.println("8) Update product details");
                        System.out.println("9) Show low stock alerts");
                        System.out.println("10) Trigger Priority Low Stock Alarm");
                        System.out.println("0) Back");

                        int choice = readInt("Select an option: ");
                        switch (choice) {
                                case 1 -> inventoryViewRecords();
                                case 2 -> inventoryCreatePurchaseOrder();
                                case 3 -> inventoryViewAllPurchaseOrders();
                                case 4 -> inventoryViewAllSuppliers();
                                case 5 -> inventoryFetchSupplierFromErp();
                                case 6 -> inventoryDeductStock();
                                case 7 -> inventoryIncreaseStock();
                                case 8 -> inventoryUpdateProduct();
                                case 9 -> showKnownAlerts();
                                case 10 -> inventoryCreateDecoratedLowStockAlert();
                                case 0 -> running = false;
                                default -> System.out.println("Invalid option.");
                        }
                }
        }

        private static void inventoryViewRecords() {
                Collection<InventoryRecord> records = inventoryRecordRepository.findAll();
                if (records.isEmpty()) {
                        System.out.println("No inventory records found.");
                        return;
                }
                for (InventoryRecord r : records) {
                        System.out.println("- " + r.getRecordId() + " | Product: " + r.getProductId() + " | Warehouse: "
                                        + r.getWarehouseName() + " | Available: " + r.getAvailableQuantity()
                                        + " | Reorder level : " + r.getReorderLevel());
                }
        }

        private static void inventoryCreatePurchaseOrder() {
                String poId = readString("Purchase Order ID: ");
                String supplierId = readString("Supplier ID: ");
                String productId = readString("Product ID: ");
                int qty = readInt("Quantity: ");
                double amount = readDouble("Unit price amount: ");
                String currency = readString("Currency (e.g. EGP): ");

                PurchaseOrder po = new PurchaseOrder(poId, supplierId, productId, qty, new Money(amount, currency));
                purchaseOrderController.createPurchaseOrder(po);
                System.out.println("Purchase order created in CRM.");

                int push = readInt("Push PO to ERP now? (1=yes, 0=no): ");
                if (push == 1) {
                        supplierController.exportPoToErp(poId);
                }
        }

        private static void inventoryViewAllPurchaseOrders() {
                Collection<PurchaseOrder> pos = purchaseOrderRepository.findAll();
                if (pos.isEmpty()) {
                        System.out.println("No purchase orders found.");
                        return;
                }
                System.out.println("\n--- All Purchase Orders ---");
                for (PurchaseOrder po : pos) {
                        System.out.println("- PO ID: " + po.getPurchaseOrderId() + " | Supplier: " + po.getSupplierId()
                                        + " | Product: " + po.getProductId() + " | Qty: " + po.getQuantity()
                                        + " | Total: " + po.getTotalCost().getAmount() + " "
                                        + po.getTotalCost().getCurrency()
                                        + " | Received: " + (po.isReceived() ? "Yes" : "No"));
                }
        }

        private static void inventoryViewAllSuppliers() {
                Collection<Supplier> suppliers = supplierRepository.findAll();
                if (suppliers.isEmpty()) {
                        System.out.println("No suppliers found.");
                        return;
                }
                System.out.println("\n--- All Suppliers ---");
                for (Supplier s : suppliers) {
                        System.out.println("- ID: " + s.getSupplierId() + " | Name: " + s.getSupplierName()
                                        + " | Contact: " + s.getContactName() + " | Email: " + s.getContactEmail()
                                        + " | Phone: " + s.getContactPhone());
                        if (s.getAddress() != null) {
                                System.out.println("  Address: " + s.getAddress().getStreet() + ", "
                                                + s.getAddress().getCity() + ", " + s.getAddress().getZone());
                        }
                }
        }

        private static void inventoryFetchSupplierFromErp() {
                String supplierId = readString("Supplier ID (ERP): ");
                supplierController.importFromErp(supplierId);
        }

        private static void inventoryDeductStock() {
                String productId = readString("Product ID: ");
                int qty = readInt("Deduct quantity: ");
                Product p = productController.getProduct(productId);
                if (p == null) {
                        System.out.println("Product not found.");
                        return;
                }
                System.out.println("Deducting stock for '" + p.getName() + "'...");
                productController.deductStock(productId, qty);
                System.out.println("New stock quantity: " + productController.getProduct(productId).getStockQuantity());
        }

        private static void inventoryIncreaseStock() {
                String productId = readString("Product ID: ");
                int qty = readInt("Increase by quantity: ");
                Product p = productController.getProduct(productId);
                if (p == null) {
                        System.out.println("Product not found.");
                        return;
                }
                productController.restockProduct(productId, qty);
                System.out.println("New stock quantity: " + productController.getProduct(productId).getStockQuantity());
        }

        private static void inventoryUpdateProduct() {
                String productId = readString("Product ID: ");
                Product p = productController.getProduct(productId);
                if (p == null) {
                        System.out.println("Product not found.");
                        return;
                }

                System.out.println("Updating product: " + p.getName());
                String name = readStringAllowEmpty("New name (leave empty to keep): ");
                String desc = readStringAllowEmpty("New description (leave empty to keep): ");
                String supplierId = readStringAllowEmpty("New supplierId (leave empty to keep): ");

                if (!name.isEmpty()) {
                        p.setName(name);
                }
                if (!desc.isEmpty()) {
                        p.setDescription(desc);
                }
                if (!supplierId.isEmpty()) {
                        p.setSupplierId(supplierId);
                }
                productController.updateProduct(p);
                System.out.println("Product updated.");
        }

        private static void inventoryCreateDecoratedLowStockAlert() {
                String productId = readString("Enter Product ID for low stock alert: ");
                String alertId = "ALT-LOW-DEC-" + productId + "-" + System.currentTimeMillis();

                System.out.println("\n--- Triggering Priority Low Stock Alarm (Logging + Escalation + Retry) ---");
                SystemAlert decoratedAlert = new RetryingAlert(
                                new EscalatingAlert(
                                                new LoggedAlert(
                                                                inventoryAlertController.processAlert(alertId,
                                                                                productId))),
                                3);
                knownAlertIds.add(decoratedAlert.getAlertId());
                System.out.println("Triggering decorated alert notification...");
                decoratedAlert.notifyTarget();
                System.out.println("Priority low stock alarm triggered successfully.");
        }

        // ==================== Orders & Payments ====================
        private static void ordersPaymentsMenu() {
                boolean running = true;
                while (running) {
                        System.out.println("\n-------------------- Orders & Payments --------------------");
                        System.out.println("1) Display customer order history");
                        System.out.println("2) Show payment details");
                        System.out.println("3) Initiate Smart Checkout");
                        System.out.println("4) Update order status based on payment status");
                        System.out.println("5) View order items");
                        System.out.println("0) Back");

                        int choice = readInt("Select an option: ");
                        switch (choice) {
                                case 1 -> ordersShowHistory();
                                case 2 -> paymentsShowDetails();
                                case 3 -> mediatorCheckoutFlow();
                                case 4 -> ordersUpdateStatusFromPayment();
                                case 5 -> ordersViewItemsWithIterator();
                                case 0 -> running = false;
                                default -> System.out.println("Invalid option.");
                        }
                }
        }

        private static void ordersShowHistory() {
                String customerId = readString("Customer ID: ");
                Collection<Order> orders = orderRepository.findAll();
                boolean found = false;
                for (Order o : orders) {
                        if (o != null && customerId.equals(o.getCustomerId())) {
                                found = true;
                                System.out.println("- " + o.getOrderId() + " | " + o.getStatus() + " | Total: "
                                                + o.getTotalAmount().getAmount() + " "
                                                + o.getTotalAmount().getCurrency());

                        }
                }
                if (!found) {
                        System.out.println("No orders found.");
                }
        }

        private static void paymentsShowDetails() {
                String paymentId = readString("Payment ID: ");
                PaymentTransaction tx = paymentRepository.findById(paymentId);
                if (tx == null) {
                        System.out.println("Payment not found.");
                        return;
                }
                System.out.println("Payment: " + tx.getPaymentId() + " | Order: " + tx.getOrderId() + " | Amount: "
                                + tx.getAmount().getAmount() + " " + tx.getAmount().getCurrency() + " | Method: "
                                + tx.getMethod() + " | Status: " + tx.getStatus() + " | Date: "
                                + tx.getTransactionDate());
        }

        private static void mediatorCheckoutFlow() {
                String orderId = readString("Order ID: ");
                String customerId = readString("Customer ID: ");
                Customer customer = customerController.retrieveCustomer(customerId);
                if (customer == null) {
                        System.out.println("Customer not found.");
                        return;
                }

                Order order = new Order(orderId, customerId);
                String productId = readString("Product ID to add: ");
                int qty = readInt("Quantity: ");
                Product p = productController.getProduct(productId);
                if (p == null) {
                        System.out.println("Product not found.");
                        return;
                }
                order.addItem(new OrderItem("ITM-" + System.currentTimeMillis(), p, qty, p.getUnitPrice()));
                orderController.createOrder(order);

                PaymentMethodType method = selectPaymentMethod();
                PaymentProvider paymentFactory = selectPaymentProvider(method);
                PaymentTransaction paymentTx = new PaymentTransaction("TXN-" + System.currentTimeMillis(), orderId,
                                order.getTotalAmount(), method);

                Address address = new Address(readString("Delivery street: "), readString("Delivery city: "),
                                readString("Delivery area: "));
                Delivery delivery = new Delivery("DEL-" + System.currentTimeMillis(), orderId, address,
                                LocalDateTime.now().plusDays(2));

                int decChoice = readInt(
                                "Select delivery update preferences (0=Standard, 1=Basic Tracking, 2=With Notifications, 3=With Priority Alerts): ");
                if (decChoice == 1) {
                        delivery = new LoggedDelivery(delivery);
                } else if (decChoice == 2) {
                        delivery = new NotifyingDelivery(new LoggedDelivery(delivery));
                } else if (decChoice == 3) {
                        delivery = new AlertingDelivery(new NotifyingDelivery(new LoggedDelivery(delivery)));
                }

                CommunicationChannelProvider commProvider = selectCommunicationChannelProvider();
                orderFulfillmentMediator.processCheckout(order, paymentTx, customer, delivery, paymentFactory,
                                commProvider);
        }

        private static void ordersUpdateStatusFromPayment() {
                String orderId = readString("Order ID: ");
                Order order = orderController.getOrder(orderId);
                if (order == null) {
                        System.out.println("Order not found.");
                        return;
                }
                String paymentId = order.getPaymentId();
                if (paymentId == null) {
                        System.out.println("No payment attached to this order.");
                        return;
                }
                PaymentTransaction tx = paymentRepository.findById(paymentId);
                if (tx == null) {
                        System.out.println("Payment record not found for attached paymentId: " + paymentId);
                        return;
                }

                if (tx.getStatus() == PaymentStatus.Completed) {
                        orderController.updateStatus(orderId, OrderStatus.Processing);
                        System.out.println("Order status updated to Processing based on successful payment.");
                } else if (tx.getStatus() == PaymentStatus.Failed) {
                        orderController.updateStatus(orderId, OrderStatus.Cancelled);
                        System.out.println("Order status updated to Cancelled due to failed payment.");
                } else {
                        System.out.println("No automatic status update for payment status: " + tx.getStatus());
                }
        }

        private static void ordersViewItemsWithIterator() {
                String orderId = readString("Order ID: ");
                Order order = orderController.getOrder(orderId);
                if (order == null) {
                        System.out.println("Order not found.");
                        return;
                }

                System.out.println("Order ID: " + orderId + "\n--- Order Items ---");
                Iterator<OrderItem> itemIterator = order.getItems().iterator();
                int count = 0;
                while (itemIterator.hasNext()) {
                        OrderItem item = itemIterator.next();
                        count++;
                        System.out.println("Item #" + count + ": " + item.getOrderItemId()
                                        + " | Product: " + item.getProduct().getName()
                                        + " | Qty: " + item.getQuantity()
                                        + " | Unit Price: " + item.getUnitPrice().getAmount() + " "
                                        + item.getUnitPrice().getCurrency()
                                        + " | Subtotal: " + item.calculateSubtotal().getAmount() + " "
                                        + item.calculateSubtotal().getCurrency());
                }

                if (count == 0) {
                        System.out.println("No items in this order.");
                } else {
                        System.out.println("Total items: " + count);
                }

                // Also demonstrate the High-Value Item Iterator (custom iterator)
                double threshold = readDouble("\nEnter minimum price threshold for high-value items: ");
                Iterator<OrderItem> hvIterator = order.highValueItemIterator(threshold);
                System.out.println("\n--- High-Value Items (>= " + threshold + ") ---");
                int hvCount = 0;
                while (hvIterator.hasNext()) {
                        OrderItem item = hvIterator.next();
                        hvCount++;
                        System.out.println("High-Value Item #" + hvCount + ": " + item.getProduct().getName()
                                        + " | Subtotal: " + item.calculateSubtotal().getAmount() + " "
                                        + item.calculateSubtotal().getCurrency());
                }
                if (hvCount == 0) {
                        System.out.println("No high-value items found above threshold.");
                }
        }

        // ==================== Reports ====================
        private static void reportsMenu() {
                boolean running = true;
                while (running) {
                        System.out.println("\n-------------------- Reporting  --------------------");
                        System.out.println("1) Customer summary report");
                        System.out.println("2) Order report");
                        System.out.println("3) Weekly delivery report");
                        System.out.println("0) Back");

                        int choice = readInt("Select report type: ");
                        switch (choice) {
                                case 1 -> generateReportWithRenderer(1);
                                case 2 -> generateReportWithRenderer(2);
                                case 3 -> generateReportWithRenderer(3);
                                case 0 -> running = false;
                                default -> System.out.println("Invalid option.");
                        }
                }
        }

        private static void generateReportWithRenderer(int reportType) {
                ReportGenerator factory;
                String reportId = readString("Report ID: ");
                int rendererChoice = readInt("Renderer (1=Console, 2=CSV): ");
                var renderer = rendererChoice == 2 ? new CsvReportRenderer() : new ConsoleReportRenderer();

                if (reportType == 1) {
                        factory = new CustomerReportGenerator(renderer);
                } else if (reportType == 2) {
                        factory = new OrderReportGenerator(renderer);
                } else {
                        factory = new WeeklyDeliveryReportGenerator(renderer);
                }
                reportController.generateReport(factory, reportId);
        }

        // ==================== Demo suite ====================
        private static void runDemoSuite() {
                testSingleton();
                testAlertSystem();
                testReportSystem();
                testCustomerOperations();
                testInventoryAndProducts();
                testPaymentSystem();
                testAdapterPattern();
                testBridgePatternCommunication();
                testComplaintSystem();
                testOrderFulfillmentMediator();
                testDecoratorPatternDelivery();
        }

        private static void testSingleton() {
                System.out.println("========== Testing Database Connection Optimization ==========");

                DatabaseConnectionManager db1 = DatabaseConnectionManager.getInstance();
                DatabaseConnectionManager db2 = DatabaseConnectionManager.getInstance();

                db1.connect();
                if (db1 == db2) {
                        System.out.println("Optimization works! Both references point to the same instance.");
                } else {
                        System.out.println("Optimization failed! Instances are different.");
                }
                db1.disconnect();

                System.out.println();
        }

        // 2 + 7. ALERT SYSTEM (Factory Method & Decorator Patterns)
        private static void testAlertSystem() {
                System.out.println("========== Testing Alert System  ==========");
                AlertRepository alertRepo = new AlertRepository();
                InventoryAlertController inventoryAlertCtrl = new InventoryAlertController(alertRepo);

                System.out.println("--- Demo 1: Dynamic Alert Construction ---");
                SystemAlert rawAlert = inventoryAlertCtrl.processAlert("ALT-001", sharedProduct.getProductId());
                System.out.println("Alert created dynamically: " + rawAlert.getClass().getSimpleName());

                System.out.println("\n--- Demo 2: Decorated Alert (Logging + Escalation + Retry) ---");
                SystemAlert decoratedSla = new RetryingAlert(
                                new EscalatingAlert(
                                                new LoggedAlert(
                                                                new SlaBreachAlertController(alertRepo)
                                                                                .processAlert("ALT-D03", "CMP-7"))),
                                3);
                decoratedSla.notifyTarget();
                System.out.println();
        }

        // 5 + 10. REPORT SYSTEM (Factory Method & Bridge Patterns)
        private static void testReportSystem() {
                System.out.println("========== Testing Flexible Report Generation System ==========");
                ReportController reportController = new ReportController();

                // Showcasing Bridge via Factory
                ReportGenerator factory = new CustomerReportGenerator(new ConsoleReportRenderer());
                Report report = reportController.generateReport(factory, "RPT-202");
                System.out.println("Report [" + report.getReportId() + "] generated using Console Renderer.");
                report.generate();

                System.out.println("\n--- Switching Renderer to CSV ---");
                Report csvReport = new OrderReport("RPT-ORD-02", new CsvReportRenderer());
                csvReport.generate();

                System.out.println("======================================================");
        }

        // 3 + 13. CUSTOMER OPERATIONS (Abstract Factory & Segmentation)
        private static void testCustomerOperations() {
                System.out.println("========== Testing Customer Operations (Communication & Segmentation) ==========");

                // --- Part 1: Customer Controller ---
                CustomerRepository customerRepo = new CustomerRepository();
                CustomerController customerCtrl = new CustomerController(customerRepo);
                customerCtrl.createCustomer(sharedCustomer.getCustomerId(), sharedCustomer);
                System.out.println("Customer [" + sharedCustomer.getName() + "] managed via CustomerController.");

                // --- Part 2: Segmentation ---
                CustomerSegment vipSegment = new CustomerSegment("SEG-01", "VIP Customers", "High lifetime value");
                new CustomerSegmentMembership("MEM-001", sharedCustomer.getCustomerId(), vipSegment.getSegmentId());
                System.out.println("Customer [" + sharedCustomer.getName() + "] assigned to segment: "
                                + vipSegment.getName());

                // --- Part 3: Communication (Abstract Factory) ---
                CommunicationChannelProvider SmsChannelProvider = new SmsChannelProvider();
                CommunicationController commController = new CommunicationController();

                System.out.println("\n--- Communication: Employee -> Customer via SMS ---");
                commController.sendMessageFromEmployeeToCustomer(sharedEmployee, sharedCustomer,
                                "Hello " + sharedCustomer.getName() + ", account SEG-01 is ready!", SmsChannelProvider);

                System.out.println();
        } // 9. DECORATOR PATTERN & ORDER CONTROLLER

        private static void checkAndPrintObjectReuse(String productId, String name) {
                System.out.println("\n--- Memory Optimization Demo for: " + productId + " ---");
                Product p1 = ProductCreator.getOrCreate(productId, name, "Category A", new Money(10.0, "USD"), 10,
                                sharedSupplierId);
                System.out.println(
                                "First retrieval [" + p1.getName() + "] -> Memory ID: " + System.identityHashCode(p1));

                Product p2 = ProductCreator.getOrCreate(productId, "Ignored Alternate Name", "Category B",
                                new Money(10.0, "USD"), 10, sharedSupplierId);
                System.out.println(
                                "Second retrieval [" + p2.getName() + "] -> Memory ID: " + System.identityHashCode(p2));

                if (p1 == p2) {
                        System.out.println(
                                        "Optimization verified: Both references point to the exact same object in memory. Creation overhead bypassed!");
                } else {
                        System.out.println("Optimization failed: Objects are mysteriously different.");
                }
        }

        // INVENTORY & PRODUCT SYSTEM (Flyweight, Controller, Records)
        private static void testInventoryAndProducts() {
                System.out.println(
                                "========== Testing Inventory & Product System (Object Caching & Records) ==========");

                // Part 1: Flyweight
                checkAndPrintObjectReuse(sharedProduct.getProductId(), "Mouse");
                checkAndPrintObjectReuse(sharedProduct2.getProductId(), "Keyboard");

                // Part 2: Product & Inventory Controllers
                System.out.println("\n--- Product & Inventory Management Controllers ---");
                ProductRepository productRepo = new ProductRepository();
                ProductController productCtrl = new ProductController(productRepo);
                productCtrl.createProduct(sharedProduct2);

                InventoryRecordRepository invRepo = new InventoryRecordRepository();
                InventoryController invCtrl = new InventoryController(invRepo);

                InventoryRecord record = new InventoryRecord("REC-X", sharedProduct2.getProductId(), "Zayed Warehouse",
                                50, 10);
                invCtrl.createInventoryRecord(record);

                System.out.println("Inventory tracking active for: " + sharedProduct2.getName());
                System.out.println("Initial Stock: " + sharedProduct2.getStockQuantity() + " | Reorder at: "
                                + record.getReorderLevel());

                // ---------------------------------------------------------
                // Observer registration — wire up automatic notifications.
                // InventoryAlertController creates a LowStockAlert on every
                // deduction; SupplierController raises an auto-reorder PO
                // when the quantity hits 0. Both happen without any manual
                // isBelowReorderLevel() check in the caller.
                // ---------------------------------------------------------
                AlertRepository alertRepo = new AlertRepository();
                InventoryAlertController alertCtrl = new InventoryAlertController(alertRepo);

                SupplierRepository supplierRepo2 = new SupplierRepository();
                PurchaseOrderRepository poRepo2 = new PurchaseOrderRepository();
                SupplierController supplierCtrl2 = new SupplierController(supplierRepo2, null, poRepo2);

                InventoryEventManager eventMgr = InventoryEventManager.getInstance();
                eventMgr.register(alertCtrl);
                eventMgr.register(supplierCtrl2);
                eventMgr.register(invCtrl); // Add InventoryController as an observer

                // Deducting stock — observers notified automatically
                System.out.println("\n--- Simulating Sales to Trigger Alert ---");
                productCtrl.deductStock(sharedProduct2.getProductId(), 45); // Using ProductController
                // The warehouse record (REC-X) is now automatically synced by
                // InventoryController's onStockChanged!

                System.out.println("Current Stock Quantity: " + sharedProduct2.getStockQuantity());
                System.out.println("Inventory Record Available: " + record.getAvailableQuantity());

                // Clean up so other tests are not affected by these observers.
                eventMgr.unregister(alertCtrl);
                eventMgr.unregister(supplierCtrl2);
                eventMgr.unregister(invCtrl);

                System.out.println("======================================================");
        }

        // 4 + 8. PAYMENT SYSTEM (Abstract Factory, Adapter, & Controller)
        private static void testPaymentSystem() {
                System.out.println("========== Testing Multi-Gateway Payment System ==========");

                // Set up Controller
                PaymentRepository payRepo = new PaymentRepository();
                RefundRepository refundRepo = new RefundRepository();
                PaymentController payCtrl = new PaymentController(payRepo, refundRepo);

                // Demo 1: Controller using a specific Factory (CardPaymentProvider)
                System.out.println("--- Demo 1: Standard Card Payment (Manual Injection) ---");
                PaymentTransaction transaction1 = new PaymentTransaction("TXN-001", "ORD-123", new Money(100.0, "USD"),
                                PaymentMethodType.Card);
                payCtrl.processPayment(transaction1, new CardPaymentProvider());

                // Demo 2: Controller using internal Adapter logic (Stripe)
                System.out.println("\n--- Demo 2: External Gateway Payment (Translation Logic) ---");
                PaymentTransaction transaction2 = new PaymentTransaction("TXN-002", "ORD-999", new Money(50.0, "USD"),
                                PaymentMethodType.Card);
                // The controller will automatically use StripePaymentProvider for Card method
                payCtrl.processPayment(transaction2, new StripePaymentProvider());

                // Demo 3: Cash On Delivery
                System.out.println("\n--- Demo 3: Cash On Delivery ---");
                PaymentTransaction transaction3 = new PaymentTransaction("TXN-003", "ORD-555", new Money(75.0, "USD"),
                                PaymentMethodType.CashOnDelivery);
                payCtrl.processPayment(transaction3, new CodPaymentProvider());

                System.out.println("======================================================");
        }

        // 6. ADAPTER PATTERN — ErpSupplierAdapter + SupplierController
        private static void testAdapterPattern() {
                System.out.println("========== Testing ERP Integration Translation ==========");

                SupplierRepository supplierRepo = new SupplierRepository();
                PurchaseOrderRepository poRepo = new PurchaseOrderRepository();
                SupplierDataService adapter = new ErpSupplierConnector();
                ProductRepository repo = new ProductRepository();

                // Full constructor: supplier repo + ERP adapter + PO repo
                SupplierController controller = new SupplierController(supplierRepo, adapter, poRepo);

                System.out.println("--- Supplier Operations ---");
                // Import supplier from ERP → saved into CRM supplier repo
                controller.importFromErp(sharedSupplierId);
                // Export supplier back to ERP
                controller.exportToErp(sharedSupplierId);

                System.out.println("\n--- Purchase Order Operations ---");
                // Import PO from ERP → saved into CRM PO repo
                controller.importPoFromErp("PO-101");
                // Export PO from CRM repo → pushed to ERP
                PurchaseOrderController Pocontroller = new PurchaseOrderController(poRepo, repo);
                PurchaseOrder po = new PurchaseOrder("PO-102", sharedSupplierId, sharedProduct4.getProductId(), 10,
                                new Money(1500.0, "USD"));
                Pocontroller.createPurchaseOrder(po);
                controller.exportPoToErp(po.getPurchaseOrderId());

                System.out.println("======================================================");
        }

        // 11. BRIDGE PATTERN — Communication Channels
        private static void testBridgePatternCommunication() {
                System.out.println("========== Testing Extensible Communication Channels ==========");

                // SMS Bridge
                System.out.println("--- Sending via SMS Channel ---");
                Notification smsNotif = new ChannelNotification("Urgent Update", new SMSChannel());
                smsNotif.send();

                // Email Bridge
                System.out.println("\n--- Sending via Email Channel ---");
                Notification emailNotif = new ChannelNotification("Weekly Newsletter", new EmailChannel());
                emailNotif.send();

                System.out.println("======================================================");
        }

        private static void testComplaintSystem() {
                System.out.println("========== Testing Complaint System (Controller & SLA Checks) ==========");
                ComplaintRepository complaintRepo = new ComplaintRepository();
                ComplaintController complaintCtrl = new ComplaintController(complaintRepo);
                SlaBreachAlertController alertCtrl = new SlaBreachAlertController(new AlertRepository());
                CommunicationController commCtrl = new CommunicationController();
                ComplaintEscalationHandler mediator = new ComplaintEscalationHandlerService(complaintCtrl, alertCtrl,
                                commCtrl);
                complaintCtrl.setMediator(mediator);

                Complaint complaint = new Complaint("CMP-001", "High Priority",
                                new com.crm.customer.SlaCalculator.StandardSlaCalculator());
                complaintCtrl.createComplaint(complaint);
                System.out.println("Complaint CMP-001 created for Customer [" + sharedCustomer.getName() + "]");
                System.out.println("Priority: " + complaint.getPriority());

                CommunicationChannelProvider commProvider = new SmsChannelProvider();

                System.out.println("\n--- 1. Attempt Breach Check (No breach yet) ---");
                // Will hit the early return block since it's just created!
                complaintCtrl.detectSlaBreach(complaint, sharedCustomer, sharedEmployee, commProvider);

                System.out.println("\n--- 2. Simulating Time Passing -> Real SLA Breach ---");
                // Cheat the clock backwards by 72 hours
                complaint.setSlaDeadline(java.time.LocalDateTime.now().minusHours(72));
                // Will now successfully breach and call the mediator
                complaintCtrl.detectSlaBreach(complaint, sharedCustomer, sharedEmployee, commProvider);

                System.out.println("\n--- 3. Testing Resolved Status ---");
                // If it's resolved, it should not escalate again
                complaint.updateStatus(com.crm.common.enums.ComplaintStatus.Resolved);
                complaintCtrl.detectSlaBreach(complaint, sharedCustomer, sharedEmployee, commProvider);

                System.out.println("======================================================");
        }

        private static void testOrderFulfillmentMediator() {
                System.out.println("========== Testing Order Fulfillment Mediator (Checkout Hub) ==========");

                OrderRepository orderRepo = new OrderRepository();
                OrderController orderCtrl = new OrderController(orderRepo);

                PaymentRepository payRepo = new PaymentRepository();
                RefundRepository refundRepo = new RefundRepository();
                PaymentController payCtrl = new PaymentController(payRepo, refundRepo);

                ProductRepository prodRepo = new ProductRepository();
                ProductController prodCtrl = new ProductController(prodRepo);

                CommunicationController commCtrl = new CommunicationController();

                OrderFulfillment mediator = new OrderFulfillment(orderCtrl, payCtrl, prodCtrl,
                                commCtrl);

                Order order = new Order("ORD-MED-01", sharedCustomer.getCustomerId());
                order.addItem(new OrderItem("ITM-001", sharedProduct, 1, new Money(149.99, "EGP")));
                orderCtrl.createOrder(order);

                PaymentTransaction pTx = new PaymentTransaction("TXN-MED-01", order.getOrderId(),
                                order.getTotalAmount(), PaymentMethodType.Card);
                Address delAddress = new Address("789 Boulevard", "Cairo", "Heliopolis");
                Delivery delivery = new Delivery("DEL-MED-01", order.getOrderId(), delAddress,
                                java.time.LocalDateTime.now().plusDays(2));

                CommunicationChannelProvider SmsChannelProvider = new SmsChannelProvider();

                mediator.processCheckout(order, pTx, sharedCustomer, delivery, new StripePaymentProvider(),
                                SmsChannelProvider);

                System.out.println("======================================================");

        }

        private static void testDecoratorPatternDelivery() {
                System.out.println("========== Testing Dynamic Behavior Extension & Order Controller ==========");

                // Set up Order and Controller
                OrderRepository orderRepo = new OrderRepository();
                OrderController orderCtrl = new OrderController(orderRepo);
                Order order = new Order("ORD-123", sharedCustomer.getCustomerId());
                orderCtrl.createOrder(order);

                // Base delivery object
                Address delAddress = new Address("123 Street", "Cairo", "Zamalek");
                LocalDateTime delEta = LocalDateTime.now().plusDays(3);
                Delivery baseDelivery = new Delivery("DEL-777", order.getOrderId(), delAddress, delEta);
                orderCtrl.attachDelivery(order.getOrderId(), baseDelivery);

                System.out.println("--- Initial State via Controller ---");
                System.out.println("Delivery tied to Order [" + order.getOrderId() + "] Status: "
                                + baseDelivery.getDeliveryStatus());

                // Wrap with Logging + Notifying + Alerting
                System.out.println("\n--- Decorated State (Logging + Notifying + Alerting) ---");
                Delivery decoratedDelivery = new AlertingDelivery(
                                new NotifyingDelivery(
                                                new LoggedDelivery(baseDelivery)));

                decoratedDelivery.markOnTheWay();
                decoratedDelivery.markDelayed(); // Should trigger alert + log + notify
                decoratedDelivery.markDelivered(); // Should log + notify

                System.out.println("======================================================");
        }

        // Backup testing

        private static SlaCalculator selectSlaStrategy() {
                System.out.println("SLA Strategy:");
                System.out.println("1) Standard SLA");
                System.out.println("2) VIP SLA");
                System.out.println("3) Priority-based SLA");
                int choice = readInt("Choose: ");
                return switch (choice) {
                        case 2 -> new VipSlaCalculator();
                        case 3 -> new PriorityBasedSlaCalculator(24);
                        default -> new StandardSlaCalculator();
                };
        }

        private static CommunicationChannelProvider selectCommunicationChannelProvider() {
                System.out.println("Communication Channel:");
                System.out.println("1) SMS");
                System.out.println("2) Email");
                System.out.println("3) Chat");
                int choice = readInt("Choose: ");
                return switch (choice) {
                        case 2 -> new EmailChannelProvider();
                        case 3 -> new ChatChannelProvider();
                        default -> new SmsChannelProvider();
                };
        }

        private static PaymentMethodType selectPaymentMethod() {
                System.out.println("Payment Method:");
                System.out.println("1) Card");
                System.out.println("2) CashOnDelivery");
                System.out.println("3) Stripe");
                int choice = readInt("Choose: ");
                return switch (choice) {
                        case 2 -> PaymentMethodType.CashOnDelivery;
                        case 3 -> PaymentMethodType.Stripe;
                        default -> PaymentMethodType.Card;
                };
        }

        private static PaymentProvider selectPaymentProvider(PaymentMethodType method) {
                if (method == PaymentMethodType.CashOnDelivery) {
                        return new CodPaymentProvider();
                }
                int choice = readInt("Card Processor (1=Stripe, 2=CardPaymentProvider): ");
                return choice == 2 ? new CardPaymentProvider() : new StripePaymentProvider();
        }

        private static int readInt(String prompt) {
                while (true) {
                        System.out.print(prompt);
                        String input = scanner.nextLine();
                        try {
                                return Integer.parseInt(input.trim());
                        } catch (Exception e) {
                                System.out.println("Please enter a valid number.");
                        }
                }
        }

        private static double readDouble(String prompt) {
                while (true) {
                        System.out.print(prompt);
                        String input = scanner.nextLine();
                        try {
                                return Double.parseDouble(input.trim());
                        } catch (Exception e) {
                                System.out.println("Please enter a valid decimal number.");
                        }
                }
        }

        private static String readString(String prompt) {
                while (true) {
                        System.out.print(prompt);
                        String input = scanner.nextLine();
                        if (input != null && !input.trim().isEmpty()) {
                                return input.trim();
                        }
                        System.out.println("Value cannot be empty.");
                }
        }

        private static String readStringAllowEmpty(String prompt) {
                System.out.print(prompt);
                String input = scanner.nextLine();
                return input == null ? "" : input.trim();
        }

        // old main
        private static void setupSharedObjects() {
                sharedCustomer = new Customer("CUST-001", "Jane Doe", "jane@example.com", "123-456-7890");
                sharedEmployee = new Employee("EMP-001", "John Smith", "Support Agent", "john.smith@crm.com");

                Money pPrice = new Money(149.99, "EGP");
                Money pPrice2 = new Money(299.99, "EGP");
                sharedProduct = ProductCreator.getOrCreate("PROD-01", "Wireless Mouse",
                                "Ergonomic wireless mouse", pPrice, 20, sharedSupplierId);
                sharedProduct2 = ProductCreator.getOrCreate("PROD-02", "Wireless Keyboard",
                                "Ergonomic wireless keyboard", pPrice2, 21, sharedSupplierId);
        }
}