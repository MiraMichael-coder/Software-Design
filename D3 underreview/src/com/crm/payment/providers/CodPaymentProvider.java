package com.crm.payment.providers;

import com.crm.payment.model.*;
import com.crm.common.Money;

public class CodPaymentProvider implements PaymentProvider {
    @Override
    public PaymentProcessor createProcessor() {
        return new CodProcessor();
    }

    @Override
    public PaymentReceipt createReceipt(String transactionId, Money amount) {
        return new CodReceipt(transactionId, amount);
    }
}
