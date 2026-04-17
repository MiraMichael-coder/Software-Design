package com.crm.payment.external;

// External Service (Adaptee)
// Simulates an external payment gateway 
public class StripeClient {
    public boolean charge(double amountInCents, String currencyCode) {
        System.out.println("[ Stripe ] Charging " + amountInCents + " cents in " + currencyCode);
        return true;
    }
}
