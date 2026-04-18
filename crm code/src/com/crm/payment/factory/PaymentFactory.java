package com.crm.payment.factory;

import com.crm.payment.model.PaymentProcessor;
import com.crm.payment.model.PaymentReceipt;
import com.crm.common.Money;

public interface PaymentFactory {
    PaymentProcessor createProcessor();
    PaymentReceipt createReceipt(String transactionId, Money amount);
}
