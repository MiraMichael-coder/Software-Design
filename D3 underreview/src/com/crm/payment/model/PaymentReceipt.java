package com.crm.payment.model;

import com.crm.common.Money;

public abstract class PaymentReceipt {
    protected String transactionId;
    protected Money amount;

    public PaymentReceipt(String transactionId, Money amount) {
        this.transactionId = transactionId;
        this.amount = amount;
    }

    public abstract void generateReceipt();
}
