package com.crm.customer.model;

import com.crm.customer.policies.SegmentationPolicy;

public class CustomerSegment {
    private String segmentId;
    private String name;
    private String criteriaDescription;
    private SegmentationPolicy strategy;

    public CustomerSegment(String segmentId, String name, String criteriaDescription) {
        this(segmentId, name, criteriaDescription, null);
    }

    public CustomerSegment(String segmentId, String name, String criteriaDescription,
            com.crm.customer.policies.SegmentationPolicy strategy) {
        this.segmentId = segmentId;
        this.name = name;
        this.criteriaDescription = criteriaDescription;
        this.strategy = strategy;
    }

    public String getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(String segmentId) {
        this.segmentId = segmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCriteriaDescription() {
        return criteriaDescription;
    }

    public void setCriteriaDescription(String criteriaDescription) {
        this.criteriaDescription = criteriaDescription;
    }

    public com.crm.customer.policies.SegmentationPolicy getStrategy() {
        return strategy;
    }

    public void setStrategy(com.crm.customer.policies.SegmentationPolicy strategy) {
        this.strategy = strategy;
    }

    public boolean belongsToSegment(Customer customer) {
        if (strategy == null) {
            return false;
        }
        return strategy.evaluate(customer);
    }
}
