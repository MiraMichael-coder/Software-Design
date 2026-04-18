package com.crm.payment.model;

import com.crm.common.Money;

public abstract class PaymentProcessor {
    public abstract boolean processPayment(Money amount);
}
