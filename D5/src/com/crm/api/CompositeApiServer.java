package com.crm.api;

import com.crm.common.Money;
import com.crm.common.enums.PaymentMethodType;
import com.crm.common.enums.PaymentStatus;
import com.crm.inventory.controller.ProductController;
import com.crm.inventory.model.Product;
import com.crm.order.controller.OrderController;
import com.crm.order.model.Order;
import com.crm.order.model.OrderItem;
import com.crm.payment.controller.PaymentController;
import com.crm.payment.model.PaymentTransaction;
import com.crm.payment.providers.CardPaymentProvider;
import com.crm.payment.providers.PaymentProvider;
import com.crm.communication.controller.CommunicationController;
import com.crm.communication.providers.EmailChannelProvider;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Composite Service that orchestrates multiple domain services:
 * 1. Inventory (Stock check & deduction)
 * 2. Order (Creation)
 * 3. Payment (Processing)
 * 4. Communication (Notification)
 */
public class CompositeApiServer {
    private static final int PORT = 8082;
    private static final AtomicLong orderCounter = new AtomicLong(500);
    private static final Gson gson = new com.google.gson.GsonBuilder().setPrettyPrinting().create();

    public static void start(OrderController orderController,
            ProductController productController,
            PaymentController paymentController,
            CommunicationController communicationController) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
            server.createContext("/api/v1/composite/place-order", new CheckoutHandler(
                    orderController, productController, paymentController, communicationController));
            server.setExecutor(null);
            server.start();
            System.out.println("Composite API Server (Orchestrator) started on port " + PORT);
        } catch (IOException e) {
            System.err.println("Failed to start Composite API Server: " + e.getMessage());
        }
    }

    private static void sendResponse(HttpExchange exchange, int statusCode, String responseText) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        byte[] bytes = responseText.getBytes("UTF-8");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }

    static class CheckoutHandler implements HttpHandler {
        private final OrderController orderController;
        private final ProductController productController;
        private final PaymentController paymentController;
        private final CommunicationController communicationController;

        public CheckoutHandler(OrderController orderController,
                ProductController productController,
                PaymentController paymentController,
                CommunicationController communicationController) {
            this.orderController = orderController;
            this.productController = productController;
            this.paymentController = paymentController;
            this.communicationController = communicationController;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"POST".equals(exchange.getRequestMethod())) {
                sendResponse(exchange, 405, "{\"error\":\"Method Not Allowed\"}");
                return;
            }

            InputStream is = exchange.getRequestBody();
            String body = new String(is.readAllBytes(), "UTF-8");

            try {
                CheckoutRequest req = gson.fromJson(body, CheckoutRequest.class);
                if (req == null || req.customerId == null || req.items == null || req.items.isEmpty()) {
                    sendResponse(exchange, 400, "{\"error\":\"Invalid request. Missing customerId or items.\"}");
                    return;
                }

                System.out.println("[Composite Service] Orchestrating checkout for customer: " + req.customerId);

                // 1. Validate Stock via Inventory Service
                List<OrderItem> orderItems = new ArrayList<>();
                double totalAmount = 0;
                for (CheckoutItem itemReq : req.items) {
                    Product product = productController.getProduct(itemReq.productId);
                    if (product == null) {
                        sendResponse(exchange, 404, "{\"error\":\"Product not found: " + itemReq.productId + "\"}");
                        return;
                    }
                    if (product.getStockQuantity() < itemReq.quantity) {
                        sendResponse(exchange, 400,
                                "{\"error\":\"Insufficient stock for product: " + product.getName() + "\"}");
                        return;
                    }

                    OrderItem orderItem = new OrderItem("ITEM-" + System.currentTimeMillis(), product, itemReq.quantity,
                            product.getUnitPrice());
                    orderItems.add(orderItem);
                    totalAmount += product.getUnitPrice().getAmount() * itemReq.quantity;
                }

                // 2. Create Order via Order Service
                String orderId = "ORD-COMP-" + orderCounter.getAndIncrement();
                Order order = new Order(orderId, req.customerId);
                orderController.createOrder(order);
                for (OrderItem item : orderItems) {
                    orderController.addItem(orderId, item);
                }

                // 3. Process Payment via Payment Service
                PaymentTransaction txn = new PaymentTransaction("TXN-" + System.currentTimeMillis(),
                        orderId, new Money(totalAmount, "EGP"),
                        req.paymentMethod != null ? req.paymentMethod : PaymentMethodType.Card);

                PaymentProvider paymentProvider = new CardPaymentProvider(); // Defaulting to Card for demo
                paymentController.processPayment(txn, paymentProvider);

                if (txn.getStatus() != PaymentStatus.Completed) {
                    orderController.updateStatus(orderId, com.crm.common.enums.OrderStatus.Cancelled);
                    sendResponse(exchange, 402, "{\"error\":\"Payment failed\", \"orderId\":\"" + orderId + "\"}");
                    return;
                }
                orderController.attachPayment(orderId, txn);

                // 4. Deduct Stock via Inventory Service
                for (OrderItem item : orderItems) {
                    productController.deductStock(item.getProduct().getProductId(), item.getQuantity());
                }

                // 5. Send Notification via Communication Service
                communicationController.sendMessage(new EmailChannelProvider(),
                        "customer@example.com", // In a real app, fetch from customer controller
                        "Success! Your order " + orderId + " has been placed and paid.");

                // Return Success
                CheckoutResponse resp = new CheckoutResponse(orderId, "Completed", totalAmount, "EGP");
                sendResponse(exchange, 201, gson.toJson(resp));

            } catch (Exception e) {
                e.printStackTrace();
                sendResponse(exchange, 500, "{\"error\":\"Internal server error: " + e.getMessage() + "\"}");
            }
        }
    }

    static class CheckoutRequest {
        String customerId;
        List<CheckoutItem> items;
        PaymentMethodType paymentMethod;
    }

    static class CheckoutItem {
        String productId;
        int quantity;
    }

    static class CheckoutResponse {
        String orderId;
        String status;
        double total;
        String currency;

        public CheckoutResponse(String orderId, String status, double total, String currency) {
            this.orderId = orderId;
            this.status = status;
            this.total = total;
            this.currency = currency;
        }
    }
}
