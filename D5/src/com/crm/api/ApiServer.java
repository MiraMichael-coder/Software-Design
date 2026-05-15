package com.crm.api;

import com.crm.inventory.controller.ProductController;
import com.crm.inventory.model.Product;
import com.crm.order.controller.OrderController;
import com.crm.order.model.Order;
import com.crm.common.Money;
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

public class ApiServer {
    private static final int PORT = 8080;
    private static final AtomicLong productCounter = new AtomicLong(100);
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
            .create();

    public static void start(ProductController productController, OrderController orderController) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
            // inventory apis
            server.createContext("/api/v1/inventory/products", new ProductHandler(productController));
            // orders apis
            server.createContext("/api/v1/orders", new OrderHandler(orderController));

            server.setExecutor(null); // creates a default executor
            server.start();
            System.out.println("API Server successfully started on port " + PORT);
        } catch (IOException e) {
            System.err.println("Failed to start API Server: " + e.getMessage());
        }
    }

    private static void sendResponse(HttpExchange exchange, int statusCode, String responseText) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
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

    static class ProductHandler implements HttpHandler {
        private final ProductController productController;

        public ProductHandler(ProductController productController) {
            this.productController = productController;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();

            if ("OPTIONS".equals(method)) {
                sendResponse(exchange, 204, "");
                return;
            }

            if ("GET".equals(method)) {
                if (path.equals("/api/v1/inventory/products") || path.equals("/api/v1/inventory/products/")) {
                    Collection<Product> products = productController.getAllProducts();
                    String json = gson.toJson(products);
                    sendResponse(exchange, 200, json);
                } else {
                    String[] parts = path.split("/");
                    // /api/v1/inventory/products/{id} -> parts length is 6
                    if (parts.length >= 6) {
                        String id = parts[5];
                        Product product = productController.getProduct(id);
                        if (product != null) {
                            sendResponse(exchange, 200, gson.toJson(product));
                        } else {
                            sendResponse(exchange, 404, "{\"error\":\"Product not found\"}");
                        }
                    } else {
                        sendResponse(exchange, 400, "{\"error\":\"Invalid path\"}");
                    }
                }
            } else if ("POST".equals(method)) {
                InputStream is = exchange.getRequestBody();
                String body = new String(is.readAllBytes(), "UTF-8");
                try {
                    ProductRequest req = gson.fromJson(body, ProductRequest.class);
                    String id = "PROD-" + productCounter.getAndIncrement();
                    Product newProduct = new Product(id, req.name, req.description,
                            new Money(req.priceAmount, req.priceCurrency), req.stockQuantity, req.supplierId);

                    productController.createProduct(newProduct);
                    sendResponse(exchange, 201, gson.toJson(newProduct));
                } catch (Exception e) {
                    sendResponse(exchange, 400,
                            "{\"error\":\"Invalid payload. Required fields: name, description, priceAmount, priceCurrency, stockQuantity, supplierId\"}");
                }
            } else if ("PUT".equals(method)) {
                String[] parts = path.split("/");
                // /api/v1/inventory/products/{id} -> parts length is 6
                if (parts.length >= 6) {
                    String id = parts[5];
                    Product product = productController.getProduct(id);
                    if (product != null) {
                        InputStream is = exchange.getRequestBody();
                        String body = new String(is.readAllBytes(), "UTF-8");
                        try {
                            ProductUpdateRequest req = gson.fromJson(body, ProductUpdateRequest.class);
                            if (req.name != null) product.setName(req.name);
                            if (req.basePrice != null) product.setUnitPrice(new Money(req.basePrice, product.getUnitPrice().getCurrency()));
                            productController.updateProduct(product);
                            sendResponse(exchange, 200, gson.toJson(product));
                        } catch (Exception e) {
                            sendResponse(exchange, 400, "{\"error\":\"Invalid payload\"}");
                        }
                    } else {
                        sendResponse(exchange, 404, "{\"error\":\"Product not found\"}");
                    }
                } else {
                    sendResponse(exchange, 400, "{\"error\":\"Invalid path\"}");
                }
            } else if ("DELETE".equals(method)) {
                String[] parts = path.split("/");
                // /api/v1/inventory/products/{id} -> parts length is 6
                if (parts.length >= 6) {
                    String id = parts[5];
                    Product product = productController.getProduct(id);
                    if (product != null) {
                        productController.deleteProduct(id);
                        sendResponse(exchange, 200, "{\"message\":\"Product deleted\"}");
                    } else {
                        sendResponse(exchange, 404, "{\"error\":\"Product not found\"}");
                    }
                } else {
                    sendResponse(exchange, 400, "{\"error\":\"Invalid path\"}");
                }
            } else {
                sendResponse(exchange, 405, "{\"error\":\"Method Not Allowed\"}");
            }
        }
    }

    static class OrderHandler implements HttpHandler {
        private final OrderController orderController;

        public OrderHandler(OrderController orderController) {
            this.orderController = orderController;
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
                    // /api/v1/orders/{id} -> parts length is 5
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
                // /api/v1/orders/{id}/delivery -> parts length is 6
                if (parts.length >= 6 && "delivery".equals(parts[5])) {
                    String id = parts[4];
                    Order order = orderController.getOrder(id);
                    if (order != null) {
                        InputStream is = exchange.getRequestBody();
                        String body = new String(is.readAllBytes(), "UTF-8");
                        try {
                            DeliveryRequest req = gson.fromJson(body, DeliveryRequest.class);
                            com.crm.common.Address address = new com.crm.common.Address(req.address != null ? req.address : "", "", "");
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
                // /api/v1/orders/{id}/items/{itemId} -> parts length is 7
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

    // DTOs for Request parsing
    static class ProductRequest {
        String name;
        String description;
        double priceAmount;
        String priceCurrency;
        int stockQuantity;
        String supplierId;
    }

    static class OrderRequest {
        String customerId;
    }

    static class ProductUpdateRequest {
        String name;
        Double basePrice;
    }

    static class DeliveryRequest {
        String address;
        String method;
    }
}
