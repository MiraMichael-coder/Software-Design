package com.crm.payment.factory;

import com.crm.payment.model.PaymentProcessor;
import com.crm.payment.model.PaymentReceipt;
import com.crm.payment.model.CardProcessor;
import com.crm.payment.model.CardReceipt;
import com.crm.common.Money;

public class CardPaymentFactory implements PaymentFactory {
    @Override
    public PaymentProcessor createProcessor() {
        return new CardProcessor();
    }

    @Override
    public PaymentReceipt createReceipt(String transactionId, Money amount) {
        return new CardReceipt(transactionId, amount);
    }
}
