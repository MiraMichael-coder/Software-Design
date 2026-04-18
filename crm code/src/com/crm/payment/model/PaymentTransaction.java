package com.crm.payment.model;

import com.crm.common.Money;
import com.crm.common.enums.PaymentMethodType;
import com.crm.common.enums.PaymentStatus;
import java.time.LocalDateTime;

public class PaymentTransaction {
    private String paymentId;
    private String orderId;
    private Money amount;
    private PaymentMethodType method;
    private PaymentStatus status;
    private LocalDateTime transactionDate;

    public PaymentTransaction(String paymentId, String orderId, Money amount, PaymentMethodType method) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.amount = amount;
        this.method = method;
        this.status = PaymentStatus.Pending;
        this.transactionDate = LocalDateTime.now();
    }

<<<<<<< Updated upstream
    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public Money getAmount() { return amount; }
    public void setAmount(Money amount) { this.amount = amount; }

    public PaymentMethodType getMethod() { return method; }
    public void setMethod(PaymentMethodType method) { this.method = method; }

    public PaymentStatus getStatus() { return status; }

    public LocalDateTime getTransactionDate() { return transactionDate; }
=======
    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Money getAmount() {
        return amount;
    }

    public void setAmount(Money amount) {
        this.amount = amount;
    }

    public PaymentMethodType getMethod() {
        return method;
    }

    public void setMethod(PaymentMethodType method) {
        this.method = method;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }
>>>>>>> Stashed changes

    public boolean validatePayment() {
        return amount != null && amount.getAmount() > 0;
    }

    public void markCompleted() {
        this.status = PaymentStatus.Completed;
    }

    public void markFailed() {
        this.status = PaymentStatus.Failed;
    }
}
