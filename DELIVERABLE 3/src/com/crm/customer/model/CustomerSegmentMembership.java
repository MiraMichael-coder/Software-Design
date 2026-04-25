package com.crm.customer.model;

import java.time.LocalDateTime;

public class CustomerSegmentMembership {
    private String membershipId;
    private String customerId;
    private String segmentId;
    private LocalDateTime assignedDate;

    public CustomerSegmentMembership(String membershipId, String customerId, String segmentId) {
        this.membershipId = membershipId;
        this.customerId = customerId;
        this.segmentId = segmentId;
        this.assignedDate = LocalDateTime.now();
    }

    public String getMembershipId() { return membershipId; }
    public void setMembershipId(String membershipId) { this.membershipId = membershipId; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getSegmentId() { return segmentId; }
    public void setSegmentId(String segmentId) { this.segmentId = segmentId; }

    public LocalDateTime getAssignedDate() { return assignedDate; }
}
