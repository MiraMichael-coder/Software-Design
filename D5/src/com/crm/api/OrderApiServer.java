package com.crm.api;

import com.crm.order.controller.OrderController;
import com.crm.order.model.Order;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;

public class OrderApiServer {
    private static final int PORT = 8081;
    private static final AtomicLong orderCounter = new AtomicLong(100);
    private static final Gson gson = new com.google.gson.GsonBuilder()
            .registerTypeAdapter(java.time.LocalDateTime.class,
                    new com.google.gson.JsonSerializer<java.time.LocalDateTime>() {
                        @Override
                        public com.google.gson.JsonElement serialize(java.time.LocalDateTime src,
                                java.lang.reflect.Type typeOfSrc, com.google.gson.JsonSerializationContext context) {
                            return new com.google.gson.JsonPrimitive(
                                    src.format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                        }
                    })
            .registerTypeAdapter(com.crm.common.Money.class,
                    new com.google.gson.JsonSerializer<com.crm.common.Money>() {
                        @Override
                        public com.google.gson.JsonElement serialize(com.crm.common.Money src,
                                java.lang.reflect.Type typeOfSrc, com.google.gson.JsonSerializationContext context) {
                            com.google.gson.JsonObject obj = new com.google.gson.JsonObject();
                            obj.addProperty("amount", Math.round(src.getAmount() * 100.0) / 100.0);
                            obj.addProperty("currency", src.getCurrency());
                            return obj;
                        }
                    })
            .registerTypeAdapter(com.crm.order.model.OrderItem.class,
                    new com.google.gson.JsonSerializer<com.crm.order.model.OrderItem>() {
                        @Override
                        public com.google.gson.JsonElement serialize(com.crm.order.model.OrderItem src,
                                java.lang.reflect.Type typeOfSrc, com.google.gson.JsonSerializationContext context) {
                            com.google.gson.JsonObject obj = new com.google.gson.JsonObject();
                            obj.addProperty("orderItemId", src.getOrderItemId());
                            if (src.getProduct() != null) {
                                obj.addProperty("productId", src.getProduct().getProductId());
                                obj.addProperty("productName", src.getProduct().getName());
                            }
                            obj.addProperty("quantity", src.getQuantity());
                            obj.add("unitPrice", context.serialize(src.getUnitPrice()));
                            obj.add("subtotal", context.serialize(src.calculateSubtotal()));
                            return obj;
                        }
                    })
            .create();

    public static void start(OrderController orderController, com.crm.inventory.controller.ProductController productController) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
            server.createContext("/api/v1/orders", new OrderHandler(orderController, productController));
            server.setExecutor(null);
            server.start();
            System.out.println("Order API Server successfully started on port " + PORT);
        } catch (IOException e) {
            System.err.println("Failed to start Order API Server: " + e.getMessage());
        }
    }

    private static void sendResponse(HttpExchange exchange, int statusCode, String responseText) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
        
        if (statusCode == 204) {
            exchange.sendResponseHeaders(statusCode, -1);
            return;
        }
        
        byte[] bytes = responseText.getBytes("UTF-8");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }

    static class OrderHandler implements HttpHandler {
        private final OrderController orderController;
        private final com.crm.inventory.controller.ProductController productController;

        public OrderHandler(OrderController orderController, com.crm.inventory.controller.ProductController productController) {
            this.orderController = orderController;
            this.productController = productController;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();

            if ("OPTIONS".equals(method)) {
                sendResponse(exchange, 204, "");
                return;
            }

            if ("GET".equals(method)) {
                String path = exchange.getRequestURI().getPath();
                if (path.equals("/api/v1/orders") || path.equals("/api/v1/orders/")) {
                    Collection<Order> orders = orderController.getAllOrders();
                    String json = gson.toJson(orders);
                    sendResponse(exchange, 200, json);
                } else {
                    String[] parts = path.split("/");
                    if (parts.length >= 5) {
                        String id = parts[4];
                        Order order = orderController.getOrder(id);
                        if (order != null) {
                            sendResponse(exchange, 200, gson.toJson(order));
                        } else {
                            sendResponse(exchange, 404, "{\"error\":\"Order not found\"}");
                        }
                    } else {
                        sendResponse(exchange, 400, "{\"error\":\"Invalid path\"}");
                    }
                }
            } else if ("POST".equals(method)) {
                InputStream is = exchange.getRequestBody();
                String body = new String(is.readAllBytes(), "UTF-8");
                try {
                    OrderRequest req = gson.fromJson(body, OrderRequest.class);
                    if (req != null && req.customerId != null) {
                        Order newOrder = new Order("ORD-" + orderCounter.getAndIncrement(), req.customerId);
                        orderController.createOrder(newOrder);
                        
                        if (req.items != null && !req.items.isEmpty()) {
                            int itemCounter = 1;
                            for (ItemRequest itemReq : req.items) {
                                com.crm.inventory.model.Product realProduct = productController != null ? productController.getProduct(itemReq.productId) : null;
                                if (realProduct == null) {
                                    realProduct = new com.crm.inventory.model.Product(
                                        itemReq.productId != null ? itemReq.productId : "UNKNOWN",
                                        "API Product", "", 
                                        new com.crm.common.Money(itemReq.priceAmount, itemReq.priceCurrency != null ? itemReq.priceCurrency : "USD"), 
                                        0, ""
                                    );
                                }
                                com.crm.order.model.OrderItem orderItem = new com.crm.order.model.OrderItem(
                                    newOrder.getOrderId() + "-ITEM-" + itemCounter++,
                                    realProduct,
                                    itemReq.quantity > 0 ? itemReq.quantity : 1,
                                    itemReq.priceAmount > 0 ? new com.crm.common.Money(itemReq.priceAmount, itemReq.priceCurrency != null ? itemReq.priceCurrency : "USD") : realProduct.getUnitPrice()
                                );
                                orderController.addItem(newOrder.getOrderId(), orderItem);
                            }
                            newOrder = orderController.getOrder(newOrder.getOrderId());
                        }
                        
                        sendResponse(exchange, 201, gson.toJson(newOrder));
                    } else {
                        sendResponse(exchange, 400, "{\"error\":\"Missing customerId\"}");
                    }
                } catch (Exception e) {
                    sendResponse(exchange, 400, "{\"error\":\"Invalid payload\"}");
                }
            } else if ("PUT".equals(method)) {
                String path = exchange.getRequestURI().getPath();
                String[] parts = path.split("/");
                if (parts.length >= 6 && "delivery".equals(parts[5])) {
                    String id = parts[4];
                    Order order = orderController.getOrder(id);
                    if (order != null) {
                        InputStream is = exchange.getRequestBody();
                        String body = new String(is.readAllBytes(), "UTF-8");
                        try {
                            DeliveryRequest req = gson.fromJson(body, DeliveryRequest.class);
                            String addressInput = req.address != null ? req.address : "";
                            String[] addrParts = addressInput.split(",");
                            String street = addrParts.length > 0 ? addrParts[0].trim() : "";
                            String city = addrParts.length > 1 ? addrParts[1].trim() : "";
                            String zone = addrParts.length > 2 ? addrParts[2].trim() : "";
                            com.crm.common.Address address = new com.crm.common.Address(street, city, zone);
                            com.crm.order.model.Delivery delivery = new com.crm.order.model.Delivery("DEL-" + System.currentTimeMillis(), id, address, java.time.LocalDateTime.now().plusDays(3));
                            orderController.attachDelivery(id, delivery);
                            sendResponse(exchange, 200, gson.toJson(orderController.getOrder(id)));
                        } catch (Exception e) {
                            sendResponse(exchange, 400, "{\"error\":\"Invalid payload\"}");
                        }
                    } else {
                        sendResponse(exchange, 404, "{\"error\":\"Order not found\"}");
                    }
                } else {
                    sendResponse(exchange, 400, "{\"error\":\"Invalid path\"}");
                }
            } else if ("DELETE".equals(method)) {
                String path = exchange.getRequestURI().getPath();
                String[] parts = path.split("/");
                if (parts.length >= 7 && "items".equals(parts[5])) {
                    String id = parts[4];
                    String itemId = parts[6];
                    Order order = orderController.getOrder(id);
                    if (order != null) {
                        orderController.removeItem(id, itemId);
                        sendResponse(exchange, 200, gson.toJson(orderController.getOrder(id)));
                    } else {
                        sendResponse(exchange, 404, "{\"error\":\"Order not found\"}");
                    }
                } else {
                    sendResponse(exchange, 400, "{\"error\":\"Invalid path\"}");
                }
            } else {
                sendResponse(exchange, 405, "{\"error\":\"Method Not Allowed\"}");
            }
        }
    }

    static class OrderRequest {
        String customerId;
        java.util.List<ItemRequest> items;
    }

    static class ItemRequest {
        String productId;
        int quantity;
        double priceAmount;
        String priceCurrency;
    }

    static class DeliveryRequest {
        String address;
        String method;
    }
}
