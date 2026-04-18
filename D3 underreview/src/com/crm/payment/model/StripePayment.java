package com.crm.payment.model;

import com.crm.common.Money;
import com.crm.payment.external.StripeClient;

//Adapter Pattern + Template Method Pattern — Concrete Adapter

public class StripePayment extends PaymentProcessor {

    private final StripeClient stripeClient;

    public StripePayment() {
        this.stripeClient = new StripeClient();
    }

    @Override
    protected boolean authorize(Money amount) {
        // Stripe expects the amount in cents.
        double amountInCents = amount.getAmount() * 100;
        System.out.println("[StripeAdapter] Authorising charge via Stripe: "
                + amountInCents + " cents in " + amount.getCurrency());
        return stripeClient.charge(amountInCents, amount.getCurrency());
    }

    @Override
    protected void capture(Money amount) {
        // Stripe's charge() is atomic — funds already settled in authorize().
        System.out.println("[StripeAdapter] Funds captured by Stripe (settled during authorisation).");
    }
}
