package com.crm.order.controller;

import com.crm.common.enums.OrderStatus;
import com.crm.order.model.Delivery;
import com.crm.order.model.Order;
import com.crm.order.model.OrderItem;
import com.crm.order.repository.OrderRepository;
import com.crm.payment.model.PaymentTransaction;

public class OrderController {
    private final OrderRepository orderRepository;

    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void createOrder(Order order) {
        orderRepository.save(order.getOrderId(), order);
    }

    public Order getOrder(String orderId) {
        return orderRepository.findById(orderId);
    }

    public void addItem(String orderId, OrderItem item) {
        Order order = orderRepository.findById(orderId);
        if (order == null) {
            return;
        }
        order.addItem(item);
        orderRepository.update(orderId, order);
    }

    public void removeItem(String orderId, String orderItemId) {
        Order order = orderRepository.findById(orderId);
        if (order == null) {
            return;
        }
        order.removeItemById(orderItemId);
        orderRepository.update(orderId, order);
    }

    public void attachDelivery(String orderId, Delivery delivery) {
        Order order = orderRepository.findById(orderId);
        if (order == null) {
            return;
        }
        order.attachDelivery(delivery);
        orderRepository.update(orderId, order);
    }

    public void attachPayment(String orderId, PaymentTransaction paymentTransaction) {
        Order order = orderRepository.findById(orderId);
        if (order == null) {
            System.out.println("Order not found");
        } else {
            order.attachPayment(paymentTransaction);
            orderRepository.update(orderId, order);
        }
    }

    public void updateStatus(String orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId);
        if (order == null) {
            System.out.println("Order not found");

        } else {
            order.updateStatus(status);
            orderRepository.update(orderId, order);
        }
    }

    public void cancelOrder(String orderId) {
        Order order = orderRepository.findById(orderId);
        if (order == null) {
            System.out.println("Order not found");

        } else {
            order.cancel();
            orderRepository.update(orderId, order);
        }
    }
    public void deleteOrder(String orderId) {
        Order order = orderRepository.findById(orderId);
        if (order == null) {
            System.out.println("Order not found");

        } else {
            orderRepository.delete(orderId);
        }
    }
}
