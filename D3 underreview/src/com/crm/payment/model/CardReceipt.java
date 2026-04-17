package com.crm.payment.model;

import com.crm.common.Money;

public class CardReceipt extends PaymentReceipt {
    public CardReceipt(String transactionId, Money amount) {
        super(transactionId, amount);
    }

    @Override
    public void generateReceipt() {
        System.out.println("=== CARD PAYMENT RECEIPT ===");
        System.out.println("Transaction ID: " + transactionId);
        System.out.println("Amount Paid: " + amount.getAmount() + " " + amount.getCurrency());
        System.out.println("Method: Credit Card");
        System.out.println("Status: APPROVED");
        System.out.println("============================");
    }
}
