package com.crm.payment.model;

import com.crm.common.Money;

/**
 * Template Method Pattern — Concrete Class (Cash on Delivery)
 *
 * Implements only the two payment-type-specific hooks: authorize() and capture().
 * The overall sequence and receipt generation are entirely handled by the
 * final template method in PaymentProcessor. No receipt class is referenced here.
 */
public class CodProcessor extends PaymentProcessor {

    @Override
    protected boolean authorize(Money amount) {
        // COD orders are always pre-approved — no card network needed.
        System.out.println("[CodProcessor] COD order authorised automatically.");
        return true;
    }

    @Override
    protected void capture(Money amount) {
        // No immediate fund transfer; mark the order for cash collection on delivery.
        System.out.println("[CodProcessor] Order marked for cash collection: "
                + amount.getAmount() + " " + amount.getCurrency() + " to be collected on delivery.");
    }
}
