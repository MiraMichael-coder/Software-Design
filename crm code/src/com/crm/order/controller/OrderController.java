package com.crm.order.controller;

import com.crm.order.model.Order;
import com.crm.order.repository.OrderRepository;

public class OrderController {
    private final OrderRepository orderRepository;

    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

<<<<<<< Updated upstream
    public void createOrder(Order order) {
        orderRepository.save(order);
=======
    public void createOrder(String id,Order order) {
        orderRepository.save(id,order);
>>>>>>> Stashed changes
    }

    public Order retrieveOrder(String orderId) {
        return orderRepository.findById(orderId);
    }

<<<<<<< Updated upstream
    public void updateOrder(Order order) {
        orderRepository.update(order);
=======
    public void updateOrder(String id,Order order) {
        orderRepository.update(id,order);
>>>>>>> Stashed changes
    }

    public void cancelOrder(String orderId) {
        Order o = orderRepository.findById(orderId);
        if (o != null) {
            o.cancel();
<<<<<<< Updated upstream
            orderRepository.update(o);
=======
            orderRepository.update(orderId,o);
>>>>>>> Stashed changes
        }
    }
}
