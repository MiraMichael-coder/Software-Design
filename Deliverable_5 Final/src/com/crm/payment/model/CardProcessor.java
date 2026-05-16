package com.crm.payment.model;

import com.crm.common.Money;

/**
 * Template Method Pattern — Concrete Class (Card)
 *
 * Implements only the two payment-type-specific hooks: authorize() and capture().
 * The overall sequence and receipt generation are entirely handled by the
 * final template method in PaymentProcessor. No receipt class is referenced here.
 */
public class CardProcessor extends PaymentProcessor {

    @Override
    protected boolean authorize(Money amount) {
        // In a real app, this would contact a card network for authorisation.
        System.out.println("[CardProcessor] Authorising credit card charge of "
                + amount.getAmount() + " " + amount.getCurrency() + "...");
        System.out.println("[CardProcessor] Authorisation approved.");
        return true;
    }

    @Override
    protected void capture(Money amount) {
        // Settle / capture the previously authorised funds.
        System.out.println("[CardProcessor] Capturing funds: "
                + amount.getAmount() + " " + amount.getCurrency() + " settled.");
    }
}
