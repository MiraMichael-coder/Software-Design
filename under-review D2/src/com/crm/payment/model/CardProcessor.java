package com.crm.payment.model;

import com.crm.common.Money;

public class CardProcessor extends PaymentProcessor {
    @Override
    public boolean processPayment(Money amount) {
        System.out.println("Processing credit card payment for " + amount.getAmount() + " " + amount.getCurrency());
        // In a real app, you would integrate Stripe or another payment gateway here.
        return true;
    }
}
