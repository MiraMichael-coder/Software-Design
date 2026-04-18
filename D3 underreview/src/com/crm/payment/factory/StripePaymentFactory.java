package com.crm.payment.factory;

import com.crm.payment.model.PaymentProcessor;
import com.crm.payment.model.PaymentReceipt;
import com.crm.payment.model.StripePayment;
import com.crm.payment.model.StripeReceipt;
import com.crm.common.Money;

// Abstract Factory Pattern — Concrete Factory for Stripe payments.
public class StripePaymentFactory implements PaymentFactory {
    @Override
    public PaymentProcessor createProcessor() {
        return new StripePayment();
    }

    @Override
    public PaymentReceipt createReceipt(String transactionId, Money amount) {
        return new StripeReceipt(transactionId, amount);
    }
}
