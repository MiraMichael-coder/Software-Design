package com.crm.payment.model;

import com.crm.common.Money;

public class CodReceipt extends PaymentReceipt {
    public CodReceipt(String transactionId, Money amount) {
        super(transactionId, amount);
    }

    @Override
    public void generateReceipt() {
        System.out.println("=== CASH ON DELIVERY RECEIPT ===");
        System.out.println("Order ID: " + transactionId);
        System.out.println("Amount to collect: " + amount.getAmount() + " " + amount.getCurrency());
        System.out.println("Method: Cash on Delivery (COD)");
        System.out.println("Status: PENDING COLLECTION");
        System.out.println("================================");
    }
}
