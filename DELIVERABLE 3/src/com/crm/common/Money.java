package com.crm.common;

import lombok.Getter;

@Getter
public class Money {
    private double amount;
    private String currency;

    public Money(double amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Money add(Money other) {
        if (other == null)
            return this;
        // Skeleton: assumes same currency for simplicity
        return new Money(this.amount + other.amount, this.currency);
    }

    public Money subtract(Money other) {
        if (other == null)
            return this;
        // Skeleton: assumes same currency for simplicity
        return new Money(this.amount - other.amount, this.currency);
    }

    public Money multiply(int i) {
        return new Money(this.amount * i, this.currency);
    }
}
