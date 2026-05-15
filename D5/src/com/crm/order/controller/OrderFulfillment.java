package com.crm.order.controller;

import com.crm.common.enums.PaymentStatus;
import com.crm.communication.controller.CommunicationController;
import com.crm.communication.providers.CommunicationChannelProvider;
import com.crm.customer.model.Customer;
import com.crm.inventory.controller.ProductController;
import com.crm.order.model.Delivery;
import com.crm.order.model.Order;
import com.crm.order.model.OrderItem;
import com.crm.payment.controller.PaymentController;
import com.crm.payment.providers.PaymentProvider;
import com.crm.payment.model.PaymentTransaction;

public class OrderFulfillment {

    private final OrderController orderController;
    private final PaymentController paymentController;
    private final ProductController productController;
    private final CommunicationController communicationController;

    public OrderFulfillment(OrderController orderController,
            PaymentController paymentController,
            ProductController productController,
            CommunicationController communicationController) {
        this.orderController = orderController;
        this.paymentController = paymentController;
        this.productController = productController;
        this.communicationController = communicationController;
    }

    public void processCheckout(Order order, PaymentTransaction paymentTransaction, Customer customer,
            Delivery delivery, PaymentProvider paymentFactory, CommunicationChannelProvider commProvider) {
        System.out.println("\n[Mediator] Starting checkout process for Order: " + order.getOrderId());

        // 1. Deduct Stock
        System.out.println("[Mediator] Step 1: Deducting stock for order items...");
        for (OrderItem item : order.getItems()) {
            productController.deductStock(item.getProduct().getProductId(), item.getQuantity());
        }

        // 2. Attempt Payment
        System.out.println("[Mediator] Step 2: Attempting payment processing...");
        paymentController.processPayment(paymentTransaction, paymentFactory);

        if (paymentTransaction.getStatus() != PaymentStatus.Completed) {
            System.out.println("[Mediator] Checkout failed due to unsuccessful payment.");
            return;
        }
        orderController.attachPayment(order.getOrderId(), paymentTransaction);

        // 3. Attach Delivery
        System.out.println("[Mediator] Step 3: Attaching delivery...");
        orderController.attachDelivery(order.getOrderId(), delivery);

        // 4. Send Receipt
        System.out.println("[Mediator] Step 4: Sending receipt via CommunicationController...");
        String receiptMessage = "Receipt for Order " + order.getOrderId() + ". Amount paid: "
                + paymentTransaction.getAmount().getAmount() + " " + paymentTransaction.getAmount().getCurrency()
                + ". Your order will be delivered to: " + delivery.getAddress().getStreet();

        communicationController.sendMessage(commProvider, customer.getEmail(), receiptMessage);

        System.out.println("[Mediator] Checkout completed successfully.\n");
    }
}
