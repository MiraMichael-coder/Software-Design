package com.crm.payment.model;

import com.crm.common.Money;
import com.crm.common.enums.RefundStatus;
import java.time.LocalDateTime;

public class Refund {
    private String refundId;
    private String orderId;
    private Money amount;
    private String reason;
    private RefundStatus status;
    private LocalDateTime createdAt;

    public Refund(String refundId, String orderId, Money amount, String reason) {
        this.refundId = refundId;
        this.orderId = orderId;
        this.amount = amount;
        this.reason = reason;
        this.status = RefundStatus.Pending;
        this.createdAt = LocalDateTime.now();
    }

    public String getRefundId() { return refundId; }
    public void setRefundId(String refundId) { this.refundId = refundId; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public Money getAmount() { return amount; }
    public void setAmount(Money amount) { this.amount = amount; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public RefundStatus getStatus() { return status; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public void approveRefund() { this.status = RefundStatus.Approved; }
    public void rejectRefund() { this.status = RefundStatus.Rejected; }
    public void processRefund() { this.status = RefundStatus.Processed; }
}
