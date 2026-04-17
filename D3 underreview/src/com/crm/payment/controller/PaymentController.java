package com.crm.payment.controller;

import com.crm.payment.factory.PaymentFactory;
import com.crm.payment.model.*;
import com.crm.payment.repository.PaymentRepository;
import com.crm.payment.repository.RefundRepository;

public class PaymentController {
    private final PaymentRepository paymentRepository;
    private final RefundRepository refundRepository;

    public PaymentController(PaymentRepository paymentRepository, RefundRepository refundRepository) {
        this.paymentRepository = paymentRepository;
        this.refundRepository = refundRepository;
    }

    public void processPayment(PaymentTransaction paymentTransaction, PaymentFactory factory) {

        System.out.println("Processing " + paymentTransaction.getMethod() + " payment for Order: "
                + paymentTransaction.getOrderId());

        // 1. Create the appropriate processor and receipt via the factory.
        PaymentProcessor processor = factory.createProcessor();
        PaymentReceipt receipt = factory.createReceipt(paymentTransaction.getPaymentId(),
                paymentTransaction.getAmount());

        // 2. Execute the Template Method — enforces validate → authorize → capture →
        // generateReceipt.

        boolean success = processor.executePayment(paymentTransaction.getAmount());

        if (success) {
            receipt.generateReceipt();
            paymentTransaction.markCompleted();
            // System.out.println("Payment Successful!");
        } else {
            paymentTransaction.markFailed();
            System.out.println("Payment Failed.");
        }

        // 3. Persist the transaction result.
        paymentRepository.save(paymentTransaction.getPaymentId(), paymentTransaction);
    }

    public void createRefund(Refund refund) {
        System.out.println("Creating refund for Order: " + refund.getOrderId());
        refundRepository.save(refund.getRefundId(), refund);
    }
}
