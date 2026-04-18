package com.crm.payment.model;

import com.crm.common.Money;

// stripe adapter recipet 
public class StripeReceipt extends PaymentReceipt {
    public StripeReceipt(String transactionId, Money amount) {
        super(transactionId, amount);
    }

    @Override
    public void generateReceipt() {
        System.out.println("=== STRIPE PAYMENT RECEIPT ===");
        System.out.println("Transaction ID: " + transactionId);
        System.out.println("Amount Paid: " + amount.getAmount() + " " + amount.getCurrency());
        System.out.println("Method: External Gateway (Stripe)");
        System.out.println("Status: PAID");
        System.out.println("==============================");
    }
}
