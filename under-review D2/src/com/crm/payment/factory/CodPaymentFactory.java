package com.crm.payment.factory;

import com.crm.payment.model.*;
import com.crm.common.Money;

public class CodPaymentFactory implements PaymentFactory {
    @Override
    public PaymentProcessor createProcessor() {
        return new CodProcessor();
    }

    @Override
    public PaymentReceipt createReceipt(String transactionId, Money amount) {
        return new CodReceipt(transactionId, amount);
    }
}
