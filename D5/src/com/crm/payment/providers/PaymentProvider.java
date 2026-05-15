package com.crm.payment.providers;

import com.crm.payment.model.PaymentProcessor;
import com.crm.payment.model.PaymentReceipt;
import com.crm.common.Money;

// Abstract Factory Pattern — Interface for creating payment-related objects.
public interface PaymentProvider {
    PaymentProcessor createProcessor();
    PaymentReceipt createReceipt(String transactionId, Money amount);
}
