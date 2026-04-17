package com.crm.payment.model;

import com.crm.common.Money;

public abstract class PaymentProcessor {

    // Template Method — final so no subclass can reorder or skip steps.
    public final boolean executePayment(Money amount) {
        if (!validate(amount))
            return false; // common — null/zero check
        boolean authorized = authorize(amount); // abstract — card vs COD logic
        if (!authorized)
            return false; // common — abort on failure
        capture(amount); // abstract — payment-specific
        return true;
    }

    protected boolean validate(Money amount) {
        if (amount == null || amount.getAmount() <= 0) {
            System.out.println("[Payment] Validation failed: invalid amount.");
            return false;
        }
        System.out.println("[Payment] Amount validated: " + amount.getAmount() + " " + amount.getCurrency());
        return true;
    }

    protected abstract boolean authorize(Money amount);

    protected abstract void capture(Money amount);
}
