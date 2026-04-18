package com.crm.payment.model;

import com.crm.common.Money;
import com.crm.payment.external.StripeClient;

// Adapter Pattern — Concrete Adapter
// Adapts the external StripeClient to the PaymentProcessor interface.
public class StripePaymentAdapter extends PaymentProcessor {
    private StripeClient stripeClient;

    public StripePaymentAdapter() {
        this.stripeClient = new StripeClient();
    }

    @Override
    public boolean processPayment(Money amount) {
        // Stripe expects the amount in cents
        double amountInCents = amount.getAmount() * 100;
        return stripeClient.charge(amountInCents, amount.getCurrency());
    }
}
