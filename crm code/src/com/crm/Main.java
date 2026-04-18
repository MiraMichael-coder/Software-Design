package com.crm;

<<<<<<< Updated upstream
import com.crm.alert.controller.AlertController;
import com.crm.alert.factory.AlertFactory;
import com.crm.alert.factory.LowStockAlertFactory;
import com.crm.alert.model.SystemAlert;
import com.crm.alert.repository.AlertRepository;
import com.crm.common.Money;
import com.crm.common.enums.PaymentMethodType;

import com.crm.payment.model.PaymentTransaction;
import com.crm.persistence.DatabaseConnectionManager;

import static com.crm.common.enums.PaymentMethodType.Card;

public class Main {
    public static void main(String[] args) {
        DatabaseConnectionManager.getInstance().connect();
        System.out.println("CRM System Started...");
        DatabaseConnectionManager.getInstance().disconnect();

        Money amount = new Money(23.1, "USD");
        PaymentTransaction p1 = new PaymentTransaction("123", "Ord12", amount, Card);
        System.out.println(p1.getMethod());

        // --- Demo: Refactored Alert Factory (Factory Method Pattern) ---
        AlertRepository alertRepo = new AlertRepository();
        AlertController alertController = new AlertController(alertRepo);

        // We pass the concrete factory (LowStockAlertFactory) to the controller
        AlertFactory lowStockFactory = new LowStockAlertFactory();
        SystemAlert alert = alertController.createAlert(lowStockFactory, "ALT-001", "PROD-99");

        System.out.println("Created Alert: " + alert.getClass().getSimpleName() + " for ID: " + alert.getAlertId());

=======
// Controllers
import com.crm.alert.controller.InventoryAlertController;
import com.crm.communication.controller.CommunicationController;
import com.crm.customer.controller.CustomerController;
import com.crm.customer.controller.MessageController;
import com.crm.payment.controller.PaymentController;
import com.crm.reporting.controller.ReportController;

// Repositories
import com.crm.alert.repository.AlertRepository;
import com.crm.customer.repository.CustomerRepository;
import com.crm.customer.repository.MessageRepository;
import com.crm.payment.repository.PaymentRepository;
import com.crm.payment.repository.RefundRepository;

// Factories
import com.crm.communication.factory.CommunicationFactory;
import com.crm.communication.factory.EmailFactory;
import com.crm.communication.factory.SMSFactory;
import com.crm.payment.factory.CardPaymentFactory;
import com.crm.payment.factory.PaymentFactory;
import com.crm.reporting.factory.CustomerReportFactory;
import com.crm.reporting.factory.ReportFactory;

// Models
import com.crm.alert.model.SystemAlert;
import com.crm.common.Employee;
import com.crm.common.Money;
import com.crm.common.enums.PaymentMethodType;
import com.crm.customer.model.Customer;
import com.crm.payment.model.PaymentProcessor;
import com.crm.payment.model.PaymentReceipt;
import com.crm.payment.model.PaymentTransaction;
import com.crm.persistence.DatabaseConnectionManager;
import com.crm.reporting.Report;

public class Main {
    public static void main(String[] args) {
        testSingleton(); // Singleton Pattern
        testFactoryMethod(); // Factory Method via AlertController
        testAbstractFactory(); // Abstract Factory via CommunicationController + MessageController
        testPaymentController(); // Abstract Factory via PaymentController
        testReportController(); // Factory Method via ReportController
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

    // 2. FACTORY METHOD — AlertController (InventoryAlertController)
    private static void testFactoryMethod() {
        System.out.println("========== Testing Factory Method Pattern ==========");

        AlertRepository alertRepo = new AlertRepository();
        InventoryAlertController alertController = new InventoryAlertController(alertRepo);

        // processAlert() internally calls the Factory Method createAlert()
        SystemAlert alert = alertController.processAlert("ALT-001", "PROD-99");
        System.out.println("Alert created & saved via controller: " + alert.getClass().getSimpleName()
                + " [ID: " + alert.getAlertId() + "]");

        alertController.resolveAlert("ALT-001");
        System.out.println("Alert resolved through controller.");

        System.out.println();
    }

    // 3. ABSTRACT FACTORY — CommunicationController + MessageController
    private static void testAbstractFactory() {
        System.out.println("========== Testing Abstract Factory Pattern (via CommunicationController) ==========");

        // Set up factories
        CommunicationFactory smsFactory = new SMSFactory();
        CommunicationFactory emailFactory = new EmailFactory();

        // Set up controllers
        CommunicationController commController = new CommunicationController();
        MessageRepository messageRepo = new MessageRepository();
        MessageController messageController = new MessageController(messageRepo);

        // Create employee & customer via their controllers
        Employee employee = new Employee("EMP-001", "John Smith", "Support Agent", "john.smith@crm.com");

        CustomerRepository customerRepo = new CustomerRepository();
        CustomerController customerController = new CustomerController(customerRepo);
        Customer customer = new Customer("CUST-001", "Jane Doe", "jane@example.com", "123-456-7890");
        customerController.createCustomer("CUST-001", customer);
        System.out.println("Customer created via CustomerController: "
                + customer.getName() + " [Status: " + customer.getStatus() + "]");

        // Employee -> Customer via SMS
        System.out.println("\n--- Employee contacts Customer via SMS ---");
        commController.sendMessageFromEmployeeToCustomer(employee, customer,
                "Hello Jane, your account is ready!", smsFactory);

        // Customer -> Employee via Email
        System.out.println("\n--- Customer contacts Employee via Email ---");
        commController.sendMessageFromCustomerToEmployee(customer, employee,
                "Thank you! I need help updating my profile.", emailFactory);

        // Follow-up via MessageController
        System.out.println("\n--- MessageController sending a follow-up SMS ---");
        messageController.sendCommunication(smsFactory,
                "Follow-up: Is there anything else we can help you with?",
                "Follow-up Notification",
                customer.getEmail());

        System.out.println();
    }

    // 4. ABSTRACT FACTORY — PaymentController
    private static void testPaymentController() {
        System.out.println("========== Testing Payment Factory  ==========");

        PaymentRepository paymentRepo = new PaymentRepository();
        RefundRepository refundRepo = new RefundRepository();
        PaymentController paymentController = new PaymentController(paymentRepo, refundRepo);

        PaymentFactory cardFactory = new CardPaymentFactory();
        PaymentProcessor cardProcessor = cardFactory.createProcessor();
        Money amount = new Money(99.99, "USD");
        PaymentReceipt cardReceipt = cardFactory.createReceipt("TXN-101", amount);

        System.out.println("Processor created: " + cardProcessor.getClass().getSimpleName());
        System.out.println("Receipt created:   " + cardReceipt.getClass().getSimpleName());

        PaymentTransaction txn = new PaymentTransaction("TXN-101", "ORD-001", amount, PaymentMethodType.Card);
        paymentController.processPayment(txn);
        System.out.println("Payment processed & saved via PaymentController.");

        System.out.println();
    }

    // 5. FACTORY METHOD — ReportController
    private static void testReportController() {
        System.out.println("========== Testing Report Factory==========");

        ReportController reportController = new ReportController();
        ReportFactory reportFactory = new CustomerReportFactory();

        Report customerReport = reportController.generateReport(reportFactory, "RPT-202");
        System.out.println("Report created & generated via ReportController: "
                + customerReport.getClass().getSimpleName());

        System.out.println("======================================================");
>>>>>>> Stashed changes
    }
}
