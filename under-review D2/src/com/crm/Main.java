package com.crm;

import com.crm.alert.controller.InventoryAlertController;
import com.crm.alert.controller.SlaBreachAlertController;
import com.crm.communication.controller.CommunicationController;
import com.crm.inventory.controller.SupplierController;
import com.crm.inventory.repository.PurchaseOrderRepository;
import com.crm.inventory.repository.SupplierRepository;
import com.crm.inventory.service.ErpSupplierAdapter;
import com.crm.inventory.service.SupplierDataService;
import com.crm.customer.controller.CustomerController;
import com.crm.customer.repository.CustomerRepository;
import com.crm.inventory.repository.InventoryRecordRepository;
import com.crm.inventory.controller.InventoryController;
import com.crm.order.controller.OrderController;
import com.crm.order.repository.OrderRepository;
import com.crm.order.model.Order;
import com.crm.customer.controller.ComplaintController;
import com.crm.customer.repository.ComplaintRepository;
import com.crm.customer.model.Complaint;
import com.crm.payment.controller.PaymentController;
import com.crm.payment.repository.PaymentRepository;
import com.crm.payment.repository.RefundRepository;
import com.crm.payment.model.PaymentTransaction;
import com.crm.common.enums.PaymentMethodType;
import com.crm.alert.repository.AlertRepository;
import com.crm.communication.factory.CommunicationFactory;
import com.crm.communication.factory.SMSFactory;
import com.crm.payment.factory.CardPaymentFactory;
import com.crm.payment.factory.PaymentFactory;
import com.crm.reporting.factory.CustomerReportFactory;
import com.crm.reporting.factory.ReportFactory;
import com.crm.reporting.model.Report;
import com.crm.alert.model.SystemAlert;
import com.crm.alert.model.LoggingAlertDecorator;
import com.crm.alert.model.EscalatingAlertDecorator;
import com.crm.alert.model.RetryAlertDecorator;
import com.crm.common.Employee;
import com.crm.common.Money;
import com.crm.customer.model.Customer;
import com.crm.payment.model.PaymentProcessor;
import com.crm.persistence.DatabaseConnectionManager;
import com.crm.inventory.model.Product;
import com.crm.common.Address;
import java.time.LocalDateTime;
import com.crm.payment.model.StripePaymentAdapter;
import com.crm.order.model.Delivery;
import com.crm.order.model.LoggingDeliveryDecorator;
import com.crm.order.model.NotifyingDeliveryDecorator;
import com.crm.order.model.AlertingDeliveryDecorator;
import com.crm.reporting.ConsoleReportRenderer;
import com.crm.reporting.controller.ReportController;
import com.crm.reporting.CsvReportRenderer;
import com.crm.reporting.model.OrderReport;
import com.crm.communication.Notification;
import com.crm.communication.ChannelNotification;
import com.crm.communication.channel.Email.EmailChannel;
import com.crm.communication.channel.SMS.SMSChannel;
import com.crm.inventory.model.ProductFactory;
import com.crm.inventory.model.PurchaseOrder;
import com.crm.inventory.model.InventoryRecord;
import com.crm.inventory.repository.ProductRepository;
import com.crm.inventory.controller.ProductController;
import com.crm.inventory.controller.PurchaseOrderController;
import com.crm.customer.model.CustomerSegment;
import com.crm.customer.model.CustomerSegmentMembership;

public class Main {
        // Shared Instances for all tests
        private static Customer sharedCustomer;
        private static Product sharedProduct;
        private static Product sharedProduct2;
        private static Employee sharedEmployee;
        private static String sharedSupplierId = "SUP-01";

        public static void main(String[] args) {
                setupSharedObjects();

                testSingleton(); // Singleton Pattern
                testAlertSystem(); // Factory Method & Decorator (Alerts)
                testReportSystem(); // Factory Method & Bridge (Reporting)

                testCustomerOperations(); // Abstract Factory, Segmentation, Communication
                testInventoryAndProducts(); // Flyweight, Controller, Inventory Records

                testPaymentSystem(); // Abstract Factory & Adapter (Stripe)
                testAdapterPattern(); // Adapter Pattern (ERP Integration)
                testDecoratorPatternDelivery(); // Decorator: Delivery Status
                testBridgePatternCommunication(); // Bridge: Communication Channels
                testComplaintSystem(); // Complaint Controller Demo
        }

        private static void setupSharedObjects() {
                sharedCustomer = new Customer("CUST-001", "Jane Doe", "jane@example.com", "123-456-7890");
                sharedEmployee = new Employee("EMP-001", "John Smith", "Support Agent", "john.smith@crm.com");

                Money pPrice = new Money(149.99, "EGP");
                Money pPrice2 = new Money(299.99, "EGP");
                sharedProduct = ProductFactory.getOrCreate("PROD-01", "Wireless Mouse",
                                "Ergonomic wireless mouse", pPrice, 20, sharedSupplierId);
                sharedProduct2 = ProductFactory.getOrCreate("PROD-02", "Wireless Keyboard",
                                "Ergonomic wireless keyboard", pPrice2, 21, sharedSupplierId);
        }

        // 1. SINGLETON PATTERN — DatabaseConnectionManager
        private static void testSingleton() {
                System.out.println("========== Testing Singleton Pattern ==========");

                DatabaseConnectionManager db1 = DatabaseConnectionManager.getInstance();
                DatabaseConnectionManager db2 = DatabaseConnectionManager.getInstance();

                db1.connect();
                if (db1 == db2) {
                        System.out.println("Singleton works! Both references point to the same instance.");
                } else {
                        System.out.println("Singleton failed! Instances are different.");
                }
                db1.disconnect();

                System.out.println();
        }

        // 2 + 7. ALERT SYSTEM (Factory Method & Decorator Patterns)
        private static void testAlertSystem() {
                System.out.println("========== Testing Alert System (Factory & Decorator) ==========");
                AlertRepository alertRepo = new AlertRepository();
                InventoryAlertController inventoryAlertCtrl = new InventoryAlertController(alertRepo);

                System.out.println("--- Demo 1: Factory Method Alert ---");
                SystemAlert rawAlert = inventoryAlertCtrl.processAlert("ALT-001", sharedProduct.getProductId());
                System.out.println("Alert created via Factory: " + rawAlert.getClass().getSimpleName());

                System.out.println("\n--- Demo 2: Decorated Alert (Logging + Escalation + Retry) ---");
                SystemAlert decoratedSla = new RetryAlertDecorator(
                                new EscalatingAlertDecorator(
                                                new LoggingAlertDecorator(
                                                                new SlaBreachAlertController(alertRepo)
                                                                                .processAlert("ALT-D03", "CMP-7"))),
                                3);
                decoratedSla.notifyTarget();
                System.out.println();
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
                CommunicationFactory smsFactory = new SMSFactory();
                CommunicationController commController = new CommunicationController();

                System.out.println("\n--- Communication: Employee -> Customer via SMS ---");
                commController.sendMessageFromEmployeeToCustomer(sharedEmployee, sharedCustomer,
                                "Hello " + sharedCustomer.getName() + ", account SEG-01 is ready!", smsFactory);

                System.out.println();
        } // 9. DECORATOR PATTERN & ORDER CONTROLLER

        private static void testDecoratorPatternDelivery() {
                System.out.println("========== Testing Decorator Pattern & Order Controller ==========");

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
                Delivery decoratedDelivery = new AlertingDeliveryDecorator(
                                new NotifyingDeliveryDecorator(
                                                new LoggingDeliveryDecorator(baseDelivery)));

                decoratedDelivery.markOnTheWay();
                decoratedDelivery.markDelayed(); // Should trigger alert + log + notify
                decoratedDelivery.markDelivered(); // Should log + notify

                System.out.println("======================================================");
        }

        // 4 + 8. PAYMENT SYSTEM (Abstract Factory, Adapter, & Controller)
        private static void testPaymentSystem() {
                System.out.println("========== Testing Payment System (Factory, Adapter, & Controller) ==========");

                // Set up Controller
                PaymentRepository payRepo = new PaymentRepository();
                RefundRepository refundRepo = new RefundRepository();
                PaymentController payCtrl = new PaymentController(payRepo, refundRepo);

                // Test 1: Standard Card Factory
                System.out.println("--- Demo 1: Standard Card Payment ---");
                PaymentFactory cardFactory = new CardPaymentFactory();
                PaymentProcessor cardProcessor = cardFactory.createProcessor();
                cardProcessor.processPayment(new Money(100.0, "USD"));

                // Saving via Controller
                PaymentTransaction transaction = new PaymentTransaction("TXN-001", "ORD-123", new Money(100.0, "USD"),
                                PaymentMethodType.Card);
                payCtrl.processPayment(transaction);
                System.out.println("Transaction TXN-001 saved via PaymentController.");

                // Test 2: Stripe Adapter
                System.out.println("\n--- Demo 2: Stripe Adapter Payment ---");
                PaymentProcessor stripeAdapter = new StripePaymentAdapter();
                stripeAdapter.processPayment(new Money(100000, "USD"));

                System.out.println("======================================================");
        }

        // 5 + 10. REPORT SYSTEM (Factory Method & Bridge Patterns)
        private static void testReportSystem() {
                System.out.println("========== Testing Report System (Factory & Bridge) ==========");
                ReportController reportController = new ReportController();

                // Showcasing Bridge via Factory
                ReportFactory factory = new CustomerReportFactory(new ConsoleReportRenderer());
                Report report = reportController.generateReport(factory, "RPT-202");
                System.out.println("Report [" + report.getReportId() + "] generated using Console Renderer.");
                report.generate();

                System.out.println("\n--- Switching Renderer to CSV ---");
                Report csvReport = new OrderReport("RPT-ORD-02", new CsvReportRenderer());
                csvReport.generate();

                System.out.println("======================================================");
        }

        // 6. ADAPTER PATTERN — ErpSupplierAdapter + SupplierController
        private static void testAdapterPattern() {
                System.out.println("========== Testing Adapter Pattern (ERP Integration) ==========");

                SupplierRepository supplierRepo = new SupplierRepository();
                PurchaseOrderRepository poRepo = new PurchaseOrderRepository();
                SupplierDataService adapter = new ErpSupplierAdapter();
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
                PurchaseOrder po = new PurchaseOrder("PO-102", sharedSupplierId, sharedProduct2.getProductId(), 10,
                                new Money(1500.0, "USD"));
                Pocontroller.createPurchaseOrder(po);
                controller.exportPoToErp(po.getPurchaseOrderId());

                System.out.println("======================================================");
        }

        // 11. BRIDGE PATTERN — Communication Channels
        private static void testBridgePatternCommunication() {
                System.out.println("========== Testing Bridge Pattern (Communication) ==========");

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

        // 12 + 14. INVENTORY & PRODUCT SYSTEM (Flyweight, Controller, Records)
        private static void testInventoryAndProducts() {
                System.out.println("========== Testing Inventory & Product System (Flyweight & Records) ==========");

                // Part 1: Flyweight
                System.out.println("--- Flyweight Pattern ---");
                Product p1 = ProductFactory.getOrCreate(sharedProduct.getProductId(), "Mouse", "Recycled",
                                new Money(10.0, "USD"), 10, sharedSupplierId);
                if (p1 == sharedProduct) {
                        System.out.println("Flyweight verified: Reused existing product object.");
                }
                Product p2 = ProductFactory.getOrCreate(sharedProduct2.getProductId(), "Keyboard", "Recycled",
                                new Money(10.0, "USD"), 10, sharedSupplierId);
                if (p2 == sharedProduct2) {
                        System.out.println("Flyweight verified: Reused existing product object.");
                }

                // Part 2: Product & Inventory Controllers
                System.out.println("\n--- Product & Inventory Management Controllers ---");
                ProductRepository productRepo = new ProductRepository();
                ProductController productCtrl = new ProductController(productRepo);
                productCtrl.createProduct(sharedProduct2);

                InventoryRecordRepository invRepo = new InventoryRecordRepository();
                InventoryController invCtrl = new InventoryController(invRepo);

                InventoryRecord record = new InventoryRecord("REC-X", sharedProduct2.getProductId(), 50, 10);
                invCtrl.createInventoryRecord(record);

                System.out.println("Inventory tracking active for: " + sharedProduct2.getName());
                System.out.println("Initial Stock: " + sharedProduct2.getStockQuantity() + " | Reorder at: "
                                + record.getReorderLevel());

                // Deducting stock to trigger alert
                System.out.println("\n--- Simulating Sales to Trigger Alert ---");
                productCtrl.deductStock(sharedProduct2.getProductId(), 45); // Using ProductController
                invCtrl.decreaseInventory("REC-X", 45); // Syncing to InventoryController

                System.out.println("Current Stock Quantity: " + sharedProduct2.getStockQuantity());
                System.out.println("Inventory Record Available: " + record.getAvailableQuantity());

                if (invCtrl.isBelowReorderLevel("REC-X")) {
                        System.out.println("ALERT LOGIC: Record reports stock is below reorder level.");
                        AlertRepository alertRepo = new AlertRepository();
                        InventoryAlertController alertCtrl = new InventoryAlertController(alertRepo);
                        alertCtrl.processAlert("ALT-AUTO-01", sharedProduct2.getProductId()).notifyTarget();
                }

                System.out.println("======================================================");
        }

        private static void testComplaintSystem() {
                System.out.println("========== Testing Complaint System (Controller) ==========");
                ComplaintRepository complaintRepo = new ComplaintRepository();
                ComplaintController complaintCtrl = new ComplaintController(complaintRepo);

                Complaint complaint = new Complaint("CMP-001", "High Priority");
                complaintCtrl.createComplaint(complaint);

                System.out.println("Complaint CMP-001 created for Customer [" + sharedCustomer.getName() + "]");
                System.out.println("Priority: " + complaint.getPriority());
                System.out.println("======================================================");
        }
}
