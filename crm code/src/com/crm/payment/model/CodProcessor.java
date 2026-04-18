package com.crm.payment.model;

import com.crm.common.Money;

public class CodProcessor extends PaymentProcessor {
    @Override
    public boolean processPayment(Money amount) {
        System.out.println("No immediate processing for Cash on Delivery. Marking order for payment upon delivery.");
        return true;
    }
}
