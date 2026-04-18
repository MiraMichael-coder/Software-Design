package com.crm.payment.controller;

import com.crm.payment.model.PaymentTransaction;
import com.crm.payment.model.Refund;
import com.crm.payment.repository.PaymentRepository;
import com.crm.payment.repository.RefundRepository;

public class PaymentController {
    private final PaymentRepository paymentRepository;
    private final RefundRepository refundRepository;

    public PaymentController(PaymentRepository paymentRepository, RefundRepository refundRepository) {
        this.paymentRepository = paymentRepository;
        this.refundRepository = refundRepository;
    }

    public void processPayment(PaymentTransaction paymentTransaction) {
        paymentRepository.save(paymentTransaction.getPaymentId(), paymentTransaction);
    }

    public void createRefund(Refund refund) {
        refundRepository.save(refund.getRefundId(), refund);
    }


}
